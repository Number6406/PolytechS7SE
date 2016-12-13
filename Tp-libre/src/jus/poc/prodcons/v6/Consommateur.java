package jus.poc.prodcons.v6;


import utils.Logger;
import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bonhourg
 */
public class Consommateur extends Acteur implements _Consommateur {
    
    private MonObservateur mon_observateur;
    private ProdCons tampon;
    private int nb_messages;
    private Message message;
    private int tpsTraitement;
    private boolean enTraitement = false;
    
    /**
     * 
     * @param tampon
     * @param observateur
     * @param mon_observateur
     * @param moyenneTempsDeTraitement
     * @param deviationTempsDeTraitement
     * @throws ControlException 
     */
    public Consommateur(ProdCons tampon, Observateur observateur, MonObservateur mon_observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement) throws ControlException {
        super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
        this.tampon = tampon;
        this.nb_messages = 0;
        this.mon_observateur = mon_observateur;
    }
    
    /**
     * fonction de récupération d'un message en tampon
     * @throws InterruptedException
     * @throws Exception 
     */
    public void retirer() throws InterruptedException, Exception {
        message = tampon.get(this); // attente de la récupération
        observateur.retraitMessage(this, message);
        mon_observateur.retraitMessage(this, message);
        nb_messages++; // incrémentation du nombre de messages
    }

    /**
     * Fonction de consommation (traitement) du message courant
     * @throws InterruptedException
     * @throws ControlException 
     */
    public void consommer() throws InterruptedException, ControlException {
        enTraitement = true; // informe l'extérieur que le consommateur est en cours de traitement. Utile pour la terminaison
        tpsTraitement = Aleatoire.valeur(moyenneTempsDeTraitement(), deviationTempsDeTraitement());
        Thread.sleep(tpsTraitement*1000); // Pour être en secondes
        observateur.consommationMessage(this, message, tpsTraitement);
        mon_observateur.consommationMessage(this, message);
        Logger.getInstance().traitementLogger(this, message, tpsTraitement);
        enTraitement = false; // fin de traitement : on annonce à l'extérieur qu'on peut être interrompu car on n'est plus occupé
    }

    public void run() {
        try {
            while(true) {
                retirer();
                consommer();
            }
        } catch (Exception ex) {
            //System.err.println(ex);
        }
    }
    
    @Override
    public int nombreDeMessages() {
        return this.nb_messages;
    }
    
    /**
     * 
     * @return true si le consommateur est en train de traiter un message (et donc que son exécution ne peut pas être terminée)
     */
    public boolean traitement() {
        return enTraitement;
    }
    
}
