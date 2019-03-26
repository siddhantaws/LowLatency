/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.logic.queues;

/*import org.mk.training.queues.UnsafeAccess;

class ColdFields<E> {
    protected final int capacity;
    protected final int mask;
    protected final E[] buffer;
    @SuppressWarnings("unchecked")
    public ColdFields(int capacity) {
        if(Pow2.isPowerOf2(capacity)){
            this.capacity = capacity;
        }
        else{
            this.capacity = Pow2.findNextPositivePowerOfTwo(capacity);
        }
        mask = this.capacity - 1;
        buffer = (E[]) new Object[this.capacity];
    }
}
class L1Pad<E> extends ColdFields<E> {
    public long p10, p11, p12, p13, p14, p15, p16;
    public L1Pad(int capacity) { super(capacity);}
}
class TailField<E> extends L1Pad<E> {
    protected volatile long tail;
    public TailField(int capacity) { super(capacity);}
}
class L2Pad<E> extends TailField<E> {
    public long p20, p21, p22, p23, p24, p25, p26;
    public L2Pad(int capacity) { super(capacity);}
}
class HeadCache<E> extends L2Pad<E> {
    protected long headCache;
    public HeadCache(int capacity) { super(capacity);}
}
class L3Pad<E> extends HeadCache<E> {
    public long p30, p31, p32, p33, p34, p35, p36;
    public L3Pad(int capacity) { super(capacity);}
}
class HeadField<E> extends L3Pad<E> {
    protected volatile long head;
    public HeadField(int capacity) { super(capacity);}
}
class L4Pad<E> extends HeadField<E> {
    public long p40, p41, p42, p43, p44, p45, p46;
    public L4Pad(int capacity) { super(capacity);}
}
class TailCache<E> extends L4Pad<E> {
    protected long tailCache;
    public TailCache(int capacity) { super(capacity);}
 
}
class L5Pad<E> extends TailCache<E> {
    public long p50, p51, p52, p53, p54, p55, p56;
    public L5Pad(int capacity) { super(capacity);}
}
 
public final class SPSCQueue1<E> extends L5Pad<E> implements Queue<E> {
    private final static long TAIL_OFFSET;
    private final static long HEAD_OFFSET;
    static {
        try {
            TAIL_OFFSET = UnsafeAccess.unsafe.objectFieldOffset(TailField.class.getDeclaredField("tail"));
            HEAD_OFFSET = UnsafeAccess.unsafe.objectFieldOffset(HeadField.class.getDeclaredField("head"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    public SPSCQueue1(final int capacity) {
        super(capacity);
    }
    private void headLazySet(long v) {
        UnsafeAccess.unsafe.putOrderedLong(this, HEAD_OFFSET, v);
    }
    private long getHead() {
        return head;
    }
    private void tailLazySet(long v) {
        UnsafeAccess.unsafe.putOrderedLong(this, TAIL_OFFSET, v);
    }
    private long getTail() {
        return tail;
    }
    //.....
}*/