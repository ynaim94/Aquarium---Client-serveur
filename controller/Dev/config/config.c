#include <stdlib.h>
#include <stdio.h>
#include"unistd.h"
#include"config.h"
#define TAILLE_MAX 1000
FILE* configfile = NULL;
void open_config(const char* path)
{
    configfile = fopen(path, "r");
    if (configfile != NULL)
    {
       printf("ouverture avec succ�s\n");
    }
    else
    {
        printf("Impossible d'ouvrir le fichier de configuration \n");
    }
}
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
