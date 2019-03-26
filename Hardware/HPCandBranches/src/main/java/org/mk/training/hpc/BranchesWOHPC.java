/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.hpc;

/**
 *
 * @author mohit
 */
import java.util.Random;
import java.util.Arrays;
import static org.mk.training.hpc.Branches.benchmark;
import static org.mk.training.hpc.Branches.createData;

public class BranchesWOHPC {

    private static int benchCondLoop(int[] data) {       
        
        int sum=benchmark(data);
        
        return sum;
    }

    public static void main(String[] args) throws Exception {
        boolean predictable = Boolean.parseBoolean(args[0]);
        boolean sort=false;
        if(args.length==2){
         sort= Boolean.parseBoolean(args[1]);
        }
        
        int count = 0;

        for (int i = 0; i < 100000; i++) {
            int[] data = createData(1024, false, predictable);
            count += benchCondLoop(data);
        }
        System.out.println("warmup done");
        
        Thread.sleep(1000);
        int[] data = createData(512 * 1024, false, predictable);
        if(sort)
            Arrays.sort(data);
        count += benchCondLoop(data);
        
        System.out.println(count);
        
    }
}
