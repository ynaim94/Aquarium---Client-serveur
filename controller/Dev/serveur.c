#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <unistd.h>

#include "serveur.h"
#include "client.h"
#include "config/config.h"
#include "log/log.h"
static void app(void)
{
   open_log("./log/log.txt");
   insert_log("démarrage du serveur");
   SOCKET sock = init_connection();
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
         //strcpy(c.ip);
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

static void clear_clients(Client *clients, int actual)
{
   int i = 0;
   for(i = 0; i < actual; i++)
   {
      closesocket(clients[i].sock);
   }
}

static void remove_client(Client *clients, int to_remove, int *actual)
{
   /* we remove the client in the array */
   memmove(clients + to_remove, clients + to_remove + 1, (*actual - to_remove - 1) * sizeof(Client));
   /* number client - 1 */
   (*actual)--;
}


static int init_connection(void)
{
   int portno;
   open_config("./config/controller.cfg");
   portno= getPortnumber();
   if (portno<0)
   {
     perror("getPortnumber()");
     exit(errno);

   }
   close_config();
   insert_log("configuration du serveur avec succès ");
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

static void end_connection(int sock)
{
   closesocket(sock);
}

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

static void write_client(SOCKET sock, const char *buffer)
{
   if(send(sock, buffer, strlen(buffer), 0) < 0)
   {
      perror("send()");
      exit(errno);
   }
}

int main(int argc, char **argv)
{

   app();


   return EXIT_SUCCESS;
}
