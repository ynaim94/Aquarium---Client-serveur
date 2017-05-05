/**
 * @file    config.h
 * @brief   configration system header file
 * @author  CHERIF Houssem
 */
#ifndef CONFIG_H
#define CONFIG_H
#include <stdlib.h>
#include <stdio.h>

int open_config(const char* path);
long int getPortnumber();
unsigned int getTimeout();
unsigned int getUpdateInterval();
void close_config();

#endif
