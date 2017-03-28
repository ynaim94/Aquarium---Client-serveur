#ifndef CONFIG_H
#define CONFIG_H
#include <stdlib.h>
#include <stdio.h>
 void open_config(const char* path);
 long int getPortnumber();
 unsigned int getTimeout();
 unsigned int getUpdateInterval();
 void close_config();

#endif
