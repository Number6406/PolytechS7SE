package jus.poc.prodcons.v1;


import java.util.logging.Level;
import java.util.logging.Logger;
import jus.poc.prodcons.Acteur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;

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
    
    private final ProdCons tampon;
    private int nb_messages;
    private final int[] tpsTraitement;
    private MessageX messages;
    
    public Producteur(ProdCons tampon,Observateur obs, int nombreMoyenDeProduction, int deviationNombreMoyenDeProduction, int tempsMoyenProduction, int deviationTempsMoyenProduction) throws ControlException{
        super(Acteur.typeProducteur,obs,tempsMoyenProduction,deviationTempsMoyenProduction);
        this.tampon = tampon;
        nb_messages = Aleatoire.valeur(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);
        this.tpsTraitement = Aleatoire.valeurs(nb_messages, tempsMoyenProduction, deviationTempsMoyenProduction);
        this.messages = new MessageX(this);
        
        //observateur.newProducteur(this);
    }
    
    public synchronized void produire() throws InterruptedException{
        wait(tpsTraitement[nb_messages]);
        nb_messages--;
        messages.next();
    }
    
    /**
     *
     * @throws InterruptedException
     * @throws Exception
     */
    public void deposer() throws InterruptedException, Exception{
        produire();
        tampon.debutProduction();
        tampon.put(this, messages);
        tampon.finProduction();
    }
    
    public void run() {
        // Production de tous les messages
        while(nombreDeMessages() > 0) {
            try {
                produire();
            } catch (Exception ex) {
                Logger.getLogger(Producteur.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public int nombreDeMessages() {
        return nb_messages; //To change body of generated methods, choose Tools | Templates.
    }
    
}
