package jus.poc.prodcons.v2;

import utils.MonSemaphore;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import utils.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bonhourg
 */
public class ProdCons implements Tampon {
    
    private Message[] tampon;
    
    private static MonSemaphore sem_prod;
    private static MonSemaphore sem_cons;
    
    private int tete_production;
    private int tete_consommation;
    private int nb_messages_tampon;
    
    public ProdCons(int taille_tampon) {
        tampon = new Message[taille_tampon];
        
        // déclaration de deux sémaphores :
        sem_prod = new MonSemaphore(taille_tampon); // l'un pour l'écriture, du dispose donc de base du nombre de ressources en fonction de la taille du buffer
        sem_cons = new MonSemaphore(0); // l'autre à 0 pour la lecture, car aucun message n'est encore présent dans le buffer
        
        tete_production = 0;
        tete_consommation = 0;
        nb_messages_tampon = 0;
    }
    
    @Override
    public void put(_Producteur p, Message msg) throws Exception, InterruptedException {
        
        sem_prod.P(); // prise de ressource dans le sémaphore de d'écriture pour informer qu'une place n'est plus disponible
        synchronized(this) { // mise en place d'un bloc synchronized pour éviter les opérations sur la même case du buffer.
            
            Logger.getInstance().productionLogger(p, msg, tete_production);
            
            //ajout dans le buffer
            tampon[tete_production] = msg;
            tete_production = (tete_production+1)%taille();
            nb_messages_tampon++;            
        }
        sem_cons.V(); // libération d'une ressource du sémaphore lecture, pour informer qu'un message est dans le buffer
        
    }

    @Override
    public Message get(_Consommateur c) throws Exception, InterruptedException {
        Message m;
            
        sem_cons.P(); // allocation de ressource en lecture pour récupérer un message dans le buffer
        synchronized(this) { // encore une sync pour éviter les erreurs 
            m = tampon[tete_consommation];
            Logger.getInstance().consommationLogger(c, m,tete_consommation);
            tete_consommation = (tete_consommation+1)%taille();

            nb_messages_tampon--;           
        }
        sem_prod.V(); // libération de ressource en production car le message n'est plus présent dans le buffer
        
        return m;
    }

    @Override
    public synchronized int enAttente() {
        return nb_messages_tampon;
    }

    @Override
    public int taille() {
        return this.tampon.length;
    }
    
}
