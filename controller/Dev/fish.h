#ifndef FISH_H
#define FISH_H

#define DIM 2
#define MAX 128
#define STARTED 1
#define STOPED 2
#define RandomPathWay 0

/**
 *  @struct Fish
 *  @brief the fish description
 *
 *  @var name : the fish name
 *  @var actualPosition : arry of the actual postion of the fish
 *  @var destination : array of the destination of the fish
 *  @var dimension : array of the dimension of the fish
 *  @var state : is the fish swiming or not
 *  @var mobility : the mobility model associated to the fish
 */

typedef struct{
	char name[MAX];
	int actualPosition[DIM];
	int destination[DIM];
	int state; //start ou pas
	int dimension[DIM];
	int mobility
}Fish;


Fish fishes[MAX];

#endif
