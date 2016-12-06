package jus.poc.prodcons.v1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    
    DateTimeFormatter dateFormat;
    private Message[] tampon;
    private int tete_production;
    private int tete_consommation;
    private int nb_messages_tampon;
    
    public ProdCons(int taille_tampon) {
        dateFormat = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        tampon = new Message[taille_tampon];
        tete_production = 0;
        tete_consommation = 0;
        nb_messages_tampon = 0;
    }
    
    @Override
    public synchronized void put(_Producteur p, Message msg) throws Exception, InterruptedException {
        
        while(nb_messages_tampon >= taille()) {
            wait();
        }
        
        System.out.println("["+ dateFormat.format(LocalDateTime.now()) +"]<PROD> " + msg.toString() + " en "+tete_production);
        //ajout dans le buffer
        tampon[tete_production] = msg;
        tete_production = (tete_production+1)%taille();
        
        nb_messages_tampon++;
        
        notifyAll(); 
    }

    @Override
    public synchronized Message get(_Consommateur c) throws Exception, InterruptedException {

        while(nb_messages_tampon <= 0) {
            wait();
            // Un message d'interruptedException s'affiche car on interrupt les threads à la fin du programme. C'est "normal"
        }
        
        Message m = tampon[tete_consommation];
        System.out.println("["+ dateFormat.format(LocalDateTime.now()) +"]<CONS><"+c.identification()+"> " + m.toString());
        tete_consommation = (tete_consommation+1)%taille();
        
        nb_messages_tampon--;
        notifyAll();
        
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
