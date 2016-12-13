package jus.poc.prodcons.v1;

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
    
    /**
     * Constructeur pour la classe MessageX
     * @param a un acteur (pour récupérer son identifiant unique)
     * @param num numéro du message
     */
    public MessageX(Acteur a,int num){
        numero = num;
        message = "prod:"+a.identification()+"|id:"+num;
    }
    
    @Override
    public String toString(){
        return message;
    }

}
