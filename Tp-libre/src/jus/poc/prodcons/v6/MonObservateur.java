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
    
    private List<Message> a_deposer;
    private List<Message> messages;
    private List<Message> a_traiter;
    private int nbProd, nbCons, nbBuffers;
    private List<Producteur> producteurs;
    private List<Consommateur> consommateurs;
    private List<String> errors;

    
    public MonObservateur() {
        a_deposer = new LinkedList<Message>();
        messages = new LinkedList<Message>();
        a_traiter = new LinkedList<Message>();
        errors = new LinkedList<String>();
        producteurs = new LinkedList<Producteur>();
        consommateurs = new LinkedList<Consommateur>();
    }
    
    public void init(int nbProd, int nbCons, int nbBuffers) {
        this.nbProd = nbProd;
        this.nbCons = nbCons;
        this.nbBuffers = nbBuffers;
        
        errors.removeAll(errors);
    }
    
    public void conforme() {   
        if(producteurs.size() != nbProd) {
            if(!errors.contains("Le nombre de producteurs n'est pas conforme.")) {
                errors.add("Le nombre de producteurs n'est pas conforme.");            
            }
        }
        
        if(consommateurs.size() != nbCons) {
            if(!errors.contains("Le nombre de consommateurs n'est pas conforme.")) {
                errors.add("Le nombre de consommateurs n'est pas conforme.");
            }
        }
    }
    
    
    
    public void newProducteur(Producteur p) {
        this.producteurs.add(p);
    }
    
    public void newConsommateur(Consommateur c) {
        this.consommateurs.add(c);
    }
    
    public void productionMessage(Producteur p, Message m) {
        a_deposer.add(m);
    }
    
    public void depotMessage(Producteur p, Message m){
        if(a_deposer.contains(m)) {
            a_deposer.remove(m);
            messages.add(m);
            if(a_deposer.size() > nbBuffers) {
                errors.add("Dépassement de la taille du buffer.");
            }
        } else {
            errors.add("jus.poc.prodcons.v6.MonObservateur.depotMessage() : message non produit");
        }
    }
    
    public void retraitMessage(Consommateur c, Message m){
        if(messages.get(0)==m){
            messages.remove(m);
            a_traiter.add(m);
        } else {
            errors.add("jus.poc.prodcons.v6.MonObservateur.retraitMessage() : pas le bon ordre");
        }
    }
    
    public void consommationMessage(Consommateur c, Message m){
        if(a_traiter.contains(m)){
            a_traiter.remove(m);
        } else {
            errors.add("jus.poc.prodcons.v6.MonObservateur.traitementMessage() : message pas traitable");
        }
    }
    
    public boolean coherent(){
        conforme();
        return a_deposer.isEmpty() && messages.isEmpty() && a_traiter.isEmpty() && (errors.size() == 0);
    }
}
