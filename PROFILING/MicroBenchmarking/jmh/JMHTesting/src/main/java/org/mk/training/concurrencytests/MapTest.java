/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.concurrencytests;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
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
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MapTest {

    private Map<Integer, Integer> map;

    @Param({"hashmap", "treemap"})
    private String type;

    private int begin;
    private int end;

    @Setup
    public void setup() {
        if (type.equals("hashmap")) {
            map = new HashMap<Integer, Integer>();
        } else if (type.equals("treemap")) {
            map = new TreeMap<Integer, Integer>();
        } else {
            throw new IllegalStateException("Unknown type: " + type);
        }
        begin = 1;
        end = 256;
        for (int i = begin; i < end; i++) {
            map.put(i, i);
        }
    }

    @Benchmark
    public void test(Blackhole bh) {
        for (int i = begin; i < end; i++) {
            bh.consume(map.get(i));
        }
    }
}
