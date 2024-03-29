package jus.poc.prodcons.v6;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import utils.MonSemaphore;
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
        
        sem_prod = new MonSemaphore(taille_tampon);
        sem_cons = new MonSemaphore(0);
        
        tete_production = 0;
        tete_consommation = 0;
        nb_messages_tampon = 0;
    }
    
    @Override
    public void put(_Producteur p, Message msg) throws Exception, InterruptedException {
        
        sem_prod.P(); // même fonctionnement qu'en v3 avec blocage de l'écriture pour les suivants
        synchronized(this) {
            
            Logger.getInstance().productionLogger(p, msg, tete_production);
            
            //ajout dans le buffer
            tampon[tete_production] = msg;
            tete_production = (tete_production+1)%taille();
            nb_messages_tampon++;            
        }
        sem_cons.V(); // déblocage en lecture
        
    }

    @Override
    public Message get(_Consommateur c) throws Exception, InterruptedException {
        Message m;
            
        sem_cons.P(); // blocage en lecture
        synchronized(this) {
            m = tampon[tete_consommation];
            Logger.getInstance().consommationLogger(c, m,tete_consommation);
            tete_consommation = (tete_consommation+1)%taille();

            nb_messages_tampon--;           
        }
        sem_prod.V(); // libération en écriture
        
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
