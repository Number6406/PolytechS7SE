/*
 * Octobre 2016, Bonhoure Gilles, Abonnenc Alicia
 */

#include <stdio.h>
#include "readcmd.h"


int main()
{
	while (1) {
		struct cmdline *l;
		int i, j;

		printf("shell> ");
		l = readcmd();
	}
}
