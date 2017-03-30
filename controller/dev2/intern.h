#ifndef INTERN_H
#define INTER_H

#include "view.h"

char* intern__load(const char* file_name, int state, int* aquarium);
char* intern_show();
char* intern_add(View view);
char* intern_del(const char* aquarium);
char* intern_save(const char* aquarium);




#endif //INTER_H
