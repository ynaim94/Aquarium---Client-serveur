CC=gcc
CFLAGS=
PFLAGS= -pthread
all : serveur clean_o
serveur: serveur.o config.o log.o thpool.o config/config.h client.h serveur.h log/log.h pool/thpool.h
	$(CC) -o $@ $(CFLAGS)$(PFLAGS) $^
serveur.o : serveur.c
	$(CC) $(CFLAGS) -c $^
config.o: config/config.c
	$(CC) $(CFLAGS) -c $^
log.o: log/log.c
	$(CC) $(CFLAGS) -c $^
thpool.o: pool/thpool.c
	$(CC) $(CFLAGS) -c $^
clean: clean_app clean_o

clean_o:
	rm *.o
clean_app:
	rm serveur
