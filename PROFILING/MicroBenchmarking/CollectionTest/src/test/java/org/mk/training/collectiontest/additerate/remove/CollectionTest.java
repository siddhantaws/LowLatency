/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.collectiontest.additerate.remove;

/**
 *
 * @author mohit
 */
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import net.openhft.collections.HugeConfig;
import net.openhft.collections.HugeHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;


public class CollectionTest {
  static final int RUNS_TIME_MS = 10 * 1000;
  static final int LARGEST_SIZE = 100 * 1000;
  static final int[] INTS = new int[LARGEST_SIZE];

  static {
    for (int i = 0; i < LARGEST_SIZE; i++) INTS[i] = i;
  }

  @Test
  public void performanceTest() {
    test(new TreeSet<Integer>());
    test(Collections.synchronizedSortedSet(new TreeSet<Integer>()), "synchronized TreeSet");
    test(new ArrayList<Integer>());
    test(new LinkedList<Integer>());
    test(new HashSet<Integer>());
    test(new LinkedHashSet<Integer>());
    test(Collections.newSetFromMap(new IdentityHashMap<Integer, Boolean>()), "newSetFromMap IdentityHashMap");
    test(Collections.newSetFromMap(new WeakHashMap<Integer, Boolean>()), "newSetFromMap WeakHashMap");

    test(Collections.synchronizedList(new ArrayList<Integer>()), "synchronized ArrayList");
    test(new Vector<Integer>());
    test(Collections.synchronizedList(new LinkedList<Integer>()), "synchronized LinkedList");
    test(Collections.synchronizedSet(new HashSet<Integer>()), "synchronized HashSet");
    test(Collections.synchronizedSet(new LinkedHashSet<Integer>()), "synchronized LinkedHashSet");
    test(new CopyOnWriteArrayList<Integer>());
    test(new CopyOnWriteArraySet<Integer>());
    test(Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>()), "newSetFromMap ConcurrentHashMap");
    test(Collections.newSetFromMap(new NonBlockingHashMap<Integer,Boolean>()), "newSetFromMap NonBlockingHashMap");
    test(Collections.newSetFromMap(new ConcurrentSkipListMap<Integer, Boolean>()), "newSetFromMap ConcurrentSkipListMap");
      
    test(Collections.newSetFromMap(new ConcurrentSkipListMap<Integer, Boolean>()), "newSetFromMap ConcurrentSkipListMap");
    
    
  }

  private void test(Collection<Integer> ints) {
    test(ints, ints.getClass().getSimpleName());
  }

  private void test(Collection<Integer> ints, String collectionName) {
    for (int size = LARGEST_SIZE; size >= 10; size /= 10) {
      long adding = 0;
      long removing = 0;
      long iterating = 0;

      int runs = 0;
      long endTime = System.currentTimeMillis() + RUNS_TIME_MS;
      do {
        runs++;
        long start = System.nanoTime();
        testAdding(ints, size);

        adding += System.nanoTime() - start;

        start = System.nanoTime();
        for (int repeat = 0; repeat < 100; repeat++)
          testIterating(ints);
        iterating += System.nanoTime() - start;

        start = System.nanoTime();
        testRemoving(ints, size);
        removing += (System.nanoTime() - start) * 2;

        ints.clear();
      } while (endTime > System.currentTimeMillis());
      System.out.println("name:" + collectionName
                             + ":" + String.format("%,d", size)
                             + ":" + format(10 * adding / runs / size)
                             + ":" + format(iterating / runs / size)
                             + ":" + format(10 * removing / runs / size)
                             );
    }
  }

  private String format(long l) {
    return l < 1000 ? "" + (l / 10.0) : l < 10000 ? "" + l / 10 : String.format("%,d", l / 10);
  }

  private static void testAdding(Collection<Integer> ints, int size) {
    // adding
    for (int i = 0; i < size; i++)
      ints.add(INTS[i]);
  }

  private static long testIterating(Collection<Integer> ints) {
    // iterating
    long sum = 0;
    for (Integer i : ints)
      sum += i;
    return sum;
  }

  private void testRemoving(Collection<Integer> ints, int size) {
    // forward and reverse
    for (int i = 0; i < size / 2; i++) {
      ints.remove(INTS[i]);
      ints.remove(INTS[size - i - 1]);
    }
  }
}
