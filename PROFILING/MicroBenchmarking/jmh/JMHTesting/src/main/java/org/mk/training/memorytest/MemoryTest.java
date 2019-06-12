/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.memorytest;

import java.math.BigInteger;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 *
 * @author mohit
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
public class MemoryTest {

    private static final int ONE_KILO = 1024;
    private static final int LONG_SIZE = 8;
    
    @Param({"7","31"})
    public int mastride;
    
    /*@Param({"1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024",
        "2048", "3072", "4096", "6144", "8192", "16384",
        "32768", "65536", "131072", "262144",
        "524288", "1048576", "2097152"})
    public long memorysize;*/
    
    
    @Param({"1", "2", "4",/* "8", "16", "32", "64", "128"*/})
    public long memorysize;
    //@Param({"1", "3", "7", "15", "31", "63", "127", "255", "513"})
    
    
    
    public final int steps=64 * 1024 * 1024;
    
    long[] array;
    int lengthMod;

    @Setup(Level.Iteration)
    public void setup() {
        long arraysize = memorysize * ONE_KILO / LONG_SIZE;
        array = new long[(int) arraysize];
        lengthMod = array.length - 1;
    }

    @Benchmark
    public void bench() {
        for (int i = 0; i < steps; i++) {
            array[(i * mastride) & lengthMod]++;
        }
        
        //return BigInteger.valueOf(mastride).isProbablePrime((int)memorysize);
    }
}
