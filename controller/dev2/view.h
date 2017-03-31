#ifndef VIEW_H
#define VIEW_H

#define MAX_VIEW 128

int nb_views;

int aquarium[2];

int state;

typedef struct{
  int id;
  int x;
  int y;
  int height;
  int width;
  int state; // if deleted or not
}View;

View views[MAX_VIEW];

#endif
