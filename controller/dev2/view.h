#ifndef VIEW_H
#define VIEW_H

#define MAX 128

int nb_views;

int aquarium[2];

typedef struct{
  int id;
  int x;
  int y;
  int height;
  int width;
  int state; // if deleted or not
}View;

View views[MAX];

#endif
