/**
 * @file    parseur_socket.c
 * @brief   the parseur_socket code file
 * @author  CHERIF Houssem
 */
#include "parseur_socket.h"
#include "../view.h"
#include "../fish.h"
#include "../client.h"
#include "../serveur.h"
#include <regex.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <time.h>

#define MAX_1 20
#define MAX_ARG 10
#define B_SIZE 1024
/***************************
 *Regular expressions
 */
const char *str_hello_id="^(hello in as[ ]N[0-9]+$){1}";//
const char *str_hello="^(hello$){1}";//
const char *str_add_fish="^(addFish[ ]([[:alnum:]]+)([_][0-9]{1,2})?[ ]at[ ][0-9]{1,2}x[0-9]{1,2},[0-9]{1,2}x[0-9]{1,2},[ ]([[:alnum:]]+)$){1}";
const char *str_del_fish="^(delFish[ ][[:alnum:]]+([_][0-9]{1,2})?$){1}";//
const char *str_log_out="^(log out$){1}";//
const char *str_start_fish="^(startFish[ ][[:alnum:]]+([_][0-9]{1,2})?$){1}";//
const char *str_get_fish="^(getFishes$){1}";//
const char *str_get_fish_continously="^(getFishesContinuously$){1}";//
const char *str_ping="^(ping[ ]+[0-9]{4,5}$)";//

/***************************
 *Functions implementations
 */


extern int nb_views;
extern View views[MAX_VIEW];
extern int nb_fishes;
extern Fish fishes[MAX];
extern int update;
/**
* @function  parser
* @brief     analyse and recognize the command receveid
*
* @param     s : input string to recognize
* @return    an integer refering to the command recognized or an erreur code
*/
int parser(const char *s)
{
  int len = 9,i = 0;
  const char* requests[] = {str_hello_id,str_hello,str_add_fish,str_del_fish,str_log_out,str_start_fish,str_get_fish_continously,str_get_fish,str_ping};
  regex_t preg;
  int err;

  for(i = 0; i<len ; i++)
  {
    err=regcomp(&preg, requests[i], REG_NOSUB | REG_EXTENDED);
    if(!err && !regexec(&preg, s, 0, NULL, 0))
    {
      regfree(&preg);
      return i;
    }
  }
  return len;
}
/**
* @function  parser_hello
* @brief     prepare the replay to hello command
*
* @param     replay : buffer to be filled with the replay message
* @return    an integer refering to the command succeded oder failed
*/
int parser_hello(char* reply,int index)
{
  int i=0;
  if (state == 0 )
  {
    sprintf(reply,"%s","no greeting : No aquarium loaded\n");
    return -1;
  }
  if (clients[index].state!=REJECTED)
  {
    sprintf(reply,"%s","no greeting : Client already attached to a view\n");
    return -1;
  }
  //char* a = malloc(sizeof(char)*13);
  while ((i<nb_views) && (views[i].state == 1))
    i++;
  //printf("l'index choisie : %d \n", i);
  if(i<nb_views)
  {
      clients[index].state = i;
      views[i].state=ATTACHED;
      //printf("le state du view choisie : %d \n",views[i].state);
      sprintf(reply,"%s%d%s","greeting N",views[i].id,"\n");
      //printf("%d \n",clients[index].state);
      return 0;
  }
  else
  {
     sprintf(reply,"%s","no greeting : No view available\n");
     return -1;
  }


}
/**
* @function  parser_hello_id
* @brief     prepare the replay to hello_id command
*
* @param     reply : buffer to be filled with the replay message
* @param     s : the input command containing the command
* @param     index : the sender view's index
* @return    an integer refering to the command succeded oder failed
*/
int parser_hello_id(const char* s, char* reply, int index)
{

  char* req = malloc (sizeof(char)*(strlen(s)+1));
  strcpy(req,s);
  char* tok;
  int last_free=nb_views,i=0;
  int id;
  if (state == 0 )
  {
    sprintf(reply,"%s","no greeting: No aquarium loaded\n");
    return -1;
  }
  if (clients[index].state!= REJECTED)
  {
    sprintf(reply,"%s","no greeting : Client already attached to a view\n");
    return -1;
  }
  tok=strtok(req," ");
  tok=strtok(NULL," ");
  tok=strtok(NULL," ");
  tok=strtok(NULL," ");
  tok++;
  id=atoi(tok);
  while ((i<nb_views) && (views[i].id != id))
  {
   if(views[i].state==FREE)
    last_free=i;
   i++;
  }
  if(((views[i].id == id))&&(views[i].state==FREE))
  {
    clients[index].state = i;
    views[i].state=ATTACHED;
    sprintf(reply,"%s%d%s","greeting N",views[i].id,"\n");
  }
  else if(((views[i].state ==ATTACHED)||(i==nb_views))&&(last_free != nb_views))
  {
    clients[index].state = i;
    views[last_free].state=ATTACHED;
    sprintf(reply,"%s%d%s","greeting N",views[last_free].id,"\n");
  }
  else
  {
    sprintf(reply,"%s","no greeting : No view available\n");
  }
}
/**
* @function  parser_log_out
* @brief     prepare the replay to logout command
*
* @param     reply : buffer to be filled with the replay message
* @return    an integer refering to the command succeded oder failed
*/
int parser_log_out(char* reply, int index)
{
  //views[index].state=FREE;
  sprintf(reply,"%s","bye\n");
}
/**
* @function  parser_ping
* @brief     prepare the replay to ping command
*
* @param     reply : buffer to be filled with the replay message
* @param     s : the input command containing the command
* @return    an integer refering to the command succeded oder failed
*/
int parser_ping(const char* s, char* reply)
{
  char* req = malloc (sizeof(char)*(strlen(s)+1));
  strcpy(req,s);
  char* tok;
  int id;
  tok=strtok(req," ");
  tok=strtok(NULL," ");
  id=atoi(tok);
  sprintf(reply,"%s%d%s","pong ",id,"\n");
}
/**
* @function  parser_add_fish
* @brief     adding fish to array and prepare the replay to add fish command
*
* @param     reply : buffer to be filled with the replay message
* @param     s : the input command containing the command
* @param     index : the sender view's index
* @return    an integer refering to the command succeded oder failed
*/
int parser_add_fish(const char* s, char* reply, int index)
{
  char* req = malloc (sizeof(char)*(strlen(s)+1));
  strcpy(req,s);
  char **argv = NULL;
  char *p = NULL;
  int i = 0,j=0,k=0, height=0,width=0,cpt=0;
  double x=0, y=0;
  if (state == 0 )
  {
    sprintf(reply,"%s","NOK: No aquarium loaded\n");
    return -1;
  }
  argv = malloc(sizeof(char *) * MAX_ARG);
  for(k=0;k<MAX_ARG;k++)
  {
   argv[k]=NULL;
  }
  p = strtok(req, " ,x");
  while(p != NULL)
   {
      if(i < MAX_ARG)
      {
         argv[i] = malloc(sizeof(char) * (1+strlen(p)));
         strcpy(argv[i], p);
         i++;
      }
      else
         break;
      p = strtok(NULL, " ,x");
   }
  Fish fish_tmp;
  while((j<nb_fishes)&&(strcmp(argv[1],fishes[j].name) != 0))
   j++;
  if (j<nb_fishes)
    sprintf(reply,"%s","NOK : Fish already added to the Fish tank\n");
  else if(strcmp(argv[7],"RandomWayPoint") != 0)
    sprintf(reply,"%s","NOK : Mobility modele not supported\n");
  else
  {
   strcpy(fish_tmp.name,argv[1]);
   x=atoi(argv[3])/100.0;
   y=atoi(argv[4])/100.0;
   height=atoi(argv[5]);
   width=atoi(argv[6]);
   if (x>100)
    fish_tmp.actualPosition[0]=views[index].x;
   else
    fish_tmp.actualPosition[0]=views[index].x+(x*views[index].width);
   if (y>100)
     fish_tmp.actualPosition[1]=views[index].y;
   else
   fish_tmp.actualPosition[1]=views[index].y+(y*views[index].height);
   fish_tmp.destination[0]=views[index].x+(x*views[index].width);
   fish_tmp.destination[1]=views[index].y+(y*views[index].height);
   fish_tmp.dimension[0]=height;
   fish_tmp.dimension[1]=width;
   fish_tmp.state= STOPED;
   fish_tmp.mobility=RandomPathWay;
   //printf("la position du poisson ajouté est %d,%d dans la vue %d,%d")
   fishes[nb_fishes]=fish_tmp;
   nb_fishes++;
   for(i = 0; argv[i] != NULL; i++)
   {
      free(argv[i]);
   }
   free(argv);
   free(req);
   sprintf(reply,"%s","OK : Fish added to fish tank\n");
  }
  /*for(cpt=0;cpt<nb_fishes;cpt++)
  {
    printf("le nom du poisson d'index %d est : %s \nla position du poisson est : %d,%d\nson state est : %d\n",cpt,fishes[cpt].name,fishes[cpt].actualPosition[0],fishes[cpt].actualPosition[1],fishes[cpt].state);
  }*/
}

/**
* @function  parser_del_fish
* @brief     deleting a fish from the fishes array and prepare the replay to del fish command
*
* @param     reply : buffer to be filled with the replay message
* @param     s : the input command containing the command
* @return    an integer refering to the command succeded oder failed
*/
int parser_del_fish(const char* s, char* reply)
{
  char* tok=NULL;
  int j=0, i=0;
  if (state == 0 )
  {
    sprintf(reply,"%s","NOK: No aquarium loaded\n");
    return -1;
  }
  char* req = malloc (sizeof(char)*(strlen(s)+1));
  strcpy(req,s);
  tok=strtok(req," ");
  tok=strtok(NULL," ");
  while((j<nb_fishes)&&(strcmp(tok,fishes[j].name) != 0))
   j++;
  if (j==nb_fishes)
   sprintf(reply,"%s","NOK : Poisson inexistant\n");
  else
  {
    for(i=j;i<nb_fishes-1;i++)
    {
      fishes[i]=fishes[i+1];
    }
    nb_fishes--;
    sprintf(reply,"%s","OK : Fish deleted from fish tank\n");

  }

}
/**
* @function  parser_start_fish
* @brief     starting a fish from the fishes array and prepare the replay to startFish command
*
* @param     reply : buffer to be filled with the replay message
* @param     s : the input command containing the command
* @return    an integer refering to the command succeded oder failed
*/
int parser_start_fish(const char* s, char* reply)
{
  char* tok=NULL;
  int j=0, i=0;
  if (state == 0 )
  {
    sprintf(reply,"%s","NOK : No aquarium loaded\n");
    return -1;
  }
  char* req = malloc (sizeof(char)*(strlen(s)+1));
  strcpy(req,s);
  tok=strtok(req," ");
  tok=strtok(NULL," ");
  while((j<nb_fishes)&&(strcmp(tok,fishes[j].name) != 0))
   j++;
  if (j==nb_fishes)
   sprintf(reply,"%s","NOK : Poisson inexistant\n");
  else
  {
    fishes[j].state=STARTED;
    sprintf(reply,"%s","OK : Fish started\n");
    //printf("le nom du poisson started est : %s \nson state est : %d",fishes[j].name,fishes[j].state);
  }
}
/**
* @function  parser_get_fish
* @brief     prepare the replay to getFish command
*
* @param     reply : buffer to be filled with the replay message
* @param     s : the input command containing the command
* @return    an integer refering to the command succeded oder failed
*/
int parser_get_fish(const char* s, char* reply, int index)
{
  int i=0,x=0,y=0,cpt=0,time=5;
  if (state == 0 )
  {
    sprintf(reply,"%s","NOK: No aquarium loaded\n");
    return -1;
  }
  new_position();
  sprintf(reply,"%s","list");
  for(i=0;i<nb_fishes;i++)
  {

    if((fishes[i].destination[0]>views[index].x)&&(fishes[i].destination[0]<views[index].x+views[index].width)&&(fishes[i].destination[1]>views[index].y)&&(fishes[i].destination[1]<views[index].y+views[index].height)&&(fishes[i].state==STARTED))
    {
      if((fishes[i].actualPosition[0]<views[index].x)||(fishes[i].actualPosition[0]>views[index].x+views[index].width)||(fishes[i].actualPosition[1]<views[index].y)||(fishes[i].actualPosition[1]>views[index].y+views[index].height))
      {
        time=0;
      }
      else
      {
        time=5;
      }
      cpt++;
      x=(fishes[i].destination[0]-views[index].x)*100/views[index].width;//views[index].width*100;
      //printf("%d\n",x);
      y=(fishes[i].destination[1]-views[index].y)*100/views[index].height;//views[index].width*100;
      //printf("%d\n",y);
      fishes[i].actualPosition[0]=fishes[i].destination[0];
      fishes[i].actualPosition[1]=fishes[i].destination[1];
      sprintf(reply,"%s%s%s%s%d%s%d%s%d%s%d%s%d%s",reply," [",fishes[i].name," at ",x,"x",y,",",fishes[i].dimension[0],"x",fishes[i].dimension[1],",",time,"]");
    }
  }
  if(cpt==0)
  {
    memset(reply, 0, sizeof (reply));
    sprintf(reply,"%s","NOK: No fishes found\n");
  }
  else
    sprintf(reply,"%s%s",reply,"\n");
  /*for (i=0;i<nb_fishes;i++)
  {
    printf("le poisson %s se trouve à la position %d, %d \n",fishes[i].name,fishes[i].actualPosition[0],fishes[i].actualPosition[1]);
  }*/
}
int parser_get_fish_continuously(const char* s,int index)
{
  char buffer[B_SIZE];
  clients[index].en_continue=TRUE;
  while(clients[index].en_continue)
  {
    parser_get_fish(s,buffer,clients[index].state);
    send(clients[index].sock, buffer, strlen(buffer),0);
    memset(buffer, 0, sizeof (buffer));
    sleep(update);
  }
}
int new_position()
{
    int i=0,j=0,k=1;
    for(j=0;j<nb_fishes;j++)
    {
      if (fishes[j].state==STARTED)
      {
        for(i=0;i<DIM;i++)
        {
          k=random_plus_minus();
          fishes[j].destination[i]=(k*(rand()%40))+fishes[j].actualPosition[i];
        }
      }
    }
}
int random_plus_minus()
{
  int i;
  i=rand();
  if(i%2==0)
    return 1;
  else
    return -1;
}
/*int main()
{
  char buffer[1000];
  int a,i;
  const char* test="log out";
  nb_views = 1;
  viewss[0].id=100;
  viewss[0].state=ATTACHED;
  viewss[0].x=300;
  viewss[0].y=100;
  viewss[0].height=200;
  viewss[0].width=200;
  a = parser("addFish PoissonNain at 20x30,10x40, RandomPathWay");
  printf("%d\n",a);
  parser_add_fish("addFish PoissonRouge at 40x30,10x40, RandomPathWay",buffer,0);
  printf("%s",buffer);
  printf("%d \n",nb_fishes);
}*/
