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
        nb_prod = 0;
        nb_conso = 0;
        nb_messages_tampon = 0;
    }
    
    @Override
    public void put(_Producteur _, Message msg) throws Exception, InterruptedException {
        debutProduction();
        System.out.println(msg.toString());
        //ajout dans le buffer
        finProduction();
    }
    
    public void ajoutTampon(Message msg) {
        if(tete_production == tampon.length) {
            tete_production = 0;
        }
        tampon[tete_consommation] = msg;
        tete_production++;
        nb_messages_tampon++;
    }
    
    public synchronized void debutProduction() throws InterruptedException {
        while(nb_prod != 0 || nb_messages_tampon == taille()) {
            wait();
        }
        nb_prod++;
    }
    
    public synchronized void finProduction() {
        nb_prod--;
        nb_messages_tampon++;
        notifyAll();
    }

    @Override
    public Message get(_Consommateur _) throws Exception, InterruptedException {
        debutConsommation(_);
        Message m = retireTampon();
        finConsommation();
        return m;
    }
    
    public Message retireTampon() {
        if(tete_consommation == tampon.length) {
            tete_consommation = 0;
        }
        Message m = tampon[tete_consommation];
        tete_consommation++;
        nb_messages_tampon--;
        return m;
    }
    
    synchronized public void debutConsommation(_Consommateur c) throws InterruptedException {
        while(nb_conso != 0 || nb_messages_tampon != 0) {
            wait();
        }
        nb_conso++;
    }
    
    public synchronized void finConsommation(){
        nb_conso--;
        nb_messages_tampon--;
        notifyAll();
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
