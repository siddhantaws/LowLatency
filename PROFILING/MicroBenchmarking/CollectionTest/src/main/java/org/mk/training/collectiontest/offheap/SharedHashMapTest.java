/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.collectiontest.offheap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.openhft.collections.HugeHashMap;
import net.openhft.collections.SharedHashMap;
import net.openhft.collections.SharedHashMapBuilder;
import net.openhft.lang.model.DataValueClasses;
import net.openhft.lang.values.IntValue;
import net.openhft.lang.values.LongValue;

/**
 *
 * @author mohit
 */
public class SharedHashMapTest {
    
    public static void main(String... args)throws Exception{
        testSHMAcquirePerf();
        
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

    public static void testSHMAcquirePerf() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, InterruptedException, ExecutionException {
//        int runs = Integer.getInteger("runs", 10);
        for (int runs : new int[]{10/*, 20,30,40,50*/}) {
            final long entries = runs * 1000 * 1000L;
            final SharedHashMap<CharSequence, IntValue> map = getSharedStringIntMap(entries, 1024, 20);
            
    
            int procs = Runtime.getRuntime().availableProcessors();
            int threads = procs * 2; // runs > 100 ? procs / 2 : procs;
            int count = runs > 500 ? runs > 1200 ? 1 : 3 : 5;
            final int independence = 8; // Math.min(procs, runs > 500 ? 8 : 4);
            System.out.println("\nKey size: " + runs + " Million entries. " + map.builder());
            for (int j = 0; j < count; j++) {
                long start = System.currentTimeMillis();
                ExecutorService es = Executors.newFixedThreadPool(procs);
                List<Future> futures = new ArrayList<Future>();
                for (int i = 0; i < threads; i++) {
                    final int t = i;
                    futures.add(es.submit(new Runnable() {
                        @Override
                        public void run() {
                            IntValue value = nativeIntValue();
                            StringBuilder sb = new StringBuilder();
                            long next = 50 * 1000 * 1000;
                            // use a factor to give up to 10 digit numbers.
                            int factor = Math.max(1, (int) ((10 * 1000 * 1000 * 1000L - 1) / entries));
                            for (long j = t % independence; j < entries + independence - 1; j += independence) {
                                sb.setLength(0);
                                sb.append("us:");
                                sb.append(j * factor);
                                map.acquireUsing(sb, value);
                                long n = value.addAtomicValue(1);
                                assert n > 0 && n < 1000 : "Counter corrupted " + n;
                                if (t == 0 && j >= next) {
                                    long size = map.longSize();
                                    if (size < 0) throw new AssertionError("size: " + size);
                                    System.out.println(j + ", size: " + size);
                                    next += 50 * 1000 * 1000;
                                }
                            }
                        }
                    }));
                }
                for (Future future : futures) {
                    future.get();
                }
                es.shutdown();
                es.awaitTermination(runs / 10 + 1, TimeUnit.MINUTES);
                long time = System.currentTimeMillis() - start;
                System.out.printf("Throughput %.1f M ops/sec%n", threads * entries / independence / 1000.0 / time);
            }
            printStatus();
            File file = map.file();
            map.close();
            file.delete();
        }
    }
    
    public static IntValue nativeIntValue() {
        return DataValueClasses.newDirectReference(IntValue.class);
//        return new LongValue$$Native();
    }
    
    private static SharedHashMap<CharSequence, IntValue> getSharedStringIntMap(long entries, int segments, int entrySize) throws IOException {
        return new SharedHashMapBuilder()
                .entries(entries)
                .minSegments(segments)
                .entrySize(entrySize)
                .generatedValueType(true)
                .putReturnsNull(true)
                .create(getPersistenceFile(), CharSequence.class, IntValue.class);
    }
    
    
    
    static File getPersistenceFile() {
        String TMP = System.getProperty("java.io.tmpdir");
        File file = new File(TMP + "/shm-test" + System.nanoTime());
        file.delete();
        file.deleteOnExit();
        return file;
    }
}
