/**
 * @file    parseur_socket.c
 * @brief   the parseur_socket code file
 * @author  CHERIF Houssem
 */
#include "parseur_socket.h"
#include "../view.h"
#include "../fish.h"
/***************************
 *Regular expressions
 */
const char *str_hello_id="^(hello in as[:space:][:alnum:]+){1}";
const char *str_hello="^(hello){1}";
const char *str_add_fish="^(addFish[:space:]([:alnum:]+)[:space:]at[:space:]([:digit:]{1,2})*([:digit:]{1,2}),[:space:]([:digit:]{1,2})*([:digit:]{1,2}),[:space:]([:alnum:]+)){1}";
const char *str_del_fish="^(delFish[:space:][:alnum:]+){1}";
const char *str_log_out="^(log out)";
const char *str_start_fish="^(startFish[:space:][:alnum:]+){1}";
const char *str_get_fish="^(getFishes){1}";
const char *str_get_fish_continously="^(getFishesContinuously){1}";
const char *str_ping"^(ping[:space:]([:digit:]{4,5})){1}";

/***************************
 *Functions implementations
 */
/**
* @function  parser
* @brief     analyse and recognize the command receveid
*
* @param     s : input string to recognize
* @return    an integer refering to the command recognized or an erreur code
*/
int parser(const char *s)
{
  int len = 9;
  const char* requests[] = {str_hello,str_hello_id,str_add_fish,str_del_fish,str_log_out,str_start_fish,str_get_fish,str_get_fish_continously,str_ping};
  regex_t preg;
  int err;

  for(int i = 0; i<len ; i++)
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
int parser_hello(char* reply)
{
  
}
