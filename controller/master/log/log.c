/**
 * @file    log.c
 * @brief   log system code file
 * @author  CHERIF Houssem
 */
#include"log.h"

FILE* logfile = NULL;
/**
 * @function  open_log
 * @brief     opening the log file in append mode
 *
 * @param     path : string containing the path to log file
 * @return    none
 */
void open_log(const char* path)
{
  logfile = fopen(path, "a");
}
/**
 * @function  insert_log
 * @brief     insert a message in the log file with time
 *
 * @param     msg : string representing the message to insert
 * @return    none
 */
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
/**
 * @function  close_log
 * @brief     close the file log
 *
 * @param     none
 * @return    none
 */
void close_log()
{
  if(logfile)
    fclose(logfile);
  logfile = NULL;
}
