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
public class Loop0 {
    public static void main(String[] args) {
        int size=10000000;
        int [] array=new int[size];
        for (int i = 0; i < size; i++) {
            array[i]=i;
        }
        long total=0;
        for (int i = 0; i < size; i++) {
            total+=array[i];
        }
        System.out.println(total);
    }
 
}
