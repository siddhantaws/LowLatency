/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.collectiontest.offheap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import static org.mk.training.collectiontest.offheap.SharedHashMapTest.testSHMAcquirePerf;

/**
 *
 * @author mohit
 */
public class ConcurrentHashMapTest {
    public static void main(String... args)throws Exception{
        testCHMAcquirePerf();
        
    }
    public static void testCHMAcquirePerf() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, InterruptedException {
        for (int runs : new int[]{10/*, 20,30,40,50*/}) {
            System.out.println("Testing " + runs + " million entries");
            final long entries = runs * 1000 * 1000L;
            final ConcurrentMap<String, AtomicInteger> map = new ConcurrentHashMap<String, AtomicInteger>((int) (entries * 5 / 4), 1.0f, 1024);

            int procs = Runtime.getRuntime().availableProcessors();
            int threads = procs * 2;
            int count = runs > 500 ? runs > 1200 ? 1 : 2 : 3;
            final int independence = Math.min(procs, runs > 500 ? 8 : 4);
            for (int j = 0; j < count; j++) {
                long start = System.currentTimeMillis();
                ExecutorService es = Executors.newFixedThreadPool(procs);
                for (int i = 0; i < threads; i++) {
                    final int t = i;
                    es.submit(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder sb = new StringBuilder();
                            int next = 50 * 1000 * 1000;
                            // use a factor to give up to 10 digit numbers.
                            int factor = Math.max(1, (int) ((10 * 1000 * 1000 * 1000L - 1) / entries));
                            for (long i = t % independence; i < entries; i += independence) {
                                sb.setLength(0);
                                sb.append("u:");
                                sb.append(i * factor);
                                String key = sb.toString();
                                AtomicInteger count = map.get(key);
                                if (count == null) {
                                    map.put(key, new AtomicInteger());
                                    count = map.get(key);
                                }
                                count.getAndIncrement();
                                if (t == 0 && i == next) {
                                    System.out.println(i);
                                    next += 50 * 1000 * 1000;
                                }
                            }
                        }
                    });
                }
                es.shutdown();
                es.awaitTermination(10, TimeUnit.MINUTES);
                printStatus();
                long time = System.currentTimeMillis() - start;
                System.out.printf("Throughput %.1f M ops/sec%n", threads * entries / 1000.0 / time);
            }
        }
    }
    
    private static void printStatus() {
        if (!new File("/proc/self/status").exists()) return;
        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/self/status"));
            for (String line; (line = br.readLine()) != null; )
                if (line.startsWith("Vm"))
                    System.out.print(line.replaceAll("  +", " ") + ", ");
            System.out.println();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
