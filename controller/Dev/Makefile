CC=gcc
CFLAGS=
all : serveur
serveur: serveur.o config.o config.h client.h serveur.h
	$(CC) -o $@ $(CFLAGS) $^
serveur.o : serveur.c
	$(CC) $(CFLAGS) -c $^
config.o: config.c
		$(CC) $(CFLAGS) -c $^
clean:
	rm *.o server
