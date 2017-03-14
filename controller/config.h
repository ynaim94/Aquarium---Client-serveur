#ifndef CONFIG_H
#define CONFIG_H
#include <stdlib.h>
#include <stdio.h>
 FILE* open();
 long int getPortnumber(FILE* fd);
 unsigned int getTimeout(FILE* fd);
 unsigned int getUpdateInterval(FILE* fd);

#endif
