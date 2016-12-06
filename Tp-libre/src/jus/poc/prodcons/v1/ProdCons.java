package jus.poc.prodcons.v1;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

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
    private int nb_prod;
    private int nb_conso;
    
    public ProdCons(int taille_tampon) {
        tampon = new Message[taille_tampon];
        tete_production = 0;
        tete_consommation = 0;
        nb_messages_tampon = 0;
    }
    
    @Override
    public synchronized void put(_Producteur _, Message msg) throws Exception, InterruptedException {
        while(nb_messages_tampon >= taille()) {
            wait();
        }
        
        System.out.println("<PROD>" + msg.toString());
        //ajout dans le buffer
        ajoutTampon(msg);
        
        nb_messages_tampon++;
        notifyAll();
    }
    
    public void ajoutTampon(Message msg) {
        tampon[tete_consommation] = msg;
        tete_production = (tete_production+1)%taille();
        nb_messages_tampon++;
    }

    @Override
    public synchronized Message get(_Consommateur _) throws Exception, InterruptedException {
        while(nb_messages_tampon <= 0) {
            wait();
        }
        
        Message m = retireTampon();
        
        nb_messages_tampon--;
        notifyAll();
        
        return m;
    }
    
    public Message retireTampon() {
        Message m = tampon[tete_consommation];
        tete_consommation = (tete_consommation+1)%taille();
        nb_messages_tampon--;
        return m;
    }    

    @Override
    public int enAttente() {
        return taille() - nb_messages_tampon;
    }

    @Override
    public int taille() {
        return this.tampon.length;
    }
    
}
