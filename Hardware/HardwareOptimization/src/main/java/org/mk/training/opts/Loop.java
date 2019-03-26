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
public class Loop {
    public static void main(String[] args) {
        for (int i = 0; i < 1000000; i++) {
            test();
        }
    }
    
    public static void test(){
       
        int [] array=new int[1000];
        for (int i = 0; i < 1000; i++) {
            array[i]=i;
        }
        int total=0;
        for (int i = 0; i < 1000; i++) {
            total+=array[i];
        }
    }
}
