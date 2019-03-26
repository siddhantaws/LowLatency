/*
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
package org.mk.training.queues.spsc82;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.mk.training.util.Pow2;
import org.mk.training.util.UnsafeAccess;

/**
 * <ul>
 * <li>Inlined counters
 * <li>Counters are padded
 * <li>Data is padded
 * <li>Class is pre-padded
 * <li>Data is sparse
 * <li>Double padding!
 * <li>head/tail and tailCache/headCache on same line
 * </ul>
 */
class L0Pad {
	public long p00, p01, p02, p03, p04, p05, p06, p07;
	public long p10, p11, p12, p13, p14, p15, p16, p17;
}

class ColdFields<E> extends L0Pad {
	protected static final int BUFFER_PAD = 32;
	protected static final int SPARSE_SHIFT = Integer.getInteger("sparse.shift", 2);
	protected final int capacity;
	protected final int mask;
	protected final E[] buffer;

	@SuppressWarnings("unchecked")
	public ColdFields(int capacity) {
		if (Pow2.isPowerOf2(capacity)) {
			this.capacity = capacity;
		} else {
			this.capacity = Pow2.findNextPositivePowerOfTwo(capacity);
		}
		mask = this.capacity - 1;
		buffer = (E[]) new Object[(this.capacity << SPARSE_SHIFT) + BUFFER_PAD * 2];
	}
}

class L1Pad<E> extends ColdFields<E> {
	public long p00, p01, p02, p03, p04, p05, p06, p07;
	public long p10, p11, p12, p13, p14, p15, p16;

	public L1Pad(int capacity) {
		super(capacity);
	}
}

class OfferFields<E> extends L1Pad<E> {
	protected volatile long tail;
	protected long headCache;

	public OfferFields(int capacity) {
		super(capacity);
	}
}

class L2Pad<E> extends OfferFields<E> {
	public long p00, p01, p02, p03, p04, p05, p06, p07;
	public long p30, p31, p32, p33, p34, p35, p36;

	public L2Pad(int capacity) {
		super(capacity);
	}
}

class PollFields<E> extends L2Pad<E> {
	protected volatile long head;
	protected long tailCache;

	public PollFields(int capacity) {
		super(capacity);
	}
}

class L3Pad<E> extends PollFields<E> {
	public long p00, p01, p02, p03, p04, p05, p06, p07;
	public long p50, p51, p52, p53, p54, p55, p56;

	public L3Pad(int capacity) {
		super(capacity);
	}
}

public final class SPSCQueue82<E> extends L3Pad<E> implements Queue<E> {
	private final static long TAIL_OFFSET;
	private final static long HEAD_OFFSET;
	static {
		try {
			TAIL_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(OfferFields.class.getDeclaredField("tail"));
			HEAD_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(PollFields.class.getDeclaredField("head"));
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public SPSCQueue82(final int capacity) {
		super(capacity);
	}

	private void headLazySet(long v) {
		UnsafeAccess.UNSAFE.putOrderedLong(this, HEAD_OFFSET, v);
	}

	private long getHead() {
		return head;
	}

	private void tailLazySet(long v) {
		UnsafeAccess.UNSAFE.putOrderedLong(this, TAIL_OFFSET, v);
	}

	private long getTail() {
		return tail;
	}

	public boolean add(final E e) {
		if (offer(e)) {
			return true;
		}
		throw new IllegalStateException("Queue is full");
	}

	private int elementOffsetInBuffer(int index) {
		return BUFFER_PAD + ((index & mask) << SPARSE_SHIFT);
	}

	public boolean offer(final E e) {
		if (null == e) {
			throw new NullPointerException("Null is not a valid element");
		}

		final long currentTail = getTail();
		final long wrapPoint = currentTail - capacity;
		if (headCache <= wrapPoint) {
			headCache = getHead();
			if (headCache <= wrapPoint) {
				return false;
			}
		}
		buffer[elementOffsetInBuffer((int) currentTail)] = e;
		tailLazySet(currentTail + 1);

		return true;
	}

	public E poll() {
		final long currentHead = getHead();
		if (currentHead >= tailCache) {
			tailCache = getTail();
			if (currentHead >= tailCache) {
				return null;
			}
		}

		final int offset = elementOffsetInBuffer((int) currentHead);
		final E e = buffer[offset];
		buffer[offset] = null;

		headLazySet(currentHead + 1);

		return e;
	}

	public E remove() {
		final E e = poll();
		if (null == e) {
			throw new NoSuchElementException("Queue is empty");
		}

		return e;
	}

	public E element() {
		final E e = peek();
		if (null == e) {
			throw new NoSuchElementException("Queue is empty");
		}

		return e;
	}

	public E peek() {
		long currentHead = getHead();
		return getElement((int) currentHead);
	}

	private E getElement(int index) {
		final int offset = elementOffsetInBuffer(index);
		return buffer[offset];
	}

	public int size() {
		return (int) (getTail() - getHead());
	}

	public boolean isEmpty() {
		return getTail() == getHead();
	}

	public boolean contains(final Object o) {
		if (null == o) {
			return false;
		}

		for (long i = getHead(), limit = getTail(); i < limit; i++) {
			final E e = getElement((int) i);
			if (o.equals(e)) {
				return true;
			}
		}

		return false;
	}

	public Iterator<E> iterator() {
		throw new UnsupportedOperationException();
	}

	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	public <T> T[] toArray(final T[] a) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(final Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(final Collection<?> c) {
		for (final Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}

		return true;
	}

	public boolean addAll(final Collection<? extends E> c) {
		for (final E e : c) {
			add(e);
		}

		return true;
	}

	public boolean removeAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		Object value;
		do {
			value = poll();
		} while (null != value);
	}
}
