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
    
    private Lock l;
    private int val_init;
    private int nb_ressources;
    private final List<Thread> file;
    
    public MonSemaphore(int nb_ressources){
        System.err.println("jus.poc.prodcons.v2.MonSemaphore.<init>() : nb_ressources = "+nb_ressources);
        this.nb_ressources = nb_ressources;
        this.val_init = nb_ressources;
        this.file = new LinkedList<>();
        this.l = new ReentrantLock();
    }
    
       
    public void P(){
        l.lock();
        try {
            System.err.println("jus.poc.prodcons.v2.MonSemaphore.P() de val init " + val_init + " : j'ai "+nb_ressources+" ressources");
            nb_ressources--;
            if(nb_ressources < 0){
                System.out.println("jus.poc.prodcons.v2.MonSemaphore.P() : current Thread : " + Thread.currentThread().toString());
                file.add(Thread.currentThread());
                l.unlock();
                Thread.currentThread().suspend();
                l.lock();
            }
        } finally {
            l.unlock();
        }
    }
    
    public void V(){
        l.lock();
        try {
            System.err.println("jus.poc.prodcons.v2.MonSemaphore.V() de val init " + val_init + " : j'ai "+nb_ressources+" ressources");
            nb_ressources++;
            if(nb_ressources <= 0){
                Thread p = file.get(0);
                p.resume();
            }
        } finally {
            l.unlock();
        }
    }
    
    
}
