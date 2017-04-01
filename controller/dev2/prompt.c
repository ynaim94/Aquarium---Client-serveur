#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include "intern.h"
#include "parser.h"
#include "view.h"

#define BUFFER_SIZE  256
#define TAILLE_MAX 1000 // A redéfinir avec TAILLE_MAX de intern.c

int get_view_id(char* str){
  char* pEnd;
  if (str == NULL){
    return -1;
  }
  str++;
  return strtol(str,&pEnd,10);
}

View* parse_view(char* str){
  char* param[4], *pEnd;
  int i, j;
  View *view = malloc (sizeof(View));

  //  printf("%s\n",str);
  
  /* test if empty*/
  if ((str == NULL) || (str[0] =='\n')){
    return NULL;
  }
  
  for ( i = 0; i < 4 ; i ++){
    j = 0;
    param[i] = malloc (sizeof(char)*5);
    while ((*str != 'x') && (*str != '+') &&
	   (*str != '\0'))
      {
	param[i][j] = *str;
	str++;
	//	printf("%s\n",str);
	j++;
      }
    str++;
    param[i][j] = '\0';
    //    printf("%s\n", param[i]);
  }
  
  view->x = strtol(param[0],&pEnd,10);
  view->y = strtol(param[1],&pEnd,10);
  view->width = strtol(param[2],&pEnd,10);
  view->height = strtol(param[3],&pEnd,10);
  view->state = 1;

}

int main(){
  char buffer[BUFFER_SIZE];
  int len_read;
  int type;
  char *str_request;
  char *msg;
  char *token;
  char *pEnd;
  int i;
  char *positionEntree;
  state = 0;

  while (1){
    if  ((len_read = read(STDIN_FILENO, buffer, BUFFER_SIZE)) == -1){
      perror("read");
      return EXIT_FAILURE;
    }
    buffer[len_read-1] = '\0';
    str_request = malloc (sizeof(char)* len_read);
   
    type = parse (buffer);

    printf("type: %d\n",type);
    
    token =  strtok (buffer," ");
    
    switch (type) {
	
    case 0 : //load
      {
	token =  strtok (NULL, " ");
	msg = intern__load(token, aquarium);
      }
      break;

    case 1 : //show
      {
	msg = intern__show();
      }
      break;

    case 2 : //add
      {
	token =  strtok (NULL, " ");
	token =  strtok (NULL, " ");
	int id = get_view_id(token);
	token =  strtok (NULL, " ");
	View* view = parse_view(token);
	view->id= id;
	msg = intern__add(*view);
	free(view);
      }
      break;

    case 3 : //del
      {
	token =  strtok (NULL, " ");
	token =  strtok (NULL, " ");
	token++;
	long ret = strtol(token, &pEnd, 10);
	msg = intern__del (ret);
	break;
      }

    case 4 : //save
      token =  strtok (NULL, " ");
      char* file_name = token;
      msg = intern__save(file_name);
      break;
	
    default: 
      msg = "not recognized\n";
    }

    if (msg != NULL)
    printf ("%s\n", msg);
  }
  
}
