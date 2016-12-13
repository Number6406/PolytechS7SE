package jus.poc.prodcons.v4;

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
    private int[] nb_conso;
    
    private static MonSemaphore sem_prod;
    private static MonSemaphore sem_cons;
    
    private int tete_production;
    private int tete_consommation;
    private int nb_messages_tampon;
    
    public ProdCons(int taille_tampon) {
        tampon = new Message[taille_tampon];
        nb_conso = new int[taille_tampon];
        
        sem_prod = new MonSemaphore(taille_tampon);
        sem_cons = new MonSemaphore(0);
        
        tete_production = 0;
        tete_consommation = 0;
        nb_messages_tampon = 0;
    }
    
    /**
     * Cette méthode met en attente le thread courant tant qu'il n'y a pas de 
     * place dans le buffer.
     * 
     * Il notifie à la fin pour que si il y a des consommateurs en attente
     * ils soient réveillés.
     * 
     * @param p
     * @param msg le message à déposer
     * @throws Exception
     * @throws InterruptedException 
     */
    @Override
    public void put(_Producteur p, Message msg) throws Exception, InterruptedException {
        
        sem_prod.P();
        synchronized(this) {
            
            Logger.getInstance().productionLogger(p, msg, tete_production,((MessageX)msg).getNbExemplaires());
            
            //ajout dans le buffer
            tampon[tete_production] = msg;
            tete_production = (tete_production+1)%taille();
            nb_messages_tampon++;            
        }
        for(int i = 0; i < ((MessageX)msg).getNbExemplaires(); i++){ // libération en lecture pour permettre à tous les exemplaires d'être consommés
            sem_cons.V();
        }
        
                
    }

    /**
     * Cette méthode met en attente le thread courant tant qu'il n'y a pas de 
     * messages dans le buffer.
     * 
     * Il notifie à la fin pour que si il y a des producteurs en attente
     * ils soient réveillés.
     * 
     * @param c
     * @return le message retiré
     * @throws Exception
     * @throws InterruptedException 
     */
    @Override
    public Message get(_Consommateur c) throws Exception, InterruptedException {
        Message m;
            
        sem_cons.P();
        synchronized(this){ // Assure qu'il y a le bon nombre de consommateur
            if(nb_conso[tete_consommation]>=((MessageX)tampon[tete_consommation]).getNbExemplaires()){ // vérification de si tous les exemplaires sont consommés par des consommateurs diférents.
                nb_conso[tete_consommation]=0;
                tete_consommation = (tete_consommation+1)%taille(); // dans ce cas, on passe la tête de lecture au message suivant
            }
            nb_conso[tete_consommation]++;
        }
            m = ((MessageX)tampon[tete_consommation]).retirer(); // on retire un exemplaire du message
            Logger.getInstance().consommationLogger(c, m,tete_consommation);
        synchronized(this) {
            if(!(((MessageX)m).estConsomme())){    
                nb_messages_tampon--;   
                ((MessageX)m).setConsomme(true);
            }
        }
        sem_prod.V();
        
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
