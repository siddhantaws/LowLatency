package org.mk.training.lockvsnolock;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * @author mohit
 * 
 * The real surprise for me from the results is the performance of ReentrantReadWriteLock.  
 * I cannot see a use for this implementation beyond a case whereby
 * there is a huge balance of reads and very little writes. 
 * My main takeaways are:
 * 1. StampedLock is a major improvement over existing lock implementations especially with 
 * increasing numbers of reader threads.
 * 2. StampedLock has a complex API. It is very easy to mistakenly call the wrong method 
 * for locking actions.
 * 3. Synchronised is a good general purpose lock implementation when contention is from 
 * only 2 threads.
 * 4. ReentrantLock is a good general purpose lock implementation when thread counts grow
 * as previously discovered.
 * 5. Choosing to use ReentrantReadWriteLock should be based on careful and appropriate 
 * measurement. As with all major decisions, measure and make decisions 
 * based on data.
 * 6. Lock-free implementations can offer significant throughput advantages over lock-based 
 * algorithms.
 */

public class PerfTest
{
    private static final long TEST_COOL_OFF_MS = 1000;
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    private static final Point[] SPACESHIPS =
    {
        new SynchronizedPoint(),
        new ReadWriteLockPoint(),
        new ReentrantLockPoint(),
        new StampedLockPoint(),
        new LockFreePoint(),
    };

    private static int NUM_WRITERS;
    private static int NUM_READERS;
    private static long TEST_DURATION_MS;

    public static void main(final String[] args) throws Exception
    {
        NUM_READERS = Integer.parseInt(args[0]);
        NUM_WRITERS = Integer.parseInt(args[1]);
        TEST_DURATION_MS = Long.parseLong(args[2]);

        for (int i = 0; i < 5; i++)
        {
            System.out.println("*** Run - " + i);
            for (final Point SPACESHIP : SPACESHIPS)
            {
                System.gc();
                Thread.sleep(TEST_COOL_OFF_MS);

                perfRun(SPACESHIP);
            }
        }

        EXECUTOR.shutdown();
    }

    public static void perfRun(final Point spaceship) throws Exception
    {
        final Results results = new Results();
        final CyclicBarrier startBarrier = new CyclicBarrier(NUM_READERS + NUM_WRITERS + 1);
        final CountDownLatch finishLatch = new CountDownLatch(NUM_READERS + NUM_WRITERS);
        final AtomicBoolean runningFlag = new AtomicBoolean(true);

        for (int i = 0; i < NUM_WRITERS; i++)
        {
            EXECUTOR.execute(new WriterRunner(i, results, spaceship, runningFlag, startBarrier, finishLatch));
        }

        for (int i = 0; i < NUM_READERS; i++)
        {
            EXECUTOR.execute(new ReaderRunner(i, results, spaceship, runningFlag, startBarrier, finishLatch));
        }

        awaitBarrier(startBarrier);

        Thread.sleep(TEST_DURATION_MS);
        runningFlag.set(false);

        finishLatch.await();

        System.out.format("%d readers %d writers %22s %s\n",
                          NUM_READERS, NUM_WRITERS,
                          spaceship.getClass().getSimpleName(),
                          results);
    }

    public static void awaitBarrier(final CyclicBarrier barrier)
    {
        try
        {
            barrier.await();
        }
        catch (final Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public static class Results
    {
        long[] reads = new long[NUM_READERS];
        long[] moves = new long[NUM_WRITERS];

        long[] readAttempts = new long[NUM_READERS];
        long[] observedMoves = new long[NUM_READERS];
        long[] moveAttempts = new long[NUM_WRITERS];

        @Override
        public String toString()
        {
            long totalReads = 0;
            for (final long v : reads)
            {
                totalReads += v;
            }
            final String readsSummary = String.format("%,d:", totalReads);

            long totalMoves = 0;
            for (final long v : moves)
            {
                totalMoves += v;
            }
            final String movesSummary = String.format("%,d:", totalMoves);

            return
                "reads=" + readsSummary + Arrays.toString(reads) +
                " moves=" + movesSummary + Arrays.toString(moves) +
                " readAttempts=" + Arrays.toString(readAttempts) +
                " moveAttempts=" + Arrays.toString(moveAttempts) +
                " observedMoves=" + Arrays.toString(observedMoves);
        }
    }

    public static class WriterRunner implements Runnable
    {
        private final int id;
        private final Results results;
        private final Point spaceship;
        private final AtomicBoolean runningFlag;
        private final CyclicBarrier barrier;
        private final CountDownLatch latch;

        public WriterRunner(final int id, final Results results, final Point spaceship,
                            final AtomicBoolean runningFlag, final CyclicBarrier barrier, final CountDownLatch latch)
        {
            this.id = id;
            this.results = results;
            this.spaceship = spaceship;
            this.runningFlag = runningFlag;
            this.barrier = barrier;
            this.latch = latch;
        }

        @Override
        public void run()
        {
            awaitBarrier(barrier);

            long movesCounter = 0;
            long movedAttemptsCount = 0;

            while (runningFlag.get())
            {
                movedAttemptsCount += spaceship.move(1, 1);

                ++movesCounter;
            }

            results.moveAttempts[id] = movedAttemptsCount;
            results.moves[id] = movesCounter;

            latch.countDown();
        }
    }

    public static class ReaderRunner implements Runnable
    {
        private final int id;
        private final Results results;
        private final Point spaceship;
        private final AtomicBoolean runningFlag;
        private final CyclicBarrier barrier;
        private final CountDownLatch latch;

        public ReaderRunner(final int id, final Results results, final Point spaceship,
                            final AtomicBoolean runningFlag, final CyclicBarrier barrier, final CountDownLatch latch)
        {
            this.id = id;
            this.results = results;
            this.spaceship = spaceship;
            this.runningFlag = runningFlag;
            this.barrier = barrier;
            this.latch = latch;
        }

        @Override
        public void run()
        {
            awaitBarrier(barrier);

            int[] currentCoordinates = new int[]{0, 0};
            int[] lastCoordinates = new int[]{0, 0};

            long readsCount = 0;
            long readAttemptsCount = 0;
            long observedMoves = 0;

            while (runningFlag.get())
            {
                readAttemptsCount += spaceship.readPosition(currentCoordinates);

                if (lastCoordinates[0] != currentCoordinates[0] ||
                    lastCoordinates[1] != currentCoordinates[1])
                {
                    ++observedMoves;
                    lastCoordinates[0] = currentCoordinates[0];
                    lastCoordinates[1] = currentCoordinates[1];
                }

                ++readsCount;
            }

            results.reads[id] = readsCount;
            results.readAttempts[id] = readAttemptsCount;
            results.observedMoves[id] = observedMoves;

            latch.countDown();
        }
    }
}
