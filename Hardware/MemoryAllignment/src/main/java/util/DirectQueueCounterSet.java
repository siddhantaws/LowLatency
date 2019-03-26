package util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class DirectQueueCounterSet {
	private static final int PADDING_SIZE = 8 * 7;
	private final ByteBuffer buffy = ByteBuffer.allocateDirect(
	        PADDING_SIZE * 6 + 8 * 4).order(ByteOrder.nativeOrder());
	private final long headAddress = UnsafeDirectByteBuffer.getAddress(buffy)
	        + PADDING_SIZE;
	private final long tailAddress = headAddress + 8 + PADDING_SIZE;
	private final long headCacheAddress = tailAddress + 8 + PADDING_SIZE;;
	private final long tailCacheAddress = headCacheAddress + 8 + PADDING_SIZE;;

	public DirectQueueCounterSet() {
	}

	public DirectQueueCounterSet(final long initialValue) {
		setHead(initialValue);
		setTail(initialValue);
		setHeadCache(initialValue);
		setTailCache(initialValue);
	}

	public long getHead() {
		return UnsafeAccess.UNSAFE.getLongVolatile(null, headAddress);
	}

	public void setHead(final long value) {
		UnsafeAccess.UNSAFE.putLong(headAddress, value);
	}

	public long getTail() {
		return UnsafeAccess.UNSAFE.getLongVolatile(null, tailAddress);
	}

	public void setTail(final long value) {
		UnsafeAccess.UNSAFE.putLong(tailAddress, value);
	}

	public long getHeadCache() {
		return UnsafeAccess.UNSAFE.getLong(null, headCacheAddress);
	}

	public void setHeadCache(final long value) {
		UnsafeAccess.UNSAFE.putLong(headCacheAddress, value);
	}

	public long getTailCache() {
		return UnsafeAccess.UNSAFE.getLong(null, tailCacheAddress);
	}

	public void setTailCache(final long value) {
		UnsafeAccess.UNSAFE.putLong(tailCacheAddress, value);
	}

	public String toString() {
		return "head:" + getHead() + " tail:" + getTail();
	}

}
