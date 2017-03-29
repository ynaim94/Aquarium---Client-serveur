#ifndef VIEW_H
#define VIEW_H

#define n 3
#define MAX 128

int aquarium[2];

typedef struct{
	char buffer[n];
	int x;
	int y;
	int height;
	int width;
	int state;
}View;

View views[MAX];

#endif