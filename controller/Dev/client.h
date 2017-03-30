/**
 * @file    client.h
 * @brief   the client header file
 * @author  CHERIF Houssem
 */
#ifndef CLIENT_H
#define CLIENT_H
#define BUFFER_SIZE 1024
#define IP_SIZE 20
#define ACCEPTED 1
#define REJECTED 0

/**
 *  @struct Client
 *  @brief the client struct
 *
 *  @var sock : the client's socket file descriptor
 *  @var name : buffer containing the client's name
 *  @var ip : buffer containing the client's ip adresse
 *  @var state : is the client accepted after hello or not
 */
typedef struct
{
   int sock;
   char name[BUFFER_SIZE];
   char ip[IP_SIZE];
   int state;
}Client;

#endif
