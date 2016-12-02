package jus.poc.prodcons.v1;


import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.ControlException;
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
    
    public Consommateur(ProdCons tampon, Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement) throws ControlException {
        super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
        this.tampon = tampon;
        this.nb_messages = 0;
    }
    
    @Override
    public int nombreDeMessages() {
        return this.nb_messages;
    }
    
}
