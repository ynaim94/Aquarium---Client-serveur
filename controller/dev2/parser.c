#include <stdio.h>
#include <stdlib.h>
#include "parser.h"


char *str_regex[]= {
  "load[ ]*[:alnum:]*", //load
  "show[ ]*[:alnum:]*", //show
  "add[ ]*view[ ]*[:alnum:]*", //add
  "del[ ]*view[ ][:alnum:]*", //del
  "save[:alnum:]*"  //save
};   

int parse(const char *str_request){
  
  int err;
  regex_t preg;
  int match = REG_NOMATCH;
  int i = -1;
  while ((match == REG_NOMATCH) && (i < MAX)){
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

  /*
  if ( match == REG_NOMATCH){
      return 0;
  }
  else{
    switch (i) {
	
    case 0 : //load
      parse_load(str_request);
      break;

    case 1 : //show
      parse_show(str_request);
      break;

    case 2 : //add
      parse_add(str_request);
      break;

    case 3 : //del
      parse_del(str_request);
      break;

    case 4 : //save
      parse_save(str_request);
      break;
	
    default: 
      printf("not recognized");
      return 0;
    }
    }*/
    


//int parse_load(const char*);
//int parse_show(const char*);
//int parse_add(const char*);
//int parse_del(const char*);
//int parse_save(const char*);


