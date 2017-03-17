CC=gcc
CFLAGS=
all : server 
server: server.o config.o config.h
	$(CC) -o $@ $(CFLAGS) $^ 
server.o : server.c
	$(CC) $(CFLAGS) -c $^
config.o: config.c 
	$(CC) $(CFLAGS) -c $^ 
clean: 
	rm *.o server 



