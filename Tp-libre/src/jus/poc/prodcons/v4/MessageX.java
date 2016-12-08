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
    
    public MessageX(Acteur a,int num, int nb_exemplaires){
        numero = num;
        this.nb_exemplaires = nb_exemplaires;
        message = "prod:"+a.identification()+"|id:"+num;
    }
    
    @Override
    public String toString(){
        return message;
    }

}
