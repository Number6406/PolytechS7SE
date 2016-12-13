package jus.poc.prodcons.v4;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Message;
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
public class MessageX implements Message {
    
    private String message = "";
    private int numero;
    private int nb_exemplaires;
    private int nb_consommateurs;
    private boolean consomme;
    
    /**
     * Constructeur pour la classe MessageX
     * 
     * Un message connait le nombre de consommateurs actuellement en attente sur lui.
     * 
     * @param a un acteur (pour récupérer son identifiant unique)
     * @param num numéro du message
     * @param nb_exemplaires 
     */
    public MessageX(Acteur a,int num, int nb_exemplaires){
        numero = num;
        this.nb_exemplaires = nb_exemplaires;
        message = "prod:"+a.identification()+"|id:"+num;
        this.nb_consommateurs = 0;
        this.consomme = false;
    }
    
    
    private boolean pret(){
        return nb_consommateurs >= nb_exemplaires;
    }
    
    /**
     * Cette méthode est appelée par un producteur après avoir déposé son
     * message. Elle le fait attendre tant que tout les consommateurs ne 
     * sont pas au rendez vous.
     * @throws InterruptedException 
     */
    public synchronized void attendre() throws InterruptedException{
        while(!pret()){
            wait();
        }
    }
    
    /**
     * Cette méthode fait attendre les consommateurs tant qu'ils ne sont pas
     * assez nombreux. Le premier à pouvoir passer réveille tout les autres.
     * @return le message
     * @throws InterruptedException 
     */
    public synchronized MessageX retirer() throws InterruptedException{
        nb_consommateurs++;
        Logger.getInstance().messageRetirerLogger("consommateur n° " + nb_consommateurs);
        while(nb_consommateurs < nb_exemplaires){
            wait();
        }
        notifyAll();
        return this;
    }
    
    public int getNbExemplaires(){
        return nb_exemplaires;
    }
    
    /**
     * Sert à verifier si on a déjà avancé la tete de liste dans prodcons
     * @param c 
     */
    public void setConsomme(boolean c){
        consomme = c;
    }
    
    public boolean estConsomme(){
        return consomme;
    }
    
    @Override
    public String toString(){
        return message;
    }

}
