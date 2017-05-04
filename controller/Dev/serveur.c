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
#include <pthread.h>
#include <time.h>


#include "serveur.h"
#include "client.h"
#include "config/config.h"
#include "log/log.h"
#include "pool/thpool.h"
#include "prompt/prompt.h"
#include "parseur_socket/parseur_socket.h"
#include "view.h"

/**
 * @function  app
 * @brief     the server main fonction starting connexion and monitoring file descriptor
 *
 * @param     none
 * @return    none
 */
extern Client clients[MAX_CLIENTS];
extern int state;
extern int update;
char buffer_msg[BUF_SIZE];
int actual = 0;
static void app(void)
{
   srand(time(NULL));
   open_log("./log/log.txt");

   insert_log("Starting the controller");
   printf("********Welcome to the aquarium's controller********\n");
   printf("-> Please load an aquarium\n\n");
   threadpool thpool = thpool_init(4);
   insert_log("Creating threads pool");
   SOCKET sock = init_connection();
   if (sock==-1)
    return;


   /* the index for the array */
   int max = sock;
   int len =0;
   int i=0;
   int len_read;
   char buffer[BUF_SIZE];
   /* an array for all clients */


   fd_set rdfs;
   //thpool_add_work(thpool, (void*)check_timeout,(void*)&actual);

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
        if  ((len_read = read(STDIN_FILENO, buffer_prompt, BUFFER_SIZE)) == -1){
            perror("read");
          }
        if (len_read ==0)
        {
          exit(0);
        }
        buffer_prompt[len_read-1] = '\0';
        thpool_add_work(thpool, (void*)display_prompt, (void*)len_read);
        /* stop process when type on keyboard */
      }
      else if(FD_ISSET(sock, &rdfs))
      {
         /* new client */
         SOCKADDR_IN csin = { 0 };
         size_t sinsize = sizeof csin;
         int csock = accept(sock, (SOCKADDR *)&csin,(socklen_t*)&sinsize);

         if(csock == SOCKET_ERROR)
         {
            perror("accept()");
            continue;
         }
         else
            printf("new client accepté\n");


         /* what is the new maximum fd ? */
         max = csock > max ? csock : max;

         FD_SET(csock, &rdfs);

         Client c = { csock };
         sprintf(c.ip,"%s",inet_ntoa(csin.sin_addr));
         c.state = REJECTED;
         c.en_continue = FALSE;
         update_freshness(&c);


         clients[actual] = c;
         printf("adresse ip : %s\n",clients[actual].ip);
         printf("time : %d \n", clients[actual].last_update.tv_sec);
         actual++;
      }
      else
      {
         //int i = 0;
         for(i = 0; i < actual; i++)
         {
            /* a client is talking */
            if(FD_ISSET(clients[i].sock, &rdfs))
            {
               Client client = clients[i];

               int c = read_client(clients[i].sock, buffer_msg);
               /* client disconnected */
               if(c == 0)
               {
                  char buffer1[BUF_SIZE];
                  closesocket(clients[i].sock);
                  views[clients[i].state].state= FREE;
                  remove_client(clients, i, &actual);
                  strcpy(buffer,client.ip);
                  sprintf(buffer1, "%s%s",buffer," is disconnected !");
                  printf("%s\n", buffer1 );
               }
               else
               {
                  update_freshness(&clients[i]);
                  //printf("a client is talking : %s [fin]\n", buffer_msg);
                  thpool_add_work(thpool,(void*)parse_socket,(void*)i);
                  //parse_socket(i);

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
   update= getUpdateInterval();

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
    buffer[n-1] = '\0';
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
 * @function  update_freshness
 * @brief     update the client last communication with the server time
 *
 * @param     client: the client to update;
 * @return    none
 */
void update_freshness(Client* client)
{
  gettimeofday(&(client->last_update),0);
  //printf("time : %d", client.last_update.tv_sec);
}
/**
 * @function  check_timeout
 * @brief     check wether the client reached the timeout and remove it if so;
 *
 * @param     client: the client to update;
 * @return    none
 */
int check_timeout(int* nb_client)
{
  struct timeval current_time;
  int i=0,c=0;
  int timeout=0;
  int e=0;
  int a=0;
  time_t minimum=0;
  struct timeval current;

  e=open_config("./config/controller.cfg");
  if(e==0)
  {
    insert_log("configuration failed...aborting the controller");
    return -1;
  }

  timeout = getTimeout();
  //rintf("le timeout est : %d \n", timeout );

  close_config();

  while(1)
  {
    gettimeofday(&current_time,0);

    for(i=0;i<*nb_client;i++)
    {
      a = current_time.tv_sec-(clients[i].last_update.tv_sec+timeout);
      if(a>0)
      {
        Client client = clients[i];
        char buffer1[BUF_SIZE];
        shutdown(clients[i].sock,2);
        remove_client(clients, i, nb_client);
        strcpy(buffer1,client.ip);
        sprintf(buffer1, "%s%s",buffer1," is ejected due to timeout !");
        printf("%s\n", buffer1 );
      }
    }
    minimum = clients[i].last_update.tv_sec;
    for(i=0;i<*nb_client;i++)
    {
        if ( clients[i].last_update.tv_sec < minimum )
        {
           minimum = clients[i].last_update.tv_sec;
        }
    }
    gettimeofday(&current,0);
    sleep(current.tv_sec-minimum);

  }
}

int parse_socket(int index)
{
  char buffer[BUF_SIZE];
  char reply[BUF_SIZE];
  int to_parse;
  strcpy(buffer,buffer_msg);
  {
    to_parse=parser(buffer);
    //printf("expression reconnu : %d \n", to_parse);
    switch(to_parse)
    {
        case 0:
        {
          parser_hello_id(buffer, reply,index);
        }
        break;
        case 1:
        {
          parser_hello(reply,index);
        }
        break;
        case 2:
        {
          clients[index].en_continue=FALSE;
          parser_add_fish(buffer,reply, index);
        }
        break;
        case 3:
        {
          clients[index].en_continue=FALSE;
          parser_del_fish(buffer,reply);
        }
        break;
        case 4:
        {
          clients[index].en_continue=FALSE;
          parser_log_out(reply,index);
          write_client(clients[index].sock, reply);
          views[clients[index].state].state= FREE;
          shutdown(clients[index].sock,2);
          remove_client(clients, index, &actual);
          return 0;
        }
        break;
        case 5:
        {
          clients[index].en_continue=FALSE;
          parser_start_fish(buffer,reply);
        }
        break;
        case 6:
        {
          clients[index].en_continue=FALSE;
          parser_get_fish_continuously(buffer,index);
          return 0;
        }
        break;
        case 7:
        {
          clients[index].en_continue=FALSE;
          parser_get_fish(buffer,reply,clients[index].state);
        }
        break;
        case 8:
        {
          parser_ping(buffer,reply);
        }
        break;
        case 9:
        sprintf(reply,"%s","unkown command try again\n");
        break;
    }

  }
  //printf("%s\n", reply);
  write_client(clients[index].sock, reply);
  memset(reply, 0, sizeof (reply));
  return 0;

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
