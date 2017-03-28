CC=gcc
CFLAGS=
all : serveur
serveur: serveur.o config.o log.o config/config.h client.h serveur.h log/log.h
	$(CC) -o $@ $(CFLAGS) $^
serveur.o : serveur.c
	$(CC) $(CFLAGS) -c $^
config.o: config/config.c
		$(CC) $(CFLAGS) -c $^
log.o: log/log.c
				$(CC) $(CFLAGS) -c $^
clean:
	rm *.o serveur
