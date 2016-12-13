package jus.poc.prodcons.v1;

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
    private int tete_production;
    private int tete_consommation;
    private int nb_messages_tampon;
    
    public ProdCons(int taille_tampon) {
        tampon = new Message[taille_tampon];
        tete_production = 0;
        tete_consommation = 0;
        nb_messages_tampon = 0;
    }
    
    @Override
    public synchronized void put(_Producteur p, Message msg) throws Exception, InterruptedException {
        while(nb_messages_tampon >= taille()) { // tant que le tampon est plein, on met le thread courant en attente
            wait();
        }
        
        Logger.getInstance().productionLogger(p, msg, tete_production);
        //ajout dans le buffer
        tampon[tete_production] = msg;
        tete_production = (tete_production+1)%taille(); // changement d'emplacement de la tête d'écriture du buffer
        
        nb_messages_tampon++; // incrémentation du nombre de messages dans le tampon
        
        notifyAll(); // on réveille les threads en attente
    }

    @Override
    public synchronized Message get(_Consommateur c) throws Exception, InterruptedException {

        while(nb_messages_tampon <= 0) { // mise en attente tant que le tampon est vide
            wait();
            // Un message d'interruptedException s'affiche car on interrupt les threads à la fin du programme. C'est "normal"
        }
        
        Message m = tampon[tete_consommation]; // récupération du message dans la tête de lecture
        Logger.getInstance().consommationLogger(c, m,tete_consommation);
        tete_consommation = (tete_consommation+1)%taille(); // déplacement de la tête de lecture
        
        nb_messages_tampon--; // on retire un au nombre de messages à lire
        notifyAll(); // réveil des threads en attente.
        
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
