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
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
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
@Warmup(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MultiThreadedHashMapPut {

    private Map<Integer, Integer> map;

    @Param({"hashmap","synchashmap", "chm","nbhm"})
    private String type;

    private int begin;
    private int end;
    private ThreadLocalRandom tlr;

    @Setup
    public void setup() {
        begin = 1;
        end = 100000;
        tlr=ThreadLocalRandom.current();
        if (type.equals("hashmap")) {
            /**
             * Unsafe but as a base line.
             */
            map = new HashMap<Integer, Integer>(end);
        } else if (type.equals("synchashmap")) {
            map = Collections.synchronizedMap(new HashMap<Integer, Integer>(end));
        } else if (type.equals("chm")) {
            map = new ConcurrentHashMap<Integer, Integer>(end);
        } else if (type.equals("nbhm")) {
            map = new NonBlockingHashMap<Integer, Integer>(end);
        }
        else {
            throw new IllegalStateException("Unknown type: " + type);
        }
        
        /*for (int i = begin; i < end; i++) {
            map.put(i, i);
        }*/
    }

    
    
    @Benchmark
    @Group("g17")
    @GroupThreads(1)
    public void put17(Blackhole bh) {
        int kv=tlr.nextInt(end);
        bh.consume(map.put(kv,kv));
    }
    @Benchmark
    @Group("g17")
    @GroupThreads(7)
    public void get17(Blackhole bh) {
        bh.consume(map.get(tlr.nextInt(end)));
    }
    
    @Benchmark
    @Group("g26")
    @GroupThreads(2)
    public void put26(Blackhole bh) {
        int kv=tlr.nextInt(end);
        bh.consume(map.put(kv,kv));
    }
    @Benchmark
    @Group("g26")
    @GroupThreads(6)
    public void get26(Blackhole bh) {
        bh.consume(map.get(tlr.nextInt(end)));
    }
    
    @Benchmark
    @Group("g35")
    @GroupThreads(3)
    public void put35(Blackhole bh) {
        int kv=tlr.nextInt(end);
        bh.consume(map.put(kv,kv));
    }
    @Benchmark
    @Group("g35")
    @GroupThreads(5)
    public void get35(Blackhole bh) {
        bh.consume(map.get(tlr.nextInt(end)));
    }
    
    @Benchmark
    @Group("g44")
    @GroupThreads(4)
    public void put44(Blackhole bh) {
        int kv=tlr.nextInt(end);
        bh.consume(map.put(kv,kv));
    }
    @Benchmark
    @Group("g44")
    @GroupThreads(4)
    public void get44(Blackhole bh) {
        bh.consume(map.get(tlr.nextInt(end)));
    }
    
    @Benchmark
    @Group("g53")
    @GroupThreads(5)
    public void put53(Blackhole bh) {
        int kv=tlr.nextInt(end);
        bh.consume(map.put(kv,kv));
    }
    @Benchmark
    @Group("g53")
    @GroupThreads(3)
    public void get53(Blackhole bh) {
        bh.consume(map.get(tlr.nextInt(end)));
    }
    
    @Benchmark
    @Group("g62")
    @GroupThreads(6)
    public void put62(Blackhole bh) {
        int kv=tlr.nextInt(end);
        bh.consume(map.put(kv,kv));
    }
    @Benchmark
    @Group("g62")
    @GroupThreads(2)
    public void get62(Blackhole bh) {
        bh.consume(map.get(tlr.nextInt(end)));
    }
    
    @Benchmark
    @Group("g71")
    @GroupThreads(7)
    public void put71(Blackhole bh) {
        int kv=tlr.nextInt(end);
        bh.consume(map.put(kv,kv));
    }
    @Benchmark
    @Group("g71")
    @GroupThreads(1)
    public void get71(Blackhole bh) {
        bh.consume(map.get(tlr.nextInt(end)));
    }
}
