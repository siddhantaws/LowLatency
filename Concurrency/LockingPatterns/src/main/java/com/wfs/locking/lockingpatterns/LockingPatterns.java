/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfs.locking.lockingpatterns;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author StillWaterrz
 */
public class LockingPatterns {
    static LockFreeList<String> l=new LockFreeList();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());
        Remover remover1=new Remover("D","One");
        remover1.start();
        Remover remover2=new Remover("B","TWO");
        remover2.start();
        l.add("A");
        l.add("B");
        l.add("D");
        l.add("E");
        //l.remove("C");
        System.out.println(l);
        remover1.notifyPrinter();
        print(500);
        //remover2.notifyPrinter();
        l.add("C");
        print(500);
        print(6000);
    }
    static void print(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(LockingPatterns.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(l);
    }
    static class Remover extends Thread{

        private String toremove;

        public Remover(String toremove, String name) {
            super(name);
            this.toremove = toremove;
        }
        @Override
        synchronized public void run() {
            while (true) {                
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(LockingPatterns.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(Thread.currentThread().getName()+":l.remove():"+l.remove(toremove));
            }
        }
        
        synchronized void notifyPrinter(){
            /*try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(LockingPatterns.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            this.notify();
        }
    }
    
}
