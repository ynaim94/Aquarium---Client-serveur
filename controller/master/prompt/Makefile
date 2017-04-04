CC=gcc
CFLAGS=
all :  prompt.o intern.o parser.o

test: test.o prompt.o intern.o parser.o
	$(CC) $(CFLAGS) $^ -o test
	./test

%.o : %.c
	$(CC) $(CFLAGS) -c $^

clean: 
	rm *.o




