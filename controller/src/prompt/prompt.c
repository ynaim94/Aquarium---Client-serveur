#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include "intern.h"
#include "parser.h"
#include "../view.h"
#include "prompt.h"
#include "../log/log.h"
#define _GNU_SOURCE
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
	j++;
      }
    str++;
    param[i][j] = '\0';
  }

  view->x = strtol(param[0],&pEnd,10);
  view->y = strtol(param[1],&pEnd,10);
  view->width = strtol(param[2],&pEnd,10);
  view->height = strtol(param[3],&pEnd,10);
  view->state = 1;

}

int display_prompt(int x){
  char buffer[BUFFER_SIZE];
  char logg[BUFFER_SIZE];
  //int len_read;
  int type=0;
  char *msg;
  char *token;
  char *pEnd;
  int i=0;
  char *positionEntree;

    strcpy(buffer,buffer_prompt);
    //str_request = malloc (sizeof(char)* x);
    sprintf(logg,"%s%s%s","request from prompt : ",buffer,"\n");
    insert_log(logg);
    memset(logg, 0, sizeof (logg));
    type = parse (buffer);

    if ((type != 0) && (type != 5) && (state == 0))
      asprintf(&msg,"->Aquarium Not loaded");
    else {


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
      asprintf(&msg,"->not recognized");
      }
    }
    if (msg != NULL)
      printf ("%s\n\n", msg);
      supprime_retour(msg);
      sprintf(logg,"%s%s%s","reponse to prompt : ",msg,"\n");
      insert_log(logg);
      memset(logg, 0, sizeof (logg));

  free(msg);


}
