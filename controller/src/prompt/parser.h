#ifndef PARSER_H
#define PARSER_H

#include <regex.h>

#define MAX 5 // Max d'opération possibles
#define UNKNOWN "Error: Not recognized"



 
int parse (const char* str_request);
/*int parse_load(const char* str_request);
int parse_show(const char* str_request);
int parse_add(const char* str_request);
int parse_del(const char* str_request);
int parse_save(const char* str_request);*/


#endif //PARSER_H
