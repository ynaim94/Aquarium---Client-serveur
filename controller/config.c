#include <stdlib.h>
#include <stdio.h>
#include"unistd.h"
#include"config.h"
#define TAILLE_MAX 1000
FILE* open()
{
    FILE* fichier = NULL;
    fichier = fopen("controller.cfg", "r");
    if (fichier != NULL)
    {

       printf("ouverture avec succès\n");

    }
    else
    {
        printf("Impossible d'ouvrir le fichier de configuration \n");
    }
   return fichier;
}
long int getPortnumber(FILE* fd){
   char chaine[TAILLE_MAX+1] = "";
   char * pEnd;
   rewind(fd);
   char caractereActuel ;
   fgets(chaine, TAILLE_MAX, fd);
   while (caractereActuel != '=')
   caractereActuel = fgetc(fd);
   fgets(chaine, TAILLE_MAX, fd);
   long ret = strtol(chaine,&pEnd,10);
   fclose(fd);
   if (ret < 65535)
    return ret;
   else
    //printf("invalid port number \n");
   return -1;
}
unsigned int getTimeout(FILE* fd){
    int i = 0;
   char chaine[TAILLE_MAX+1] = "";
   char * pEnd;
   rewind(fd);
   char caractereActuel ;
   for (i=0; i<3; i++)
    fgets(chaine, TAILLE_MAX, fd);
   while (caractereActuel != '=')
   caractereActuel = fgetc(fd);
   fgets(chaine, TAILLE_MAX, fd);
   long ret = strtol(chaine,&pEnd,10);
   fclose(fd);
   return ret;
}
unsigned int getUpdateInterval(FILE* fd){
    int i = 0;
   char chaine[TAILLE_MAX+1] = "";
   char * pEnd;
   rewind(fd);
   char caractereActuel ;
   for (i=0; i<5; i++)
    fgets(chaine, TAILLE_MAX, fd);
   while (caractereActuel != '=')
   caractereActuel = fgetc(fd);
   fgets(chaine, TAILLE_MAX, fd);
   long ret = strtol(chaine,&pEnd,10);
   fclose(fd);
   return ret;
}

/*int main(int argc, char *argv[]){
 FILE* f = open();
 printf("%d \n",getPortnumber(f));
 printf("%d \n",getTimeout(f));
 printf("%d \n",getUpdateInterval(f));
 return 0;

}*/
