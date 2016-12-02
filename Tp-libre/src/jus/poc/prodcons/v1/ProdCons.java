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
    }
    
    @Override
    public void put(_Producteur _, Message msg) throws Exception, InterruptedException {
        debutProduction(_);
        //ajout dans le buffer
        finProduction();
    }
    
    public void ajoutTampon(Message msg) {
        //TODO
    }
    
    synchronized public void debutProduction(_Producteur p) throws InterruptedException {
        while(nb_prod != 0 || nb_messages_tampon == taille()) {
            p.wait();
        }
        nb_prod++;
    }
    
    synchronized public void finProduction() {
        nb_prod--;
        nb_messages_tampon++;
        notifyAll();
    }

    @Override
    public Message get(_Consommateur _) throws Exception, InterruptedException {
    }

    @Override
    public int enAttente() {
    }

    @Override
    public int taille() {
        return this.tampon.length;
    }
    
}
