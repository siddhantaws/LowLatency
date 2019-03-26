/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mohit
 */
public class LockTest {
    public static void main(String[] args){}
    
    
    static class Test{
        public synchronized static void m1(){
            for (int i = 0; i < 20; i++) {
                System.out.println("Test.m1"+Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LockTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    
        public static void m2(){
            for (int i = 0; i < 20; i++) {
                System.out.println("Test.m2"+Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LockTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        public synchronized void m3(){
            for (int i = 0; i < 20; i++) {
                System.out.println("Test.m3"+Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LockTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        public void m4(){
            for (int i = 0; i < 20; i++) {
                System.out.println("Test.m4"+Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LockTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        public synchronized static void m5(){
            for (int i = 0; i < 20; i++) {
                System.out.println("Test.m5"+Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LockTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        public synchronized void m6(){
            for (int i = 0; i < 20; i++) {
                System.out.println("Test.m6"+Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LockTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
