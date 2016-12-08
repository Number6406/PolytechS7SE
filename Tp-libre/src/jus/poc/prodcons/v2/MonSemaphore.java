/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jus.poc.prodcons.v2;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author alicia
 */
public class MonSemaphore {
    
    private int nb_ressources;
    
    public MonSemaphore(int nb_ressources){
        System.err.println("jus.poc.prodcons.v2.MonSemaphore.<init>() : nb_ressources = "+nb_ressources);
        this.nb_ressources = nb_ressources;
    }
    
    public synchronized void P() throws InterruptedException{
        if(nb_ressources <= 0){
            wait();
        }
        nb_ressources--;
    }
    
    public synchronized void V(){
        System.err.println("jus.poc.prodcons.v2.MonSemaphore.V() : j'ai "+nb_ressources+" ressources");
        if((++nb_ressources)>0) {
            notify();
        }
    }
    
    
}
