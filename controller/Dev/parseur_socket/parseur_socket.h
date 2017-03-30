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
int parser_hello(const char* s);
int parser_hello_id(const char* s);
int parser_get_fish(const char* s);
int parser_get_fish_continuously(const char* s);
int parser_add_fish(const char* s);
int parser_del_fish(const char* s);
int parser_start_fish(const char* s);
int parser_log_out(const char* s);
int parser_ping(const char* s);


#endif
