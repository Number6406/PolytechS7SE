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
		
		
		// Pour pouvoir sortir du shell
		/*
		if(strcmp(l->seq[0][0],"exit") == 0)
			exit(0);
		
		*/
		
		// On fork pour eviter les erreur.
		pid = fork();
		
		switch (pid) {
			case -1 : perror("Fork Error ");
			case 0 : // Code du fils
				/* Si il n'y a pas d'erreur de redirection d'entrée ou de sortie on execute la commande */
				if(l->err) {
					printf("Error : %s\n", l->err);
				}
				else {
					if( l->out ) { // Si il y a redirection de fichier en sortie.
						int file = open(l->out, O_WRONLY | O_CREAT);
						if(file < 0) {
							perror("Error : Output file\n");
							exit(2);
						}
						if(dup2(file,1) < 0) {
							perror("Error : Output Redirection error\n");
							exit(2);
						}
						close(file);
					}
					if( l->in ) { // Si il y a redirection de fichier en entrée
						int file = open(l->in, O_RDONLY);
						if(file < 0) {
							perror("Error : No input file\n");
							exit(2);
						}
						if(dup2(file,0) < 0) {
							perror("Error : Input Redirection error\n");
							exit(2);
						}
						close(file);
					}
					
					exec = execvp(l->seq[0][0],l->seq[0]);
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
