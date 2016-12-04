 /*
 * Octobre 2016, Bonhoure Gilles, Abonnenc Alicia
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>


#include "readcmd.h"


void dup_out(char* fichier){
	int file = open(fichier, O_WRONLY | O_CREAT | O_TRUNC, S_IRWXU);
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
	if(dup2(0,file) < 0) {
		perror("Error : Input Redirection error\n");
		exit(2);
	}
	close(0);
}


int main() {
	while (1) {
		struct cmdline *l;
		int i = 0;
		int pid, fpid;
		int exec = 0;
		int p[2];

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
					dup_out(l->out);
				}
				if( l->in ) { // Si il y a redirection de fichier en entrée
					dup_in(l->in);
				}
				
				// Le Pipe //
				int res_pipe = pipe(p);
				if(res_pipe < 0) {
					printf("Erreur à la création du pipe %d.\n", i+1);
					exit(1);
				}


				for (i=0; l->seq[i]!=0; i++) {
					fpid = fork();
					if(fpid == 0) {
						close(p[1]);
						dup2(p[1], STDOUT_FILENO);
						close(p[0]);
						printf(" commande %d : %s\n",i,l->seq[i][0]);
						exec = execvp(l->seq[i][0],l->seq[i]);
						printf("exécution :)\n");

						if(exec == -1) {/* Si l'éxécution n'a pas marché on affiche l'erreur */
							perror("Error ");
							exit(1);
						}

					} else {
						close(p[0]);
						dup2(p[0], STDIN_FILENO);
						printf("Je suis le père de %d\n", fpid);
					}
					wait(0);
					printf("Terminaison de %d\n==========================================\n", fpid);

				}
				
				//exec = execvp(l->seq[0][0],l->seq[0]);
				exit(0);

			default : // Code du père
				wait(pid); // Attends la fin d'éxécution du fils

		}

	}
}
