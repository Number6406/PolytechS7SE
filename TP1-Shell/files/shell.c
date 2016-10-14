/*
 * Octobre 2016, Bonhoure Gilles, Abonnenc Alicia
 */

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include "readcmd.h"


void dup_out(char* fichier){
	int file = open(fichier, O_WRONLY | O_CREAT | O_TRUNC);
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

void dup_in(char* fichier){
	int file = open(fichier, O_RDONLY);
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


int main()
{
	while (1) {
		struct cmdline *l;
		int i, j;
		int pid;
		int exec = 0;
		
		printf("shell> ");
		l = readcmd();
		
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
						dup_out(l->out);
					}
					if( l->in ) { // Si il y a redirection de fichier en entrée
						dup_in(l->in);
					}
					
					// Le Pipe //
					int p[2];
					
					for (i=0; l->seq[i]!=0; i++) {
						printf(" commande %d : %s\n",i,l->seq[i][0]);
						
						pipe(p);
						dup2(p[0],0);
						close(p[0]);
						
						exec = execvp(l->seq[i][0],l->seq[i]);
						
						dup2(p[1],1);
						close(p[1]);
					} 
					
					
					//exec = execvp(l->seq[0][0],l->seq[0]);
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
