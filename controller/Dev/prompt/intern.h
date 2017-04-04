#ifndef INTERN_H
#define INTER_H

#include "view.h"

char* intern__load(const char* file_name,  int* aquarium);
char* intern__show();
char* intern__add(View view);
char* intern__del(int id);
char* intern__save(char* file_name);




#endif //INTER_H
