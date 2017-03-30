/**
 * @file    serveur.c
 * @brief   The controller code File
 * @author  CHERIF Houssem
 */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <unistd.h>

#include "serveur.h"
#include "client.h"
#include "config/config.h"
#include "log/log.h"

/**
 * @function  app
 * @brief     the server main fonction starting connexion and monitoring file descriptor
 *
 * @param     none
 * @return    none
 */
static void app(void)
{
   open_log("./log/log.txt");

   insert_log("");

   SOCKET sock = init_connection();
   if (sock==-1)
    return;

   char buffer[BUF_SIZE];
   /* the index for the array */
   int actual = 0;
   int max = sock;
   int len =0;
   /* an array for all clients */
   Client clients[MAX_CLIENTS];

   fd_set rdfs;

   while(1)
   {
      int i = 0;
      FD_ZERO(&rdfs);

      /* add STDIN_FILENO */
      FD_SET(STDIN_FILENO, &rdfs);

      /* add the connection socket */
      FD_SET(sock, &rdfs);

      /* add socket of each client */
      for(i = 0; i < actual; i++)
      {
         FD_SET(clients[i].sock, &rdfs);
      }

      if(select(max + 1, &rdfs, NULL, NULL, NULL) == -1)
      {
         perror("select()");
         exit(errno);
      }

      /* something from standard input : i.e keyboard */
      if(FD_ISSET(STDIN_FILENO, &rdfs))
      {
          fgets(buffer, sizeof(buffer), stdin);
          len = strlen(buffer) - 1;
          if (buffer[len] == '\n')
              buffer[len] = '\0';
          printf("'%s' was read from stdin.\n", buffer);
        /* stop process when type on keyboard */

      }
      else if(FD_ISSET(sock, &rdfs))
      {
         /* new client */
         SOCKADDR_IN csin = { 0 };
         size_t sinsize = sizeof csin;
         int csock = accept(sock, (SOCKADDR *)&csin, &sinsize);

         if(csock == SOCKET_ERROR)
         {
            perror("accept()");
            continue;
         }
         else
            printf("new client accepté\n");

         /* after connecting the client sends its name */
         if(read_client(csock, buffer) == -1)
         {
            /* disconnected */
            continue;
         }

         /* what is the new maximum fd ? */
         max = csock > max ? csock : max;

         FD_SET(csock, &rdfs);

         Client c = { csock };
         strncpy(c.name, buffer, BUF_SIZE - 1);
         printf("Client name : %s\n", c.name);
         char buffer2[BUF_SIZE];
         sprintf(c.ip,"%s",inet_ntoa(csin.sin_addr));
         printf("adresse ip : %s\n",c.ip);
         clients[actual] = c;
         actual++;
      }
      else
      {
         int i = 0;
         for(i = 0; i < actual; i++)
         {
            /* a client is talking */
            if(FD_ISSET(clients[i].sock, &rdfs))
            {
               Client client = clients[i];
               int c = read_client(clients[i].sock, buffer);
               /* client disconnected */
               if(c == 0)
               {
                  char buffer1[BUF_SIZE];
                  closesocket(clients[i].sock);
                  remove_client(clients, i, &actual);
                  strcpy(buffer,client.name);
                  sprintf(buffer1, "%s%s",buffer," is disconnected !");
                  printf("%s\n", buffer1 );
               }
               else
               {

                  printf("a client is talking \n" );
                  printf("%s\n", buffer );
                  if (strcmp(buffer,"hello\0")==0)
                  {
                    write_client(clients[i].sock,"greeting" );
                  }
               }
               break;
            }
         }
      }
   }
   close_log();

   clear_clients(clients, actual);

   end_connection(sock);
}
/**
 * @function  clear_clients
 * @brief     remove all the clients from the array
 *
 * @param     clients : clients array
 * @param     actual : the clients total number
 * @return    none
 */
static void clear_clients(Client *clients, int actual)
{
   int i = 0;
   for(i = 0; i < actual; i++)
   {
      closesocket(clients[i].sock);
   }
}
/**
 * @function  remove_client
 * @brief     remove a client from clients array
 *
 * @param     clients : clients array
 * @param     to_remove : the index of the client to be removed
 * @param     actaul : a pointer to the clients total number
 * @return    none
 */
static void remove_client(Client *clients, int to_remove, int *actual)
{
   /* we remove the client in the array */
   memmove(clients + to_remove, clients + to_remove + 1, (*actual - to_remove - 1) * sizeof(Client));
   /* number client - 1 */
   (*actual)--;
}
/**
 * @function  init_connection
 * @brief     create a socket with parameters from configuration file
 *
 * @param     none
 * @return    return the socket file descriptor
 */
static int init_connection()
{
   int portno;
   int e;

   e=open_config("./config/controller.cfg");
   if(e==0)
   {
     insert_log("configuration failed...aborting the controller");
     return -1;
   }

   portno= getPortnumber();

   if (portno<0)
   {
      perror("getPortnumber()");
      exit(errno);
   }

   close_config();

   insert_log("configuration success");

   SOCKET sock = socket(AF_INET, SOCK_STREAM, 0);
   SOCKADDR_IN sin = { 0 };

   if(sock == INVALID_SOCKET)
   {
      perror("socket()");
      exit(errno);
   }

   insert_log("création de socket avec succès");

   sin.sin_addr.s_addr = htonl(INADDR_ANY);
   sin.sin_port = htons(portno);
   sin.sin_family = AF_INET;

   if(bind(sock,(SOCKADDR *) &sin, sizeof sin) == SOCKET_ERROR)
   {
      perror("bind()");
      exit(errno);
   }

   if(listen(sock, MAX_CLIENTS) == SOCKET_ERROR)
   {
      perror("listen()");
      exit(errno);
   }

   return sock;
}
/**
 * @function  end_connection
 * @brief     close the socket file descriptor
 *
 * @param     sock : the clients socket file descriptor
 * @return    none
 */

static void end_connection(int sock)
{

   closesocket(sock);

}
/**
 * @function  read_client
 * @brief     read a message  from the client socket file descriptor
 *
 * @param     sock : the clients socket file descriptor
 * @param     buffer : buffer containing the message read
 * @return    none
 */
static int read_client(SOCKET sock, char *buffer)
{
   int n = 0;

   if((n = recv(sock, buffer, BUF_SIZE - 1, 0)) < 0)
   {
      perror("recv()");
      /* if recv error we disonnect the client */
      n = 0;
   }
   else if (n>0)
    buffer[n-2] = '\0';
   return n;
}
/**
 * @function  write_client
 * @brief     send a message to a client
 *
 * @param     sock : the clients socket file descriptor
 * @param     buffer : buffer containing the message to send
 * @return    none
 */
static void write_client(SOCKET sock, const char *buffer)
{
   if(send(sock, buffer, strlen(buffer), 0) < 0)
   {
      perror("send()");
      exit(errno);
   }
}

/**
 * @function  main
 * @brief     launch the app function
 *
 * @param     argc : the nombre of arguments
 * @param     argv : array of pointers to arguments
 * @return    EXIT_SUCCESS if everything went right
 */

int main(int argc, char **argv)
{
   app();

   return EXIT_SUCCESS;
}
