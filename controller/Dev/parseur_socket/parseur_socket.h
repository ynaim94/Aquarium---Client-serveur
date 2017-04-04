/**
 * @file    parseur_socket.h
 * @brief   the parseur_socket header file
 * @author  CHERIF Houssem
 */
#ifndef PARSEUR_SOCKET_H
#define PARSEUR_SOCKET_H

#include <stdio.h>
#include <string.h>
#include <regex.h>
#include <unistd.h>

/*************************
 *Parsing functions
 */
int parser(const char* s);
int parser_hello(char* reply, int index);
int parser_hello_id(const char* s, char* reply, int index);
int parser_get_fish(const char* s, char* reply, int index);
int parser_get_fish_continuously(const char* s, char* reply);
int parser_add_fish(const char* s, char* reply, int index);
int parser_del_fish(const char* s, char* reply);
int parser_start_fish(const char* s, char* reply);
int parser_log_out(char* reply, int index);
int parser_ping(const char* s, char* reply);


#endif
