/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.concurrencytests;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 *
 * @author mohit
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 10000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 10000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class Finals {

    

    @Benchmark
    public void objectCreation(Blackhole bh) {
        bh.consume(new NormalFields(2, 3));
    }

    @Benchmark
    public void finalObjectCreation(Blackhole bh) {
        bh.consume(new FinalFields(2, 3));
    }
    
    @Benchmark
    public void volatileObjectCreation(Blackhole bh) {
        bh.consume(new VolatileFields(2, 3));
    }
    
    static class VolatileFields{
        volatile int x;
        volatile int y;

        public VolatileFields(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
    }
    
    static class FinalFields{
        final int x;
        final int y;

        public FinalFields(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
    }
    
    static class NormalFields{
        int x;
        int y;

        public NormalFields(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
    }
}
