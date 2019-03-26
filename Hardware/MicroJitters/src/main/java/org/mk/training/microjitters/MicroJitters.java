/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.microjitters;

/**
 *
 * @author mohit
 */
import net.openhft.affinity.AffinityLock;
import net.openhft.lang.model.constraints.NotNull;

import java.io.PrintStream;
import java.util.Arrays;
/* e.g.
 After 2430 seconds, the average per hour was
 2us	78400
 3us	122703
 4us	345238
 6us	216098
 8us	78694
 10us	3977528
 14us	114495
 20us	4931
 30us	203
 40us	35
 60us	18
 80us	11
 100us	9
 140us	132
 200us	85
 300us	473
 400us	5
 1ms	24
 */

class MicroJitters {

    private static final long[] DELAY = {
        2 * 1000, 3 * 1000, 4 * 1000, 6 * 1000, 8 * 1000, 10 * 1000, 14 * 1000,
        20 * 1000, 30 * 1000, 40 * 1000, 60 * 1000, 80 * 1000, 100 * 1000, 140 * 1000,
        200 * 1000, 300 * 1000, 400 * 1000, 600 * 1000, 800 * 1000, 1000 * 1000,
        2 * 1000 * 1000, 5 * 1000 * 1000, 10 * 1000 * 1000,
        20 * 1000 * 1000, 50 * 1000 * 1000, 100 * 1000 * 1000
    };
    private static final double UTIL = Double.parseDouble(System.getProperty("util", "50"));
    //    static final int CPU = Integer.getInteger("cpu", 0);
    private final int[] count = new int[DELAY.length];
    private long totalTime = 0;

    public static void main(String... ignored) throws InterruptedException {
        AffinityLock al = AffinityLock.acquireLock();

        System.out.println("UTILWA:"+UTIL);
        // warmup.
        new MicroJitters().sample(1000 * 1000 * 1000);

        MicroJitters microJitterSampler = new MicroJitters();
        while (!Thread.currentThread().isInterrupted()) {
            if (UTIL >= 100) {
                microJitterSampler.sample(30L * 1000 * 1000 * 1000);
            } else {
                long sampleLength = (long) ((1 / (1 - UTIL / 100) - 1) * 1000 * 1000);
                System.out.println("sampleLength:"+sampleLength);
                for (int i = 0; i < 30 * 1000; i += 2) {
                //for (int i = 0; i < 6; i += 2) {
                    //System.out.println("microJitterSampler.sample:"+i);
                    microJitterSampler.sample(sampleLength);
                    //noinspection BusyWait
                    //Thread.sleep(1);
                }
            }

            microJitterSampler.print(System.out);
        }
    }

    @NotNull
    private static String asString(long timeNS) {
        return timeNS < 1000 ? timeNS + "ns"
                : timeNS < 1000000 ? timeNS / 1000 + "us"
                : timeNS < 1000000000 ? timeNS / 1000000 + "ms"
                : timeNS / 1000000000 + "sec";
    }

    void sample(long intervalNS) {
        
        long prev = System.nanoTime();
        long end = prev + intervalNS;
        long now;
        do {
            now = System.nanoTime();
            //System.out.println("prev:"+prev+" intervalNS:"+intervalNS+" now:"+now);
            long time = now - prev;
            //System.out.println("time:"+time);
            if (time >= DELAY[0]) {
                int i;
                for (i = 1; i < DELAY.length; i++) {
                    //System.out.println(" DELAY["+i+"]:"+DELAY[i]);
                    if (time < DELAY[i]) {
                        break;
                    }
                }
                count[i - 1]++;
                //System.out.println("count["+(i - 1)+"]:"+count[i - 1]);
            }
            prev = now;
        } while (now < end);
        totalTime += intervalNS;
    }

    void print(@NotNull PrintStream ps) {
        ps.println("After " + totalTime / 1000000000 + " seconds, the average per hour was");
        for (int i = 0; i < DELAY.length; i++) {
            if (count[i] < 1) {
                continue;
            }
            long countPerHour = (long) Math.ceil(count[i] * 3600e9 / totalTime);
            ps.println(asString(DELAY[i]) + '\t' + countPerHour);
        }
        ps.println();
    }
}