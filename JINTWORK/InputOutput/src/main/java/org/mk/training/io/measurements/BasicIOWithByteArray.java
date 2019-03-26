/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.io.measurements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.mk.training.util.Utilities;

/**
 *
 * @author mohit
 */
public class BasicIOWithByteArray {
    private static final long ITERATIONS=100000;
    public static void main(String... args) throws IOException{
        long iter=ITERATIONS;
        
        if(args.length!=0){
            try{
                iter=Long.parseLong(args[0]);
            }catch (NumberFormatException nfe){
                //ignore
            }
        }
        String text="abcdefghij";
        byte [] data=text.getBytes();
        String loc=Utilities.getTmpStoreLocation();
        FileOutputStream fos=new FileOutputStream(loc+ File.separatorChar+"biowba.tmp");
        for (int i = 0; i < iter; i++) {
            fos.write(data,0,10);
        }
        fos.close();
    }
}
