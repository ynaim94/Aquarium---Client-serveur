CC=gcc
CFLAGS=
all : server parser.o
server: server.o config.o config.h
	$(CC) -o $@ $(CFLAGS) $^ 
%.o : %.c
	$(CC) $(CFLAGS) -c $^
clean: 
	rm *.o server 



