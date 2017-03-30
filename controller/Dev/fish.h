#ifndef FISH_H
#define FISH_H

#define n 2
#define MAX 128

/**
 *  @struct Fish
 *  @brief the fish description
 *
 *  @var name : the fish name
 *  @var actualPosition : arry of the actual postion of the fish
 *  @var destination : array of the destination of the fish
 *  @var dimension : array of the dimension of the fish
 *  @var state : is the fish swiming or not
 */

typedef struct{
	char name[];
	int actualPosition[n];
	int destination[n];
	int state; //start ou pas
	int dimension[n];
}Fish;


Fish fishes[MAX];

#endif
