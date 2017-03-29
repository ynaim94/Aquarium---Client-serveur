#ifndef CLIENT_H
#define CLIENT_H
#define BUFFER_SIZE 1024
#define IP_SIZE 20
typedef struct
{
   int sock;
   char name[BUFFER_SIZE];
   char ip[IP_SIZE];
}Client;

#endif
