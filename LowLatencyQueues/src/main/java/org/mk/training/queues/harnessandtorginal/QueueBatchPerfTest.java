/*
 * Copyright 2012 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mk.training.queues.harnessandtorginal;

import java.util.Queue;

import org.mk.training.queues.spsc9.SPSCQueue9;


public class QueueBatchPerfTest {
	// 15 == 32 * 1024
	public static final int QUEUE_CAPACITY = 1 << Integer.getInteger("scale", 15);
	public static final int REPETITIONS = Integer.getInteger("reps", 50) * 1000 * 1000;
	public static final Integer TEST_VALUE = Integer.valueOf(777);

	public static void main(final String[] args) throws Exception {
		System.out.println("capacity:" + QUEUE_CAPACITY + " reps:" + REPETITIONS);
		final SPSCQueue9<Integer> queue = new SPSCQueue9<Integer>(QUEUE_CAPACITY);

		final long[] results = new long[20];
		for (int i = 0; i < 20; i++) {
			System.gc();
			results[i] = performanceRun(i, queue);
		}
		// only average last 10 results for summary
		long sum = 0;
		for(int i = 10; i < 20; i++){
		    sum+=results[i];
		}
		System.out.format("summary,QueueBatchPerfTest,%s,%d\n", queue.getClass().getSimpleName(), sum/10);
	}

    static Integer result;
    static SPSCQueue9.Processor<Integer> processor = new SPSCQueue9.Processor<Integer>() {
        @Override
        public void process(Integer e) {
            result = e;
        }
    };

	private static long performanceRun(final int runNumber,
	        final SPSCQueue9<Integer> queue) throws Exception {
	    result = null;
		final long start = System.nanoTime();
        final Thread thread = new Thread(new Producer(queue));
        thread.start();
        
		int i = REPETITIONS;
		do {
			i -= queue.pollBatch(processor);
		} while (0 != i);

		thread.join();
		
		final long duration = System.nanoTime() - start;
		final long ops = (REPETITIONS * 1000L * 1000L * 1000L) / duration;
		System.out.format("%d - ops/sec=%,d - %s result=%d\n", Integer
		        .valueOf(runNumber), Long.valueOf(ops), queue.getClass()
		        .getSimpleName(), result);
		return ops;
	}

	public static class Producer implements Runnable {
		private final Queue<Integer> queue;
        
		public Producer(final Queue<Integer> queue) {
			this.queue = queue;
		}

		public void run() {
			int i = REPETITIONS;
			do {
				while (!queue.offer(TEST_VALUE)) {
					Thread.yield();
				}
			} while (0 != --i);
		}
	}
}
