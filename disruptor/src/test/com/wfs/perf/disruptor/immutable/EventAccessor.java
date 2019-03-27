package com.wfs.perf.disruptor.immutable;

public interface EventAccessor<T>
{
    T take(long sequence);
}
