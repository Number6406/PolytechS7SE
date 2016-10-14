/*
 * Octobre 2016, Bonhoure Gilles, Abonnenc Alicia
 */

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include "readcmd.h"


int main()
{
	while (1) {
		struct cmdline *l;
		int i, j;
		int pid;
		int exec = 0;
		
		printf("shell> ");
		l = readcmd();
		
		if(strcmp(l->seq[0][0],"exit") == 0)
			exit(0);
		
		pid = fork();
		
		switch (pid) {
			case -1 : perror("Fork Error ");
			case 0 : // Code du fils
				
				/* Si il n'y a pas d'erreur de redirection d'entrée ou de sortie on execute la commande */
				if(!l->err) {
					exec = execvp(l->seq[0][0],l->seq[0]);
				}
				else {/* Sinon on affiche l'erreur */
					printf("%s\n", l->err);
				}
				
				if(exec == -1) {/* Si l'éxécution n'a pas marché on affiche l'erreur */
					perror("Error ");
				}
				exit(1);
				
				
			default : // Code du père
				wait(pid); // Attends la fin d'éxécution du fils
				
			
		}
		
	}
}
