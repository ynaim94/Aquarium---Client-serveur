CC=gcc
CFLAGS=-Wall
all : serveur config
serveur: serveur.o
	$(CC) -o $@ $(CFLAGS) $^ 
serveur.o : serveur.c
	$(CC) $(CFLAGS) -c $^
config : config.o config.h 
	$(CC) -o $@ $(CFLAGS) $^
config.o: config.c 
	$(CC) $(CFLAGS) -c $^ 
clean: 
	rm *.o serveur config



