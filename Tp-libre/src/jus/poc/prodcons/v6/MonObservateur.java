/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jus.poc.prodcons.v6;

import java.util.LinkedList;
import java.util.List;
import jus.poc.prodcons.Message;

/**
 *
 * @author alicia
 */
public class MonObservateur {
    
    private List<Message> messages;
    private List<Message> a_traiter;
    
    public MonObservateur(){
        messages = new LinkedList<>();
        a_traiter = new LinkedList<>();
    }
    
    public void deposeMessage(Producteur p, Message m){
        messages.add(m);
    }
    
    public void retraitMessage(Consommateur c, Message m){
        if(messages.get(0)==m){
            messages.remove(m);
            a_traiter.add(m);
        } else {
            System.out.println("jus.poc.prodcons.v6.MonObservateur.retraitMessage() : pas le bon ordre");
        }
    }
    
    public void traitementMessage(Consommateur c, Message m){
        if(a_traiter.contains(m)){
        a_traiter.remove(m);
        } else {
            System.out.println("jus.poc.prodcons.v6.MonObservateur.traitementMessage() : message pas traitable");
        }
    }
    
    public boolean peutFinir(){
        return messages.isEmpty() && a_traiter.isEmpty();
    }
}
