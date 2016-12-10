/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;


/**
 *
 * @author alicia
 */
public class MonSemaphore {
    
    private int nb_ressources;
    private final int nb_initial_ressources;
    
    public MonSemaphore(int nb_ressources){
        Logger.getInstance().semaphoreinitLogger("Initialisation à " + nb_ressources + " ressources.");
        this.nb_ressources = nb_ressources;
        this.nb_initial_ressources = this.nb_ressources;
    }
    
    public synchronized void P() throws InterruptedException{
        Logger.getInstance().semaphorePLogger("<"+ nb_initial_ressources +"> nb_ressources = " + nb_ressources);
        if((--nb_ressources) < 0){
            wait();
        }
    }
    
    public synchronized void V(){
        Logger.getInstance().semaphoreVLogger("<"+ nb_initial_ressources +"> nb_ressources = " + nb_ressources);
        if((++nb_ressources)<=0) {
            notify();
        }
    }
    
    
}
