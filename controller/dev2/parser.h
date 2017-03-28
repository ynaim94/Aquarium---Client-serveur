#ifndef PARSER_H
#define PARSER_H

#include <regex.h>

#define MAX 5// Max d'opération possibles

char *str_regex[]= {
  "load[:space:][:alnum:]*", //load
  "show[:space:][:alnum:]*", //show
  "add[:space:]view[:space:][:alnum:]*", //add
  "del[:space:]view[:space:][:alnum:]*", //del
  "save[:alnum:]*"  //save
};
  
int parse (const char*);
int parse_load(const char*);
int parse_show(const char*);
int parse_add(const char*);
int parse_del(const char*);
int parse_save(const char*);


#endif //PARSER_H
