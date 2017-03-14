CC=gcc
CFLAGS=
all : serveur 
serveur: serveur.o config.o config.h
	$(CC) -o $@ $(CFLAGS) $^ 
serveur.o : serveur.c
	$(CC) $(CFLAGS) -c $^
config.o: config.c 
	$(CC) $(CFLAGS) -c $^ 
clean: 
	rm *.o serveur 



