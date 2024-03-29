/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jus.poc.prodcons.Message;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

/**
 *
 * @author bonhourg
 */
public class Logger {
    
    DateTimeFormatter dateFormat;
    int id_log = 0;
    private static final String PROD_IDENTIFIER = "PROD";
    private static final String CONS_IDENTIFIER = "CONS";
    private static final String INFO_IDENTIFIER = "INFO";
    private static int debug = 0;
    
    /* Logger est un singleton */
    
    private Logger(){
        dateFormat = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");        
    }
    
    private static Logger INSTANCE = new Logger();
    
    public static Logger getInstance() {
        return INSTANCE;
    }
    
    private int currentId() {
        return ++id_log;
    }
    
    /* Partie des gestion des messages */
    
    public void productionLogger(_Producteur p, Message m, int buffer) {
        if(debug>=1)System.out.format("[LOG:%3d|%s] <%s:%2d> production   : (%s) buffer:%d%n", currentId(), dateFormat.format(LocalDateTime.now()), PROD_IDENTIFIER, p.identification(), m.toString(), buffer);
    }
    
    public void productionLogger(_Producteur p, Message m, int buffer, int nb_exemplaires) {
        if(debug>=1)System.out.format("[LOG:%3d|%s] <%s:%2d> production   : (%s) buffer:%d en %d exemplaires%n", currentId(), dateFormat.format(LocalDateTime.now()), PROD_IDENTIFIER, p.identification(), m.toString(), buffer,nb_exemplaires);
    }
    
    public void consommationLogger(_Consommateur c, Message m, int buffer) {
        if(debug>=1)System.out.format("[LOG:%3d|%s] <%s:%2d> consommation : (%s) buffer:%d%n", currentId(), dateFormat.format(LocalDateTime.now()), CONS_IDENTIFIER, c.identification(), m.toString(), buffer);
    }
    
    public void traitementLogger(_Consommateur c, Message m, int time) {
        if(debug>=1)System.out.format("[LOG:%3d|%s] <%s:%2d> fin traitement du message (%d secondes) : (%s)%n", currentId(), dateFormat.format(LocalDateTime.now()), CONS_IDENTIFIER, c.identification(), time, m.toString());
    }
    
    public void infoLogger(String message) {
        if(debug>=1)System.out.format("[LOG:%3d|%s] <%s> %s%n", currentId(), dateFormat.format(LocalDateTime.now()), INFO_IDENTIFIER, message);
    }
    
    public void semaphoreinitLogger(String message) {
        if(debug>=2)System.out.println("[Semaphore][Init] "+message);
    }
    
    public void semaphorePLogger(String message) {
        if(debug>=2)System.out.println("[Semaphore][P] "+message);
    }
    
    public void semaphoreVLogger(String message) {
        if(debug>=2)System.out.println("[Semaphore][V] "+message);
    }
    
    public void messageRetirerLogger(String message) {
        if(debug>=3)System.out.println("[MessageX][retirer] " + message);
    }
    
    public void autreLogger(String message){
        if(debug >=4)System.out.println("[AUTRE] "+ message);
    }
    
    public void setDebug(int debug){
        this.debug = debug;
    }
    
}
