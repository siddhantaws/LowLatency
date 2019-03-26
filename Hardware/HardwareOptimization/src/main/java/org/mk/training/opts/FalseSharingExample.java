/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.opts;

/**
 *
 * @author mohit
 */
public class FalseSharingExample {
    private static final int SPACING=64;
    private static final long ITERATIONS=1000l*1000l*1000l;
    private static final int NPROC=Runtime.getRuntime().availableProcessors();
    private static final short[] shortArray=new short[NPROC*SPACING];
    public static void main(String[] args) {
        Thread [] threads=new Thread[NPROC];
        
        for (int i = 0; i < NPROC-1; i++) {
            threads[i]=new Runner(i);
        }
        
        for (int i = 0; i < NPROC-1; i++) {
            threads[i].start();
        }
    }
    
    final static class Runner extends Thread{
        private final int i;
        

        public Runner(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            for (int j = 0; j < ITERATIONS; j++) {
                shortArray[i]++;
            }
        }
        
    }
    
    
}
