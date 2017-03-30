/**
 * @file    config.c
 * @brief   configration system code file
 * @author  CHERIF Houssem
 */

#include <stdlib.h>
#include <stdio.h>
#include"unistd.h"
#include"config.h"


#define TAILLE_MAX 1000
FILE* configfile = NULL;

/**
 * @function  open_config
 * @brief     opening a the configuration file in reading mode
 *
 * @param     path : string containing the path to configuration file
 * @return    none
 */
int open_config(const char* path)
{
    configfile = fopen(path, "r");
    if (configfile != NULL)
    {
       return 1;
    }
    else
    {
       return 0;
    }
}
/**
 * @function  getPortnumber
 * @brief     get the port number to bind to from the configuration file
 *
 * @param     none
 * @return    the port number
 */
long int getPortnumber(){
  char chaine[TAILLE_MAX+1] = "";
  char * pEnd;
  if (!configfile)
    return -1;
  rewind(configfile);
  char caractereActuel ;
  fgets(chaine, TAILLE_MAX,configfile);
  while (caractereActuel != '=')
   caractereActuel = fgetc(configfile);
  fgets(chaine, TAILLE_MAX, configfile);
  long ret = strtol(chaine,&pEnd,10);
  if (ret < 65535)
    return ret;
  else
    return -1;
}
/**
 * @function  getTimeout
 * @brief     get the Timeout value from the configuration file
 *
 * @param     none
 * @return    the timeout value
 */
unsigned int getTimeout(){
  int i = 0;
  char chaine[TAILLE_MAX+1] = "";
  char * pEnd;
  if (!configfile)
    return -1;
  rewind(configfile);
  char caractereActuel ;
  for (i=0; i<3; i++)
    fgets(chaine, TAILLE_MAX, configfile);
  while (caractereActuel != '=')
    caractereActuel = fgetc(configfile);
  fgets(chaine, TAILLE_MAX, configfile);
  long ret = strtol(chaine,&pEnd,10);
  return ret;
}
/**
 * @function  getUpdateInterval
 * @brief     get the update interval from the configuration file
 *
 * @param     none
 * @return    the update interval value
 */
unsigned int getUpdateInterval(){
  int i = 0;
  char chaine[TAILLE_MAX+1] = "";
  char * pEnd;
  if (!configfile)
    return -1;
  rewind(configfile);
  char caractereActuel ;
  for (i=0; i<5; i++)
   fgets(chaine, TAILLE_MAX, configfile);
  while (caractereActuel != '=')
   caractereActuel = fgetc(configfile);
  fgets(chaine, TAILLE_MAX, configfile);
  long ret = strtol(chaine,&pEnd,10);
  return ret;
}
/**
 * @function  close_config
 * @brief     close the file descriptor to the configuration file
 *
 * @param     none
 * @return    none
 */
void close_config()
{
  if(configfile) fclose(configfile);
  configfile=NULL;
}
/*int main(int argc, char *argv[]){
 FILE* f = open();
 printf("%d \n",getPortnumber(f));
 printf("%d \n",getTimeout(f));
 printf("%d \n",getUpdateInterval(f));
 return 0;

}*/
