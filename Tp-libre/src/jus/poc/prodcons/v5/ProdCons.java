package jus.poc.prodcons.v5;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
    
    DateTimeFormatter dateFormat;
    private Message[] tampon;
    
    private final Lock lock;
    private final Condition nonPlein; 
    private final Condition nonVide; 
    
    private int tete_production;
    private int tete_consommation;
    private int nb_messages_tampon;
    
    public ProdCons(int taille_tampon) {
        dateFormat = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        tampon = new Message[taille_tampon];
        
        this.lock = new ReentrantLock();
        this.nonPlein = lock.newCondition();
        this.nonVide = lock.newCondition();
        
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
        lock.lock();
        try{
            while(nb_messages_tampon == taille()){
                nonPlein.await();
            }
            //ajout dans le buffer
            tampon[tete_production] = msg;
            tete_production = (tete_production+1)%taille();
            nb_messages_tampon++; 
            Logger.getInstance().productionLogger(p, msg, tete_production);
            nonVide.signal();
        } finally {
            lock.unlock();
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
        
        lock.lock();
        try{
            while(nb_messages_tampon==0){
                nonVide.await();
            }
            // Recupérer
            m = tampon[tete_consommation];
            tete_consommation = (tete_consommation+1)%taille();
            nb_messages_tampon--; 
            Logger.getInstance().consommationLogger(c, m,tete_consommation);
            nonPlein.signal();
        } finally {
            lock.unlock();
        }        
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
