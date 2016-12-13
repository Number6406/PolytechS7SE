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
    
    public Consommateur(ProdCons tampon, Observateur observateur, MonObservateur mon_observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement) throws ControlException {
        super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
        this.tampon = tampon;
        this.nb_messages = 0;
        this.mon_observateur = mon_observateur;
    }
    
    public void retirer() throws InterruptedException, Exception {
        message = tampon.get(this);
        observateur.retraitMessage(this, message);
        mon_observateur.retraitMessage(this, message);
        nb_messages++;
    }

    public void consommer() throws InterruptedException, ControlException {
        enTraitement = true;
        tpsTraitement = Aleatoire.valeur(moyenneTempsDeTraitement(), deviationTempsDeTraitement());
        Thread.sleep(tpsTraitement*1000); // Pour être en secondes
        observateur.consommationMessage(this, message, tpsTraitement);
        mon_observateur.traitementMessage(this, message);
        Logger.getInstance().traitementLogger(this, message, tpsTraitement);
        enTraitement = false;
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
    
    public boolean traitement() {
        return enTraitement;
    }
    
}
