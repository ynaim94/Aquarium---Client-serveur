CC=gcc
CFLAGS= -g
ALLFLAGS= -D THPOOL_DEBUG
PFLAGS= -pthread
all : serveur clean_o
serveur: serveur.o config.o log.o thpool.o parseur_socket.o src/config/config.h src/client.h src/serveur.h src/log/log.h src/pool/thpool.h prompt.o intern.o parser.o src/parseur_socket/parseur_socket.h
	$(CC) -o $@ $(CFLAGS) $(PFLAGS) $^
serveur.o : src/serveur.c
	$(CC) $(CFLAGS) -c $^
config.o: src/config/config.c
	$(CC) $(CFLAGS) -c $^
log.o: src/log/log.c
	$(CC) $(CFLAGS) -c $^
thpool.o: src/pool/thpool.c
	$(CC) $(CFLAGS)  -c $^
prompt.o: src/prompt/prompt.c
	$(CC) $(CFLAGS) -c $^
intern.o: src/prompt/intern.c
	$(CC) $(CFLAGS) -c $^
parser.o: src/prompt/parser.c
	$(CC) $(CFLAGS) -c $^
parseur_socket.o: src/parseur_socket/parseur_socket.c
			$(CC) $(CFLAGS) -c $^
clean: clean_app clean_o

clean_o:
	rm *.o
clean_app:
	rm serveur
