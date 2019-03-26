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
import ch.usi.overseer.OverAgent;
import ch.usi.overseer.OverHpc;
import java.util.concurrent.atomic.AtomicInteger;

public class HPCIntro {

    private static String[] EVENTS = {
        "UNHALTED_CORE_CYCLES",
        "INSTRUCTION_RETIRED",
        "LLC_REFERENCES",
        
"LLC_MISSES",
/*"LLC-LOADS",*/
"LLC-LOAD-MISSES",
            "LLC-STORES",
"LLC-STORE-MISSES",
            "PERF_COUNT_SW_CPU_MIGRATIONS",
            "CPU-MIGRATIONS"


    /*    "L2_RQSTS:LD_MISS",
        "LLC_REFERENCES",
        "MEM_LOAD_RETIRED:LLC_MISS",
        "PERF_COUNT_SW_CPU_MIGRATIONS",
        "MEM_UNCORE_RETIRED:LOCAL_DRAM_AND_REMOTE_CACHE_HIT",
        "MEM_UNCORE_RETIRED:REMOTE_DRAM"*/
    };

    private static String[] EVENTS_NAME = {
        "Cycles",
        "Instructions",
        "LLC references",
        "LLC misses",
        /*"LLC loads",*/
        "LLC load misses",
           "LLC stores",
        "LLC store misses",
        "CPU migrations",
        "CPU migrations"
       /* "L2 misses",
        "LLC hits",
        "LLC misses",
        "CPU migrations",
        "Local DRAM",
        "Remote DRAM"*/
    };

    private static long[] results = new long[EVENTS.length];
    private static OverHpc oHpc = OverHpc.getInstance();

    static int counter;
    static AtomicInteger atomicCounter = new AtomicInteger();

    static void stdIncrement() {
        counter++;
    }

    static void atomicIncrement() {
        atomicCounter.incrementAndGet();
    }

    static void benchStdIncrement(int loopCount) {
        for (int i = 0; i < loopCount; i++) {
            stdIncrement();
        }
    }

    static void benchAtomicIncrement(int loopCount) {
        for (int i = 0; i < loopCount; i++) {
            atomicIncrement();
        }
    }

    public static void main(String[] args) throws Exception {

        if (OverAgent.isRunning()) {
            System.out.println("   OverAgent is running");
        } else {
            System.out.println("   OverAgent is not running (check your JVM settings)");
            return;
        }
        boolean std = args.length > 0 && args[0].equals("std");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < EVENTS.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(EVENTS[i]);
        }
        oHpc.initEvents(sb.toString());
        // warmup
        if (std) {
            benchStdIncrement(10000);
            benchStdIncrement(10000);
        } else {
            benchAtomicIncrement(10000);
            benchAtomicIncrement(10000);
        }
        Thread.sleep(1000);
        System.out.println("warmup done");

        int tid = oHpc.getThreadId();
        System.out.println("" + tid);
        oHpc.bindEventsToThread(tid);
        // bench
        //long previousValue = oHpc.getEventFromThread(tid, 0);
        if (std) {
            benchStdIncrement(5 * 1000 * 1000);
        } else {
            benchAtomicIncrement(5 * 1000 * 1000);
        }

        for (int i = 0; i < EVENTS.length; i++) {
            results[i] = oHpc.getEventFromThread(tid, i);
        }
        for (int i = 0; i < EVENTS.length; i++) {
            System.out.println(EVENTS_NAME[i] + ": " + String.format("%,d", results[i]));
        }
        OverHpc.shutdown();
    }

}
