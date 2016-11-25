
all : memshell

memshell : memshell.o alloc.o
	gcc -Wall -g -o memshell memshell.o alloc.o
	
memshell.o : memshell.c alloc.h
	gcc -Wall -g -c memshell.c
	
alloc.o : alloc.c alloc.h
	gcc -Wall -g -c alloc.c
	
clean : 
	rm *.o memshell
