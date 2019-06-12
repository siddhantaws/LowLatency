/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.executor.custom;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static net.openhft.affinity.AffinityStrategies.ANY;
import static net.openhft.affinity.AffinityStrategies.DIFFERENT_SOCKET;
import static net.openhft.affinity.AffinityStrategies.SAME_CORE;
import net.openhft.affinity.AffinityThreadFactory;

/**
 *
 * @author mohit
 */
public class ThreadAffinityExecutorService implements ExecutorService{
    private final ExecutorService ES;
    
    private static final int NUM_THREADS=Integer.getInteger("jmh.executor.threads", 2);
    
    public ThreadAffinityExecutorService() {
        
        this(NUM_THREADS,"atp");
        System.out.println("");
    }
    
    public ThreadAffinityExecutorService(int ts,String name) {
        System.out.println("");
        ES=Executors.newFixedThreadPool(ts,
            new AffinityThreadFactory
        (name, SAME_CORE, DIFFERENT_SOCKET, ANY));
    }

    @Override
    public void shutdown() {
        ES.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return ES.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return ES.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return ES.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return ES.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return ES.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return ES.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return ES.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return ES.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return ES.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return ES.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return ES.invokeAny(tasks, timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        ES.execute(command);
    }

    @Override
    public int hashCode() {
        return ES.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return ES.equals(obj);
    }

    @Override
    public String toString() {
        return ES.toString();
    }

   

    
}
