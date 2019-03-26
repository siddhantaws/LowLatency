package org.mk.training.lockvsnolock;


import org.mk.training.lockvsnolock.Point;

public class SynchronizedPoint implements Point
{
    private int x;
    private int y;

    @Override
    public synchronized int readPosition(final int[] coordinates)
    {
        coordinates[0] = x;
        coordinates[1] = y;

        return 1;
    }

    @Override
    public synchronized int move(final int xDelta, final int yDelta)
    {
        x += xDelta;
        y += yDelta;

        return 1;
    }
}
