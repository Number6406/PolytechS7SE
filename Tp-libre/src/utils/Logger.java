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
    
    /* Logger est un singleton */
    
    private Logger() {
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
        System.out.format("[LOG:%3d|%s] <%s:%2d> production d'un message : (%s) buffer:%d%n", currentId(), dateFormat.format(LocalDateTime.now()), PROD_IDENTIFIER, p.identification(), m.toString(), buffer);
    }
    
    public void consommationLogger(_Consommateur c, Message m, int buffer) {
        System.out.format("[LOG:%3d|%s] <%s:%2d> consommation d'un message : (%s) buffer:%d%n", currentId(), dateFormat.format(LocalDateTime.now()), CONS_IDENTIFIER, c.identification(), m.toString(), buffer);
    }
    
    public void traitementLogger(_Consommateur c, Message m, int time) {
        System.out.format("[LOG:%3d|%s] <%s:%2d> fin traitement du message (%d secondes) : (%s)%n", currentId(), dateFormat.format(LocalDateTime.now()), CONS_IDENTIFIER, c.identification(), time, m.toString());
    }
    
    public void infoLogger(String message) {
        System.out.format("[LOG:%3d|%s] <%s> %s%n", currentId(), dateFormat.format(LocalDateTime.now()), INFO_IDENTIFIER, message);
    }
    
}
