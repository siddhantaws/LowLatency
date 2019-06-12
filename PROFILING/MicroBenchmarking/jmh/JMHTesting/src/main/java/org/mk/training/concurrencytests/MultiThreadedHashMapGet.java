/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.concurrencytests;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 *
 * @author mohit
 */
@State(Scope.Group)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MultiThreadedHashMapGet {

    private Map<Integer, Integer> map;

    @Param({"hashmap","synchashmap", "chm"})
    private String type;

    private int begin;
    private int end;
    private ThreadLocalRandom tlr;

    @Setup
    public void setup() {
        tlr=ThreadLocalRandom.current();
        begin = 1;
        end = 100000;
        if (type.equals("hashmap")) {
            /**
             * Unsafe but as a base line.
             */
            map = new HashMap<Integer, Integer>(end);
        } else if (type.equals("synchashmap")) {
            map = Collections.synchronizedMap(new HashMap<Integer, Integer>(end));
        } else if (type.equals("chm")) {
            map = new ConcurrentHashMap<Integer, Integer>(end);
        } else {
            throw new IllegalStateException("Unknown type: " + type);
        }
        
        for (int i = begin; i < end; i++) {
            map.put(i, i);
        }
    }

    
    @Benchmark
    @Group("g4")
    @GroupThreads(4)
    public void get4(Blackhole bh) {
        bh.consume(map.get(tlr.nextInt(end)));
    }

    @Benchmark
    @Group("g1")
    @GroupThreads(1)
    public void get1(Blackhole bh) {
        bh.consume(map.get(tlr.nextInt(end)));
    }
    
    
    @Benchmark
    @Group("g8")
    @GroupThreads(8)
    public void get8(Blackhole bh) {
        bh.consume(map.get(tlr.nextInt(end)));
    }
}
