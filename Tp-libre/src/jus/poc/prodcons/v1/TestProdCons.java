package jus.poc.prodcons.v1;


import java.io.IOException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;
import utils.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bonhourg
 */
public class TestProdCons extends Simulateur {
    
    protected static int debug;
    protected static int nbProd;
    protected static int nbCons;
    protected static int nbBuffer;
    protected static int tempsMoyenProduction;
    protected static int deviationTempsMoyenProduction;
    protected static int tempsMoyenConsommation;
    protected static int deviationTempsMoyenConsommation;
    protected static int nombreMoyenDeProduction;
    protected static int deviationNombreMoyenDeProduction;
    protected static int nombreMoyenNbExemplaire;
    protected static int deviationNombreMoyenNbExemplaire;
    
    protected static ProdCons prodCons;
    protected static Producteur[] producteurs;
    protected static Consommateur[] consommateurs;

    public TestProdCons(Observateur observateur) {
        super(observateur);
    }

    @Override
    protected void run() throws Exception {
        init("test3.xml");
        
        prodCons = new ProdCons(nbBuffer);
        producteurs = new Producteur[nbProd];
        consommateurs = new Consommateur[nbCons];
        
        for(int pi = 0; pi < nbProd; pi ++) {
            producteurs[pi] = new Producteur(prodCons, observateur, nombreMoyenDeProduction, deviationNombreMoyenDeProduction, tempsMoyenProduction, deviationTempsMoyenProduction);
            producteurs[pi].start();
        }
        
        for(int ci = 0; ci < nbCons; ci++) {
            consommateurs[ci] = new Consommateur(prodCons, observateur, tempsMoyenConsommation, deviationTempsMoyenConsommation);
            consommateurs[ci].start();
        }
        
        /* Attente de la fin de production */
        for (int pi = 0; pi < nbProd; pi ++) {
            producteurs[pi].join();
        }
        
        Logger.getInstance().infoLogger("Fin de la production");
        
        /* Attente de la fin de consommation de tous les messages */
        do {
            Thread.yield();
        } while (prodCons.enAttente() > 0);
        
        /* Fin des consommateurs */
        for(int ci = 0; ci < nbCons; ci++) {
            while(consommateurs[ci].traitement()) {
                Thread.sleep(1000);
                //System.out.println("J'attends la fin du traitement");
            }
            //System.out.println("C["+ci+"] Ne traite plus");
            consommateurs[ci].interrupt();
        }
        
        Logger.getInstance().infoLogger("Fin de la consommation (et du programme)");
        
    }
    
    public static void main(String[] args){
        new TestProdCons(new Observateur()).start();
    }
    
    /**
    * Retreave the parameters of the application.
    * @param file the final name of the file containing the options.
    */
    protected static void init(String file) throws IOException {     
        //Version 1. Voir V2 pour améliorer
        
        final class Properties extends java.util.Properties {
        private static final long serialVersionUID = 1L;
        public int get(String key){return Integer.parseInt(getProperty(key));}
        public Properties(String file) {
            try{
                loadFromXML(ClassLoader.getSystemResourceAsStream(file));
            } catch(Exception e){
                e.printStackTrace();}
            }
        }
        Properties option = new Properties("jus/poc/prodcons/options/"+file);
        
        debug = Integer.parseInt(option.getProperty("debug"));
        nbProd = Integer.parseInt(option.getProperty("nbProd"));
        nbCons = Integer.parseInt(option.getProperty("nbCons"));
        nbBuffer = Integer.parseInt(option.getProperty("nbBuffer"));
        tempsMoyenProduction = Integer.parseInt(option.getProperty("tempsMoyenProduction"));
        deviationTempsMoyenProduction = Integer.parseInt(option.getProperty("deviationTempsMoyenProduction"));
        tempsMoyenConsommation = Integer.parseInt(option.getProperty("tempsMoyenConsommation"));
        deviationTempsMoyenConsommation = Integer.parseInt(option.getProperty("deviationTempsMoyenConsommation"));
        nombreMoyenDeProduction = Integer.parseInt(option.getProperty("nombreMoyenDeProduction"));
        deviationNombreMoyenDeProduction = Integer.parseInt(option.getProperty("deviationNombreMoyenDeProduction"));
        nombreMoyenNbExemplaire = Integer.parseInt(option.getProperty("nombreMoyenNbExemplaire"));
        deviationNombreMoyenNbExemplaire = Integer.parseInt(option.getProperty("deviationNombreMoyenNbExemplaire"));
    
        Logger.getInstance().setDebug(debug);
    }
    
}
