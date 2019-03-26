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

public class Branches {

    final static int COUNT = 64 * 1024;
    static Random random = new Random(System.currentTimeMillis());

    static int[] createData(int count, boolean warmup, boolean predict) {
        int[] data = new int[count];
        for (int i = 0; i < count; i++) {
            data[i] = warmup ? random.nextInt(2)
                    : (predict ? 1 : random.nextInt(2));
        }
        return data;
    }

    static int benchmark2(int[] data){
        int sum = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 1) {
                sum += i;
            }
        }
        return sum;
    }
    
    private static int benchCondLoop(int[] data) {       
        HWCounters.start();
        int sum=benchmark(data);
        HWCounters.stop();
        return sum;
    }
    
    static int benchmark(int[] data){
        int sum = 0;
        long ms = System.currentTimeMillis();
        for (int i = 0; i < data.length; i++) {
            if (i + ms > 0 && data[i] == 1) {
                sum += i;
            }
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        boolean predictable = Boolean.parseBoolean(args[0]);
        boolean sort=false;
        if(args.length==2){
         sort= Boolean.parseBoolean(args[1]);
        }
        HWCounters.init();
        int count = 0;

        for (int i = 0; i < 10000; i++) {
            int[] data = createData(1024, false, predictable);
            count += benchCondLoop(data);
        }
        System.out.println("warmup done");
        
        Thread.sleep(1000);
        int[] data = createData(512 * 1024, false, predictable);
        if(sort)
            Arrays.sort(data);
        count += benchCondLoop(data);
        HWCounters.printResults();
        System.out.println(count);
        HWCounters.shutdown();
    }
}
