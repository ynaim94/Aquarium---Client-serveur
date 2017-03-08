#include <stdlib.h>
#include <stdio.h>

unsigned int getportnumber(FILE* fd){
   rewind(fd);

}

int main(int argc, char *argv[]){
    FILE* fichier = NULL;
    fichier = fopen("controller.cfg", "r");
    if (fichier != NULL)
    {

       

    }
    else
    {
        printf("Impossible d'ouvrir le fichier test.txt");
    }


    return 0;

}
