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

/**
 *
 * @author mohit
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Fork(50)
public class Volatiles {

    private int plainV;
    private volatile int volatileV;
    private AtomicInteger ai = new AtomicInteger();

    @Benchmark
    public int baseline() {
        return 42;
    }

    @Benchmark
    public int incrPlain() {
        return plainV++;
    }

    @Benchmark
    public int incrVolatile() {
        return volatileV++;
    }

    @Benchmark
    public int incrAtomic() {
        return ai.incrementAndGet();
    }

    @Benchmark
    public long testcmpcxchg() {
        for (;;) {
            int current = ai.get();
            int next = current + 1;
            if (ai.compareAndSet(current, next)) {
                return next;
            }
        }
    }

}
