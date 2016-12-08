/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jus.poc.prodcons.v2;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author alicia
 */
public class MonSemaphore {
    
    private int nb_ressources;
    private final List<Thread> file;
    
    public MonSemaphore(int nb_ressources){
        this.nb_ressources = nb_ressources;
        this.file = new LinkedList<>();
    }
    
    private void put(Thread t, List<Thread> f){
        f.add(t);
    };
    
    private Thread get(List<Thread> f){
        return f.get(0);
    };
    
    public void P(){
        nb_ressources--;
        if(nb_ressources < 0){
            put(Thread.currentThread(),file);
            Thread.currentThread().suspend();
        }
        
    }
    
    public void L(){
        nb_ressources++;
        if(nb_ressources <= 0){
            Thread p = get(file);
            p.resume();
        }
    }
    
    
}
