#ifndef CLIENT_H
#define CLIENT_H
#define BUFFER_SIZE 1024

typedef struct
{
   int sock;
   char name[BUFFER_SIZE];
}Client;

#endif
