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
    
    private List<Message> a_deposer; // Liste des messages produits à déposer dans le buffer
    private List<Message> messages; // Liste des messages présents dans le buffer
    private List<Message> a_traiter; // Liste des messages en attente de consommation (hors buffer)
    private int nbProd, nbCons, nbBuffers; //Nombre initial de producteurs, consommateurs et buffers annoncés.
    private List<Producteur> producteurs; 
    private List<Consommateur> consommateurs;
    private List<String> errors; // Liste des erreurs pour un affichage de celles-ci

    
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
        
        errors.removeAll(errors); //suppresion de tout message d'erreur potentiel.
    }
    
    public void conforme() {   
        if(producteurs.size() != nbProd) { // Vérification du nombre de producteurs conformément à la valeur annoncée à la création
            if(!errors.contains("Le nombre de producteurs n'est pas conforme.")) {
                errors.add("Le nombre de producteurs n'est pas conforme.");            
            }
        }
        
        if(consommateurs.size() != nbCons) { // vérification du nombre de producteurs
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
    
    public void productionMessage(Producteur p, Message m, int tmp) {
        // nous n'avons pas vérifié le temps de traitement
        a_deposer.add(m);
    }
    
    public void depotMessage(Producteur p, Message m){
        if(a_deposer.contains(m)) { // Vérification de la présence du message dans la liste des messages produits
            a_deposer.remove(m);
            messages.add(m); // dépot dans le buffer
            if(messages.size() > nbBuffers) { // vérification du dépassement de buffer (semble non fonctionnel)
                errors.add("Dépassement de la taille du buffer.");
            }
        } else {
            errors.add("jus.poc.prodcons.v6.MonObservateur.depotMessage() : message non produit ["+m+"]");
        }
    }
    
    public void retraitMessage(Consommateur c, Message m){
        if(messages.get(0)==m){ // vérification de la présence du message en tête de lecture
            messages.remove(m);
            a_traiter.add(m);
        } else {
            errors.add("jus.poc.prodcons.v6.MonObservateur.retraitMessage() : pas le bon ordre ["+m+"]");
        }
    }
    
    public void consommationMessage(Consommateur c, Message m){
        if(a_traiter.contains(m)){
            a_traiter.remove(m); // retrait du message : consommation terminée
        } else {
            errors.add("jus.poc.prodcons.v6.MonObservateur.traitementMessage() : message pas traitable ["+m+"]");
        }
    }
    
    public boolean coherent(){
        conforme();
        if(a_deposer.isEmpty() || messages.isEmpty() || a_traiter.isEmpty()) {
            errors.add("Buffer ou producteurs / consommateurs avec des messages non traités.");
        }
        return a_deposer.isEmpty() && messages.isEmpty() && a_traiter.isEmpty() && (errors.size() == 0); // vérification du traitement de touts les messages
    }

    public void listErrors() {
        for (String error : errors) {
            System.err.println(error);
        }
    }
}
