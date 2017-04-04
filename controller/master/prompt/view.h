/**
 * @file    view.h
 * @brief   the view header file
 * @author  MOURTADI Mehdi&CHERIF Houssem
 */
#ifndef VIEW_H
#define VIEW_H

#define n 3
#define MAX_VIEW 128
#define FREE 0
#define ATTACHED 1


/**
 *  @struct View
 *  @brief the view struct
 *
 *  @var id : the vue name
 *  @var x : the view x position
 *  @var y : the view y position
 *  @var height : the view's height
 *  @var width : the view's width
 *  @var state : the view's state 0 if the view is free, 1 if there's a client attached to the view
 */

typedef struct{
  int id;
  int x;
  int y;
  int height;
  int width;
  int state;
}View;

View views[MAX_VIEW];
int aquarium[2];
int nb_views;
int state;

#endif //VIEW_H
