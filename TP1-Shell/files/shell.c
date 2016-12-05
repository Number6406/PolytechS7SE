 /*
 * Octobre 2016, Bonhoure Gilles, Abonnenc Alicia
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>


#include "readcmd.h"


int dup_out(char* fichier){
	int file = open(fichier, O_WRONLY | O_CREAT | O_TRUNC, S_IRWXU);
	if(file < 0) {
		perror("Error : Output file\n");
		exit(2);
	}
	//~ if(dup2(file,1) < 0) {
		//~ perror("Error : Output Redirection error\n");
		//~ exit(2);
	//~ }
	//~ close(file);
	return file;
}

int dup_in(char* fichier){
	int file = open(fichier, O_RDONLY);
	if(file < 0) {
		perror("Error : No input file\n");
		exit(2);
	}
	//~ close(STDIN_FILENO);
	//~ if(dup(file) < 0) {
		//~ perror("Error : Input Redirection error\n");
		//~ exit(2);
	//~ }
	return file;
}

void executer(int entree, int sortie,char** cmd){
	int pid;
	
	if(pid = fork() == 0){
		if(entree != STDIN_FILENO){
			close(STDIN_FILENO);
			if(dup(entree) < 0){
				perror("Error : Input Redirection error\n");
				exit(2);
			}
		}
		if(sortie != STDOUT_FILENO){
			close(STDOUT_FILENO);
			if(dup(sortie) < 0){
				perror("Error : Output Redirection error\n");
				exit(2);
			}
		}
		execvp(cmd[0],cmd);
	}
	else {
		wait(pid);
	}
}


int main() {
	while (1) {
		struct cmdline *l;
		int i = 0;
		int pid;
		int exec = 0;
		int entree = STDIN_FILENO;
		int sortie = STDOUT_FILENO;

		printf("shell> ");
		l = readcmd();

		// Terminaison du shell
		if(strcmp(l->seq[0][0], "exit") == 0) {
			exit(0);
		}

		// On fork pour eviter les erreur.
		pid = fork();

		switch (pid) {
			case -1 : perror("Fork Error ");
			case 0 : // Code du fils
				/* Si il n'y a pas d'erreur de redirection d'entrée ou de sortie on execute la commande */
				if(l->err) {
					printf("Error : %s\n", l->err);
					exit(1);
				}
			
				if( l->out ) { // Si il y a redirection de fichier en sortie.
					sortie = dup_out(l->out);
				}
				if( l->in ) { // Si il y a redirection de fichier en entrée
					entree = dup_in(l->in);
				}
				
				// Le Pipe //
				for (i=0; l->seq[i]!=0; i++) {
					
					int p[2];
					if(pipe(p) < 0) {
						printf("Erreur à la création du pipe %d.\n", i+1);
						exit(1);
					}
					
					if(l->seq[i+1]==NULL){
						executer(entree,sortie,l->seq[i]);
					} else {
						executer(entree,p[1],l->seq[i]);
						close(p[1]);
					}
					
					entree = p[0];
				}
				exit(0);

			default : // Code du père
				wait(pid); // Attends la fin d'éxécution du fils

		}

	}
}
