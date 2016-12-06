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
        nb_prod = 0;
        nb_conso = 0;
    }
    
    @Override
    public synchronized void put(_Producteur _, Message msg) throws Exception, InterruptedException {
        
        debutProd();
        
        System.out.println("<PROD>" + msg.toString() + " en "+tete_production);
        //ajout dans le buffer
        ajoutTampon(msg);
        
        finProd();
        
    }
    
    public synchronized void debutProd() throws InterruptedException{
        while(nb_messages_tampon >= taille() /*|| nb_prod!=0*/) {
            wait();
        }
        nb_prod++;
    }
    
    public synchronized void finProd(){
        nb_messages_tampon++;
        nb_prod--;
        notifyAll();
    }
    
    public synchronized void ajoutTampon(Message msg) {
        tampon[tete_production] = msg;
        tete_production = (tete_production+1)%taille();
        nb_messages_tampon++;
    }

    @Override
    public synchronized Message get(_Consommateur _) throws Exception, InterruptedException {
        
        debutConso();
        
        Message m = retireTampon();
        
        finConso();
        
        return m;
    }
    
    public synchronized void  debutConso() throws InterruptedException{
        while(nb_messages_tampon <= 0 /*|| nb_conso > 0*/) {
            wait();
        }
        nb_conso++;
    }
    
    public synchronized void finConso(){
        nb_messages_tampon--;
        nb_conso--;
        notifyAll();
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
