#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "parser.h"


char *str_regex[]= {
  "load[ ]+[:alnum:]+", //load
  "show[ ]+aquarium", //show
  "add[ ]+view[ ]+N[0-9]+[ ]+[0-9]+x[0-9]+\\+[0-9]+\\+[0-9]+", //add
  "del[ ]+view[ ]+N[0-9]+", //del
  "save[:alnum:]*"  //save
};   

int parse(const char *str_request){  
  int err;
  regex_t preg;
  int match = REG_NOMATCH;
  int i = -1;
  
  //printf ("str_request: %s, len :%d\n", str_request, strlen (str_request));
  
  while ((match == REG_NOMATCH) && (i < MAX-1)){
    i++;
    //    printf ("str_regex[i]: %s, len :%d\n", str_regex[i], strlen (str_regex[i]));
    err = regcomp(&preg, str_regex[i], REG_NOSUB | REG_EXTENDED);
    if (err == 0){
      match = regexec (&preg, str_request, 0, NULL, 0);
      regfree (&preg);
    }
  }
  if ( match == REG_NOMATCH){
   return MAX;
  }
  else{
    return i;
  }
}

 

