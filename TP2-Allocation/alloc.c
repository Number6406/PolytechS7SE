/**
 * TP2 - Allocation mémoire
 * Abonnenc Alicia
 * Bonhoure Gilles
 * RICM4
 */


#include <stdio.h>
#include <stddef.h>
#include "alloc.h"


struct fb;

struct fb {
	size_t size;
	struct fb* next;
};

struct bb {
	size_t size;
};

//char mem_heap[HEAP_SIZE]; est dans le .h déjà


/** 
 * Initialisation de la mémoire avec les structures de contrôle adéquates
*/
void mem_init(){
	char *adr_zone = mem_heap + sizeof(struct fb*);
	struct fb p_bloc_vide;
	
	// Placement du pointeur vers le début de la liste chainées des espaces disponibles
	*((struct fb**) mem_heap) = (struct fb*)(mem_heap + sizeof(struct fb*));
	
	// Remplissage de la structure fb placée en début de la seule zone libre
	p_bloc_vide.size = HEAP_SIZE - sizeof(struct fb*);
	p_bloc_vide.next = NULL;
	
	// Placement de la structure en mémoire
	*((struct fb*) adr_zone) = p_bloc_vide;
}

/**
 * Méthode qui alloue un bloc donné avec une taille donnée et modifie
 * la chaine des espaces libres en conséquence
 */
 void allouer(size_t size, char* zone, struct fb* courant, struct fb* precedent){
	// Placer la structure bb de début de zone
	struct bb p_bloc_plein;
	p_bloc_plein.size = size + sizeof(struct bb);
	
	char* adr_bloc_plein_suivant = (char*)courant + courant->size;
	size_t espace_restant = (size_t) adr_bloc_plein_suivant - ((size_t)courant + p_bloc_plein.size);
	
	if(espace_restant < sizeof(struct fb)){
		p_bloc_plein.size = courant->size;
		precedent->next = courant->next;
	} else {		
		//Rebrancher
		struct fb p_bloc_vide; // Nouveau bloc crée
		char* adr_bloc_vide_suivant = (char*)courant + p_bloc_plein.size;
		if(courant->next == NULL) { // fin de mémoire
			p_bloc_vide.next = NULL;
		} else {
			p_bloc_vide.next = courant->next;
		}
		p_bloc_vide.size = (size_t) adr_bloc_plein_suivant - ((size_t)courant + p_bloc_plein.size);
		
		if(precedent == NULL){ // Si on est le premier bloc libre
			*(struct fb**)mem_heap = (struct fb*) adr_bloc_vide_suivant;
		} else {
			precedent->next = (struct fb*) adr_bloc_vide_suivant;
		}
		
		*(struct fb*)adr_bloc_vide_suivant = p_bloc_vide;
	}
	
	*(struct bb*)zone = p_bloc_plein;	
 }

/**
 * Allocation d'un espace mémoire de taille size dans la mémoire actuelle
 * ---
 * Ici on implémente la méthode du First Fit
 */
void *mem_alloc(size_t size){
	// On caste le "début de la mémoire" en pointeur de pointeur de structure fb
	struct fb** debut = (struct fb **) mem_heap;
	struct fb* courant; // Pour avancer dans la file
	struct fb* precedent; // Pour pouvoir rebrancher
	
	precedent = NULL;
	courant = *debut; // Première zone libre
	
	// Tant que la zone n'est pas assez grande avancer
	while(courant != NULL && courant->size < size){ 
		precedent = courant;
		courant = courant->next; 
	}
	
	if(courant == NULL) {
		//fprintf(stderr,"Pas d'espace disponible de cette taille\n");
		return NULL;
	} else {
		char* zone = (char*)courant; // Le début de la zone
		allouer(size,zone,courant,precedent);
		return zone + sizeof(struct bb);	
	}
}


/**
 * Libérer la zone d'adresse donnée et de taille fournie 
 * ---
 * l'argument size est ignoré.
 */
void mem_free(void *zone, size_t size){
	// Trouver les deux zones adjacentes (pour chainer et fusionner si besoin)
	struct fb *precedent, *suivant,*courant;
	precedent = NULL;
	courant = *(struct fb**)mem_heap;
	while(courant!=NULL && (void*)courant < zone) {
		precedent = courant;
		courant = courant->next;
	}
	
	if(courant == NULL) {
		printf("Erreur..\n");
	} else {
		
		suivant = courant->next;
		struct fb p_bloc_libre;
		
		if(zone + (*(struct bb*)zone).size == suivant){ //Fusionner
			if(suivant == NULL){ // bloc final
				p_bloc_libre.size = (*(struct bb*)zone).size + suivant->size;
				p_bloc_libre.next = NULL;
			} else { // Pas bloc final
				p_bloc_libre.size = (*(struct bb*)zone).size + suivant->size;
				p_bloc_libre.next = suivant->next;
			}
		} 
		else { // Pas fusionner
			p_bloc_libre.size = (*(struct bb*)zone).size;
			p_bloc_libre.next = suivant;
		}
		
		if(precedent == NULL){ // Premier bloc
			*(struct fb**)mem_heap = zone;
		} else {
			precedent->next = zone;
		}
		*(struct fb*)zone = p_bloc_libre;
		
	}
	
}

/**
 * Affiche la totalité des espaces libres de la zone mémoire gérée
 * ----
 * Elle prends en paramètre une procédure "print" qui affiche les infos
 * relatives à une zone libre (donnée en paramètre)
 * ---
 * Cette fonction parcoure la liste chainée des zones libres 
 * en les affichant une par une en appelant print
 */
void mem_show(void (*print)(void *zone, size_t size)){
	// On caste le "début de la mémoire" en pointeur de pointeur de structure fb
	struct fb** debut = (struct fb **) mem_heap;
	struct fb* courant; // Pour avancer dans la file
	
	void* adr_zone;
	size_t taille;
	
	courant = *debut;
	
	// Tant qu'il reste des zones à parcourir
	while(courant!=NULL){
		adr_zone = (void*)courant;
		taille = courant->size;
		print(adr_zone, taille); // Afficher
		
		courant = courant->next; // Avancer dans la liste chainée
	}
}

