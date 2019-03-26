package org.mk.training.logic.queues;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.mk.training.queues.P1C1OffHeapQueue;
import org.mk.training.queues.P1C1Queue2CacheLinesHeapBuffer;
import org.mk.training.queues.P1C1Queue4CacheLinesHeapBuffer;
import org.mk.training.queues.P1C1Queue4CacheLinesHeapBufferUnsafe;

public final class SPSCQueueFactory {

    public static Queue<Integer> createQueue(int qId, int qScale) {
        int qCapacity = 1 << qScale;
        switch (qId) {
            case 0:
                return new ArrayBlockingQueue<Integer>(qCapacity);
            case -1:
                return new ConcurrentLinkedQueue<Integer>();
            case 1:
                return new P1C1QueueOriginal1<Integer>(qCapacity);
            case 12:
                return new P1C1QueueOriginal12<Integer>(qCapacity);
            case 2:
                return new P1C1QueueOriginal2<Integer>(qCapacity);
            case 21:
                return new P1C1QueueOriginal21<Integer>(qCapacity);
            case 22:
                return new P1C1QueueOriginal22<Integer>(qCapacity);
            case 23:
                return new P1C1QueueOriginal23<Integer>(qCapacity);
            case 3:
                return new P1C1QueueOriginal3<Integer>(qCapacity);
            case 4:
                return new P1C1Queue2CacheLinesHeapBuffer<Integer>(qCapacity);
            case 5:
                return new P1C1Queue4CacheLinesHeapBuffer<Integer>(qCapacity);
            case 6:
                return new P1C1Queue4CacheLinesHeapBufferUnsafe<Integer>(
                        qCapacity);
            case 7:
                return new P1C1OffHeapQueue(qCapacity);
            case 8:
                return new P1C1QueueOriginalPrimitive(qCapacity);
            case 9:
                return new FFBuffer<Integer>(qScale,Integer.getInteger("sparse.shift", 2));
            case 94:
                return new FFBufferOrdered1<Integer>(qCapacity);
            /*case 95:
             return new FFBufferOrdered2<Integer>(qCapacity);
             case 96:
             return new FFBufferOrdered3<Integer>(qCapacity);
             case 961:
             return new FFBufferOrdered31<Integer>(qCapacity,Integer.getInteger("sparse.shift", 2));   
             case 962:
             return new FFBufferOrdered32<Integer>(qCapacity);   
             */
            default:
                throw new IllegalArgumentException("Invalid option: " + qId);
        }
    }
}
