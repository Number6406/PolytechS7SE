package jus.poc.prodcons.v1;


import java.util.logging.Level;
import java.util.logging.Logger;
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

    private ProdCons tampon;
    private int nb_messages;
    private Message messages;
    private int tpsTraitement;
    private boolean enTraitement = false;
    
    public Consommateur(ProdCons tampon, Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement) throws ControlException {
        super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
        this.tampon = tampon;
        this.nb_messages = 0;
        
        //observateur.newConsommateur(this);
    }
    
    public void consommer() throws InterruptedException, Exception {
        messages = tampon.get(this);
        nb_messages++;
    }

    public void traiter() throws InterruptedException {
        enTraitement = true;
        tpsTraitement = Aleatoire.valeur(moyenneTempsDeTraitement(), deviationTempsDeTraitement());
        Thread.sleep(tpsTraitement);
        enTraitement = false;
    }

    public void run() {
        try {
            while(true) {
                consommer();
                traiter();
            }
        } catch (Exception ex) {
            Logger.getLogger(Consommateur.class.getName()).log(Level.SEVERE, null, ex);
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
