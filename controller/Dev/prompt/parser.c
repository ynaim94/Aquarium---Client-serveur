#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "parser.h"


char *str_regex[]= {
  "load[ ]+[0-9A-Za-z]+", //load
  "show[ ]+aquarium", //show
  "add[ ]+view[ ]+N[0-9]+[ ]+[0-9]+x[0-9]+\\+[0-9]+\\+[0-9]+", //add
  "del[ ]+view[ ]+N[0-9]+", //del
  "save[0-9A-Za-z]*"  //save
};   

int parse(const char *str_request){  
  int err;
  regex_t preg;
  int match = REG_NOMATCH;
  int i = -1;
  
  while ((match == REG_NOMATCH) && (i < MAX-1)){
    i++;
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

 

