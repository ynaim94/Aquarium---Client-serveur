/*
  If passed an aquarium
  And an action
*/

#define _GNU_SOURCE         /* See feature_test_macros(7) */
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include "intern.h"


#define TAILLE_MAX 1000

extern int state;
int del__element(View t[], int id, int* length){
  int i = id+1;
  if (id > *length - 1){
    return 0;
  }
  for (i; i < *length; i++){
    t[i-1] = t[i];
  }
  *length--;
  return 1;
}


FILE* open(const char* file_name){
  FILE* file = NULL;
  file = fopen(file_name, "r");
  if (file == NULL){
    perror("fichier");
    return EXIT_SUCCESS;
  }
  return file;
}

/*
 * put the heigh and width from the file in the array aquarium
 * param fd : pointer of an opened file
 * param aquarium: array aquarium that we'll change
 *
 */
int get_aquarium_dim(FILE* fd, int* aquarium){
  char chaine1[TAILLE_MAX+1] = "";
  char chaine2[TAILLE_MAX+1] = "";
  char * pEnd;
  long heigh;
  int  i = 0;
  rewind(fd);

  chaine1[i] = fgetc(fd);
  while (chaine1[i] != 'x') {
    i++;
    chaine1[i] = fgetc(fd);
  }
  aquarium[0] = strtol(chaine1,&pEnd,10);
  if (fgets(chaine2, TAILLE_MAX, fd) == NULL){
    perror("fgets");
    return EXIT_FAILURE;
  }
  aquarium[1] = strtol(chaine2,&pEnd,10);
  return 1;
}


int get_aquarium_views(FILE* fd, View* views){
  char line[TAILLE_MAX+1] = "";
  char *param[5]; //param[0] <=> x , param[1] <=> y, param[2] <=> width, param[3] <=> height
  //param[5] <=> id
  char *pEnd;
  int i,j;
  char actual_char;

  rewind(fd);

  /*get rid of first line*/
  if (fgets(line,TAILLE_MAX,fd) == NULL){
    perror("fgets");
    return EXIT_FAILURE;
  }

  actual_char = fgetc(fd);

  /*While there is more view to read*/
  while ( (actual_char != '\n') && (nb_views < MAX_VIEW)){
    /*get rid of the name of the view*/
    param[4] = malloc(TAILLE_MAX+1);
    actual_char = fgetc(fd);
    i = 0;
    while (actual_char != ' '){
      param[4][i] = actual_char;
      actual_char =  fgetc(fd);
      i++;
    }
    param[4][i] = '\0';
    for (j = 0; j < 4; j++){
      /*Allocate space for the view parameters (x,y,width,heigh)*/
      param[j] = malloc(TAILLE_MAX+1);
      i = 0;
      actual_char = fgetc(fd);
      while ((actual_char != 'x') && (actual_char != '+') &&
	     (actual_char != '\n') && (actual_char != EOF)) {
	param[j][i] = actual_char;
	actual_char = fgetc(fd);
	//	printf("i : %d, j : %d param %s\n",i,j,param[j]);
	i++;

      }
      param[j][i] = '\0';
    }

    /* affect value of each parameter*/
    views[nb_views].x = strtol(param[0],&pEnd,10);
    views[nb_views].y = strtol(param[1],&pEnd,10);
    views[nb_views].width = strtol(param[2],&pEnd,10);
    views[nb_views].height = strtol(param[3],&pEnd,10);
    views[nb_views].id = strtol(param[4],&pEnd,10);
    views[nb_views].state = FREE;
    nb_views++;

    for (j = 0; j < 5; j++){
      free(param[j]);
    }

    actual_char = fgetc(fd);
  }
  return EXIT_SUCCESS;
}


/*
 * Load an aquarium file and return the answer message for the prompt
 * param file_name: Name of the file of the aquarium.
 * param state: Boolean to know if it's already loaded or not
 *
 */
char* intern__load(const char* file_name, int* aquarium){
  FILE* fd = NULL;
  int i = 0;
  char *msg;
  if (state == 1){
    return "Aquarium already loaded";
  }

  nb_views = 0;
  fd = open(file_name);
  if (fd == NULL){
    return "File does not exit";
  }

  get_aquarium_dim(fd,aquarium);// TODO: Tester si success ou failure
  get_aquarium_views(fd,views); // TODO: Tester si success ou failure
  state = 1; //TODO : 0 if failure

  fclose(fd);

  asprintf(&msg,"-> aquarium loaded (%d display view)", nb_views);

  state = 1;

  return msg;
}


char* intern__show(){
  int i;
  char *s,*msg;
  msg = malloc (nb_views*25 + 9);
  asprintf(&s, "%dx%d\n", aquarium[0],aquarium[1]);
  strcpy(msg, s);
  //TODO
  for (i = 0; i < nb_views; i++){
      asprintf(&s, "N%d %dx%d+%d+%d\n", views[i].id, views[i].x, views[i].y, views[i].width, views[i].height);
      strcat(msg,s);
  }

  return msg;

}

char* intern__add(View view){

  int i;
  for (i = 0; i < nb_views; i++){
    if (view.id == views[i].id){
      return "-> view name already exist";
    }
  }

  //TODO: parametre plus petit que aquarium[0]*aquarium[1]
  if ((view.x <0) || (view.x > aquarium[0]) || (view.x + view.width > aquarium[0]) ||
      (view.y <0) || (view.y > aquarium[1]) || (view.y + view.height > aquarium[1]) ) {
    return "-> view parameters don't fit in the aquarium";
  }
  nb_views++;
  views[nb_views-1] = view;
  return "-> view added";
}


char* intern__del(int id){
  int i;
  char *msg;
  for (i = 0; i < nb_views; i++){
    if (views[i].id == id){
      //TODO
      del__element(views,i,&nb_views);
      asprintf(&msg,"-> view N%d deleted.", id);
      nb_views--;
      return msg;
    }
  }
  return "-> view does not exist";
}

char* intern__save(char* file_name){
  FILE* file = NULL;
  char *msg;
  file = fopen(file_name, "w");
  if (file == NULL){
    perror("fichier");
    return EXIT_SUCCESS;
  }
  char *aqua = intern__show();

  fwrite(aqua,1,strlen(aqua),file);

  fclose(file);

  asprintf(&msg, "-> Aquarium saved !(%d display view)", nb_views);
  return msg;
}



/*int main (char *args[]){

  printf("%s\n",intern__load("aquarium.info", 0, aquarium));
  View v = {7,1,2,3,4,1};
  printf("%s\n",intern__add(v));
  printf("%s\n", intern__show());
  printf("%s\n", intern__del(1));
  printf("%s\n", intern__show());
  printf("%s\n", intern__del(9));
  printf("%s\n", intern__save("aquarium_save.info"));
}
*/
