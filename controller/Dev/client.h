/**
 * @file    client.h
 * @brief   the client header file
 * @author  CHERIF Houssem
 */
#ifndef CLIENT_H
#define CLIENT_H
#define BUFFER_SIZE 1024
#define MAX_CLIENTS 	100
#define IP_SIZE 20
#define REJECTED -1
#define TRUE 1
#define FALSE 0
#include <sys/time.h>


/**
 *  @struct Client
 *  @brief the client struct
 *
 *  @var sock : the client's socket file descriptor
 *  @var name : buffer containing the client's name
 *  @var ip : buffer containing the client's ip adresse
 *  @var state : is the client accepted after hello or not
 *  @var last_update : the last update time;
 */
typedef struct
{
   int sock;
   char name[BUFFER_SIZE];
   char ip[IP_SIZE];
   int en_continue;
   int state;
   struct timeval last_update;
}Client;
Client clients[MAX_CLIENTS];
#endif
