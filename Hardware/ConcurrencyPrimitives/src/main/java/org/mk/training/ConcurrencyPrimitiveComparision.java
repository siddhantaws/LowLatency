/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author mohit
 */
public class ConcurrencyPrimitiveComparision {

    private static long nanopermili=1000000;
    private static long iterations=500000000L;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        long start=System.nanoTime();
        increment(iterations);
        long end=System.nanoTime();
        int millis=(int) ((end-start)/nanopermili);
        System.out.println("Plain:end-start:"+millis);
        start=System.nanoTime();
        incrementVolatile(iterations);
        end=System.nanoTime();
        int millisvol=(int) ((end-start)/nanopermili);
        System.out.println("Volatile:end-start:"+millisvol+" "+((double)millisvol/millis)+" times worse:");
        start=System.nanoTime();
        incrementAtomic(iterations);
        end=System.nanoTime();
        int millisatomic=(int) ((end-start)/nanopermili);
        System.out.println("Atomic:end-start:"+millisatomic+" "+((double)millisatomic/millis)+" times worse:");
        start=System.nanoTime();
        incrementLock(iterations);
        end=System.nanoTime();
        int millislock=(int) ((end-start)/nanopermili);
        System.out.println("Lock:end-start:"+millislock+" "+((double)millislock/millis)+" times worse:");
        
        Thread t1=new WorkerThreadAtomic(iterations);
        Thread t2=new WorkerThreadAtomic(iterations);
        start=System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        end=System.nanoTime();
        int millisatomic2=(int) ((end-start)/nanopermili);
        System.out.println("Atomic2:end-start:"+millisatomic2+" "+((double)millisatomic2/millis)+" times worse:");
        
        t1=new WorkerThreadLock(iterations);
        t2=new WorkerThreadLock(iterations);
        start=System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        end=System.nanoTime();
        int millislock2=(int) ((end-start)/nanopermili);
        System.out.println("Lock:end-start:"+millislock2+" "+((double)millislock2/millis)+" times worse:");
        
        
        /*
        System.out.println("vol:"+millisvol/millis);
        System.out.println("atomic:"+millisatomic/millis);
        System.out.println("lock:"+millislock/millis);
        System.out.println("atomic2:"+millisatomic2/millis);
        System.out.println("lock2:"+millislock2/millis);*/
    }
    static long foo  = 0;
    static volatile long foovol  = 0;
    static AtomicInteger ai=new AtomicInteger();
    static long foolock=0;
    static Lock lock=new ReentrantLock();
    
    private static void increment(long iterations) {
        for (long l = 0; l < iterations; l++) {
            foo++;
        }
    }
    
    private static void incrementVolatile(long iterations) {
        for (long l = 0; l < iterations; l++) {
            foovol++;
        }
    }
    
    private static void incrementAtomic(long iterations) {
        for (long l = 0; l < iterations; l++) {
            ai.getAndDecrement();
        }
    }
    
    private static void incrementLock(long iterations) {
        for (long l = 0; l < iterations; l++) {
            try{
                lock.lock();
                foolock++;
            }
            finally{
                lock.unlock();
            }
        }
    }
    
    private static class WorkerThreadAtomic extends Thread{
        long iterations;

        public WorkerThreadAtomic(long iterations) {
            this.iterations = iterations;
        }
        
        
        @Override
        public void run() {
            ConcurrencyPrimitiveComparision.incrementAtomic(iterations);
        }
        
    }
    
    private static class WorkerThreadLock extends Thread{
        long iterations;

        public WorkerThreadLock(long iterations) {
            this.iterations = iterations;
        }
        
        
        @Override
        public void run() {
            ConcurrencyPrimitiveComparision.incrementLock(iterations);
        }
        
    }
    
}
