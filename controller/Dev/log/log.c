#include"log.h"

FILE* logfile = NULL;
void open_log(const char* path)
{
  logfile = fopen(path, "a");
}

void insert_log(const char* msg)
{
  time_t rawtime;
  struct tm* timeinfo;
  char buffer[BUFF_SIZE];
  if(!logfile)
    return;
  time(&rawtime);
  timeinfo = localtime(&rawtime);
  strftime(buffer,BUFF_SIZE,"%x %X",timeinfo);
  fprintf(logfile,"[%s]   %s\n",buffer,msg);
  fflush(logfile);

}
void close_log()
{
  if(logfile)
    fclose(logfile);
  logfile = NULL;
}
