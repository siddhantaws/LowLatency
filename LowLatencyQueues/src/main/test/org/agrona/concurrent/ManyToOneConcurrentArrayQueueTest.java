package org.agrona.concurrent;

import java.util.Queue;

/**
 * @author Siddhanta Kumar Pattnaik
 */
public class ManyToOneConcurrentArrayQueueTest
{

    private static final Queue<Integer> BUFFER = new ManyToOneConcurrentArrayQueue<>(4);

    public static void shouldExchangeInFifoOrder()
    {
        final int numItems = 7;

        for (int i = 0; i < numItems; i++)
        {
            BUFFER.offer(i);
        }

        /*for (int i = 0; i < numItems; i++)
        {
            assertThat(queue.poll(), is(i));
        }

        assertThat(queue.size(), is(0));
        assertTrue(queue.isEmpty());*/
    }

    public static void main(final String[] args)
    {
        ManyToOneConcurrentArrayQueueTest.shouldExchangeInFifoOrder();
    }
}
