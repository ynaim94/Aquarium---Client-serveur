/**
 * @file    log.h
 * @brief   log system header file
 * @author  CHERIF Houssem
 */
#ifndef LOG_H
#define LOG_H

#include<stdio.h>
#include<unistd.h>
#include<string.h>
#include<fcntl.h>
#include<time.h>

#define BUFF_SIZE 25

void open_log(const char* path);
void insert_log(const char* msg);
void close_log();


#endif
