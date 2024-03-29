package jus.poc.prodcons.v6;


import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.v1.MessageX;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bonhourg
 */
public class Producteur extends Acteur implements _Producteur {
    
    private MonObservateur mon_observateur;
    private final ProdCons tampon;
    private int nb_messages;
    private final int[] tpsTraitement;
    private Message m;
    private int numero_de_message;
    
    /**
     * Constructeur pour la classe producteurs avec un nombre non connu au 
     * préalable de messages à ecrire
     * 
     * les temps de traitement de chaque messages sont stockés dans un tableau
     * de taille nb_messages
     * 
     * @param tampon dans lequel il va écrire
     * @param obs 
     * @param mon_observateur
     * @param nombreMoyenDeProduction
     * @param deviationNombreMoyenDeProduction
     * @param tempsMoyenProduction
     * @param deviationTempsMoyenProduction
     * @throws ControlException 
     */
    public Producteur(ProdCons tampon,Observateur obs, MonObservateur mon_observateur, int nombreMoyenDeProduction, int deviationNombreMoyenDeProduction, int tempsMoyenProduction, int deviationTempsMoyenProduction) throws ControlException{
        super(Acteur.typeProducteur,obs,tempsMoyenProduction,deviationTempsMoyenProduction);
        this.tampon = tampon;
        nb_messages = Aleatoire.valeur(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);
        this.tpsTraitement = Aleatoire.valeurs(nb_messages, tempsMoyenProduction, deviationTempsMoyenProduction);
        numero_de_message = 0;
        this.mon_observateur = mon_observateur;
    }
    
    /**
     * Production d'un message (en faisant dormir le thread)
     * @throws InterruptedException
     * @throws Exception
     */
    public void produire() throws InterruptedException, Exception{
        Thread.sleep(tpsTraitement[nb_messages-1]*1000);
        m = new MessageX(this,numero_de_message++);
        observateur.productionMessage(this, m, tpsTraitement[nb_messages-1]);
        mon_observateur.productionMessage(this, m, tpsTraitement[nb_messages-1]);
    }
    
    /**
     * Dépot d'un message. L'attente est gérée dans le tampon
     * @throws InterruptedException
     * @throws Exception 
     */
    public void deposer() throws InterruptedException, Exception{
        tampon.put(this, m);
        observateur.depotMessage(this, m);
        mon_observateur.depotMessage(this, m);
        nb_messages--;
    }
    
    
    public void run() {
        // Production de tous les messages
        while(nombreDeMessages() > 0) {
            try {
                
                produire();
                deposer();
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }
    }

    @Override
    public int nombreDeMessages() {
        return nb_messages; //To change body of generated methods, choose Tools | Templates.
    }
    
}
