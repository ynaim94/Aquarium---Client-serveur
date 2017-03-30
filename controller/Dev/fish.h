#ifndef FISH_H
#define FISH_H

#define n 2
#define MAX 128



typedef struct{
	char name[];
	int actualPosition[n];
	int destination[n];
	int state; //start ou pas
	int length[n];
}Fish;


Fish fishes[MAX];

#endif