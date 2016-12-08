package jus.poc.prodcons.v3;


import jus.poc.prodcons.Acteur;
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
    
    private final ProdCons tampon;
    private int nb_messages;
    private final int[] tpsTraitement;
    //private MessageX messages;
    private int numero_de_message;
    
    public Producteur(ProdCons tampon,Observateur obs, int nombreMoyenDeProduction, int deviationNombreMoyenDeProduction, int tempsMoyenProduction, int deviationTempsMoyenProduction) throws ControlException{
        super(Acteur.typeProducteur,obs,tempsMoyenProduction,deviationTempsMoyenProduction);
        this.tampon = tampon;
        nb_messages = Aleatoire.valeur(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);
        this.tpsTraitement = Aleatoire.valeurs(nb_messages, tempsMoyenProduction, deviationTempsMoyenProduction);
        numero_de_message = 0;
        //this.messages = new MessageX(this,numero_de_message);
        
        //observateur.newProducteur(this);
    }
    
    /**
     *
     * @throws InterruptedException
     * @throws Exception
     */
    public void produire() throws InterruptedException, Exception{
        Thread.sleep(tpsTraitement[nb_messages-1]); 
        numero_de_message++;
    }
    
    public void deposer() throws InterruptedException, Exception{
        tampon.put(this, new MessageX(this,numero_de_message));
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