/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training;

/**
 *
 * @author mohit
 */
public class ThreeDMain3 {
    public static void main(String[] args) {
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                ThreeDPoint p=new ThreeDPoint(2, 2, 2);
        System.out.println("p:"+p);
            }
        }).start();
        
    }
}
