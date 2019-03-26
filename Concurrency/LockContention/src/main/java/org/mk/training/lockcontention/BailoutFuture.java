/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.lockcontention;

/**
 *
 * @author mohit
 */
public class BailoutFuture {

    private double iterationsPerSecond;
    private long recordsAdded, recordsRemoved, nullCounter;

    public BailoutFuture(double iterationsPerSecond, long recordsAdded,
            long recordsRemoved, long nullCounter) {
        this.iterationsPerSecond = iterationsPerSecond;
        this.recordsAdded = recordsAdded;
        this.recordsRemoved = recordsRemoved;
        this.nullCounter = nullCounter;
    }

    public double getIterationsPerSecond() {
        return iterationsPerSecond;
    }

    public long getRecordsAdded() {
        return recordsAdded;
    }

    public long getRecordsRemoved() {
        return recordsRemoved;
    }

    public long getNullCounter() {
        return nullCounter;
    }
}