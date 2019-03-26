package dynamiccompilation;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

interface Incrementer {
  void increment();
}

class LockIncrementer implements Incrementer {
  private long counter = 0;
  private Lock lock = new ReentrantLock();
  public void increment() {
    lock.lock();
    try {
      ++counter;
    } finally {
      lock.unlock();
    }
  }
}

class SyncIncrementer implements Incrementer {
  private long counter = 0;
  public synchronized void increment() {
    ++counter;
  }
}

class SyncLockTestMain {
  static long test(Incrementer incr) {
    long start = System.nanoTime();
    for(long i = 0; i < 10000000L; i++)
      incr.increment();
    return System.nanoTime() - start;
  }

  public static void main(String[] args) {
    long synchTime = test(new SyncIncrementer());
    long lockTime = test(new LockIncrementer());
    System.out.printf("synchronized: %1$10d\n", synchTime);
    System.out.printf("Lock:         %1$10d\n", lockTime);
    System.out.printf("Lock/synchronized = %1$.3f",
      (double)lockTime/(double)synchTime);
  }
}