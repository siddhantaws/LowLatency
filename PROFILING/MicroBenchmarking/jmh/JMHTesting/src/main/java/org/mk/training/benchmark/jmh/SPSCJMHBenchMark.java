package org.mk.training.benchmark.jmh;

import java.util.Queue;
import java.util.concurrent.TimeUnit;
import net.openhft.affinity.AffinityLock;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

//import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Created by mohit on 3/1/17.
 */


@State(Scope.Group)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
public class SPSCJMHBenchMark {
    public static final Integer TEST_VALUE = Integer.valueOf(777);
    public AffinityLock al;
    volatile boolean preventUnrolling = true;
    @Param(value = { "0"})
    int qType;
    Queue<Integer> q;
    
    @Setup
    public void init(){
        if(q==null){
           // q = SPSCQueueFactory.createQueue(qType, Integer.getInteger("scale", 15));
        }
    }
    
    
    @Benchmark
    @Group("g11")
    @GroupThreads(1)
    public void poll(Blackhole bh){
        bh.consume(q.poll());
    }
    
    @Benchmark
    @Group("g11")
    @GroupThreads(1)
    public void offer(Blackhole bh){
        bh.consume(q.offer(TEST_VALUE));
    }
    @TearDown
    public void close(){
        //al.release();
    }
}
