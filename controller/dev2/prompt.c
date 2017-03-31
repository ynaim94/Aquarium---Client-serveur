#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include "intern.h"
#include "parser.h"


#define BUFFER_SIZE  256

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
	/*token[1]++;
	int a = strtol (
	msg = intern__add(token[1]);*/
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
      msg = intern__save("aquarium_save.info");
      break;
	
    default: 
      msg = "not recognized\n";
    }

    if (msg != NULL)
    printf ("%s\n", msg);
  }
  
}
