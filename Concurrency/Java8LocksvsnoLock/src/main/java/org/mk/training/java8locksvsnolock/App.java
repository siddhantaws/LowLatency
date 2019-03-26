package org.mk.training.java8locksvsnolock;

import java.util.concurrent.locks.StampedLock;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        System.out.println("LG_READERS: " + LG_READERS);
        System.out.println("RUNIT: " + RUNIT);
        System.out.println("WBIT: " + WBIT);
        System.out.println("RBITS: " + RBITS);
        System.out.println("RFULL: " + RFULL);
        System.out.println("ABITS: " + ABITS);
        System.out.println("SBITS: " + SBITS);
        System.out.println("ORIGIN: " + ORIGIN);
        long stamp = writeLock();
        tryReadLock();
        unlockWrite(stamp);
        long rstamp=tryReadLock();
        
        unlockRead(rstamp);
        stamp = writeLock();
        tryReadLock();
        unlockWrite(stamp);
        //writeLock();

        tryOptimisticRead();
        //long rstamp = tryReadLock();
        //unlockRead(rstamp);
        rstamp=tryReadLock();
        tryOptimisticRead();
        long rstamp2=tryReadLock();
        
        unlockRead(rstamp);
        unlockRead(rstamp2);
        
    }
    private static final int LG_READERS = 3;
    // Values for lock state and stamp operations
    private static final long RUNIT = 1L;
    private static final long WBIT = 1L << LG_READERS;
    private static final long RBITS = WBIT - 1L;
    private static final long RFULL = RBITS - 1L;
    private static final long ABITS = RBITS | WBIT;
    private static final long SBITS = ~RBITS;
    private static final long ORIGIN = WBIT << 1;
    private static volatile long state = ORIGIN;

    public static long writeLock() {
        long s, next;  // bypass acquireWrite in fully unlocked case only
        boolean canwelock = (((s = state) & ABITS) == 0L);
        System.out.println("(((s = state) & ABITS) == 0L): " + canwelock);
        if (canwelock) {
            next = s + WBIT;
            state = next;

        } else {
            next = 0L;
        }
        System.out.println("wl:lock:state: " + state);
        return next;
    }

    public static void unlockWrite(long stamp) {
        //StampedLock.WNode h;
        if (state != stamp || (stamp & WBIT) == 0L) {
            throw new IllegalMonitorStateException();
        }
        state = (stamp += WBIT) == 0L ? ORIGIN : stamp;
        System.out.println("wl:unlock:state: " + state);
        //if ((h = whead) != null && h.status != 0)
        //  release(h);
    }

    public static long tryReadLock() {
        for (;;) {
            long s, m, next;
            System.out.println("rl:canwelock:(s = state) & ABITS) == WBIT):state:" + state + " ABITS:" + ABITS + " WBIT:" + WBIT+" (state & ABITS):" +(state & ABITS));
            if ((m = (s = state) & ABITS) == WBIT) {
                System.out.println("rl:lock:state: " + 0L);
                return 0L;
            } else if (m < RFULL) {
                //if (U.compareAndSwapLong(this, STATE, s, next = s + RUNIT)) {
                next = s + RUNIT;
                state = next;
                System.out.println("rl:lock:state: " + next);
                return next;
                //}
            /*} else if ((next = tryIncReaderOverflow(s)) != 0L) {
                 return next;*/
            }
        }
    }

    public static void unlockRead(long stamp) {
        long s, m; //WNode h;
        for (;;) {
            if (((s = state) & SBITS) != (stamp & SBITS)
                    || (stamp & ABITS) == 0L || (m = s & ABITS) == 0L || m == WBIT) {
                throw new IllegalMonitorStateException();
            }
            if (m < RFULL) {
                s = s - RUNIT;
                state = s;
                break;
                /*if (U.compareAndSwapLong(this, STATE, s, s - RUNIT)) {
                 if (m == RUNIT && (h = whead) != null && h.status != 0)
                 release(h);
                 break;
                 }*/

            }

            //else if (tryDecReaderOverflow(s) != 0L)
            //break;
        }
        System.out.println("rl:unlock:state: " + state);
    }
    
    public static long tryOptimisticRead() {
        long s;
        long canweread;
        System.out.println("or:canweread:(s = state) & WBIT)==):state:" + state + " WBIT:" + WBIT +" (state & WBIT):" +(state & WBIT));
        if((((s = state) & WBIT) == 0L)){
            canweread=(s & SBITS);
        }
        else {
            canweread=0L;
        }
        System.out.println("or:read:" + canweread);
        return canweread;
    }
    
    public boolean validate(long stamp) {
        //U.loadFence();
        System.out.println("or:validate:(stamp & SBITS) == (state & SBITS):"+((stamp & SBITS) == (state & SBITS)));
        return (stamp & SBITS) == (state & SBITS);
    }
}
