package jus.poc.prodcons.v4;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Message;

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
    
    public MessageX(Acteur a,int num, int nb_exemplaires){
        numero = num;
        this.nb_exemplaires = nb_exemplaires;
        message = "prod:"+a.identification()+"|id:"+num;
        this.nb_consommateurs = 0;
    }
    
    public boolean pret(){
        return nb_consommateurs == nb_exemplaires;
    }
    
    public synchronized MessageX retirer() throws InterruptedException{
        nb_consommateurs++;
        while(nb_consommateurs < nb_exemplaires){
            wait();
        }
        notifyAll();
        return this;
    }
    
    @Override
    public String toString(){
        return message;
    }

}
