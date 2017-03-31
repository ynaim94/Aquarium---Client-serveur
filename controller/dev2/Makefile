CC=gcc
CFLAGS=
all : prompt
prompt: prompt.o intern.o parser.o
	$(CC) -g -o $@ $(CFLAGS) $^ 
server: server.o config.o config.h
	$(CC) -o $@ $(CFLAGS) $^ 
%.o : %.c
	$(CC) $(CFLAGS) -c $^
clean: 
	rm *.o prompt server 



