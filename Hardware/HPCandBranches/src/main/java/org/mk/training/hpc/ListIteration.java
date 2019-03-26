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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ch.usi.overseer.OverHpc;

public class ListIteration {

    private static List<String> arrayList = new ArrayList<>();
    private static List<String> linkedList = new LinkedList<>();

    public static void initializeList(List<String> list, int bufferSize) {
        for (int i = 0; i < 50000; i++) {
            byte[] buffer = null;
            if (bufferSize > 0) {
                buffer = new byte[bufferSize];
            }
            String s = String.valueOf(i);
            list.add(s);
            // avoid buffer to be optimized away
            if (System.currentTimeMillis() == 0) {
                System.out.println(buffer);
            }
        }
    }

    public static void bench(List<String> list) {
        if (list.contains("bar")) {
            System.out.println("bar found");
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("\"array\".equals(args[0]) ? arrayList : linkedList buffersize");
            System.exit(0);
        }
        
        List<String> benchList = "array".equals(args[0]) ? arrayList : linkedList;
        int bufferSize = Integer.parseInt(args[1]);
        initializeList(benchList, bufferSize);
        HWCounters.init(EVENTS, EVENTS_NAME);
        System.out.println("init done");
        // warmup
        for (int i = 0; i < 10000; i++) {
            bench(benchList);
        }
        Thread.sleep(1000);
        System.out.println("warmup done");

        HWCounters.start();
        for (int i = 0; i < 1000; i++) {
            bench(benchList);
        }
        HWCounters.stop();
        HWCounters.printResults();
        HWCounters.shutdown();
    }

    private static String[] EVENTS = {
        "UNHALTED_CORE_CYCLES",
        "INSTRUCTION_RETIRED",
        "L1-DCACHE-LOADS",
        "L1-DCACHE-LOAD-MISSES",
        "LLC_REFERENCES",
        "LLC_MISSES",
        "LLC-LOADS",
        "LLC-LOAD-MISSES",
        "PERF_COUNT_SW_CPU_MIGRATIONS",
        "CPU-MIGRATIONS"
   };

    private static String[] EVENTS_NAME = {
        "Cycles",
        "Instructions",
        "L1D-loads",
        "L1D-loads-misses",
        "LLC references",
        "LLC misses",
        "LLC loads",
        "LLC load misses",
        "CPU migrations",
        "CPU migrations"
    };
}
