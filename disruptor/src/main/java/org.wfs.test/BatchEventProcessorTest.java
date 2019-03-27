package org.wfs.test;

import com.lmax.disruptor.*;

import static com.lmax.disruptor.RingBuffer.createMultiProducer;

/**
 * @author Siddhanta Kumar Pattnaik
 */
public class BatchEventProcessorTest {
    public static void main(String[] args) {
        final RingBuffer<StubEvent> ringBuffer = createMultiProducer(StubEvent.EVENT_FACTORY, 16, new BusySpinWaitStrategy());

        final SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        final BatchEventProcessor<StubEvent> batchEventProcessor = new BatchEventProcessor<StubEvent>(
                ringBuffer, sequenceBarrier, new EventHandler<StubEvent>() {
            @Override
            public void onEvent(StubEvent event, long sequence, boolean endOfBatch) throws Exception {
                System.out.println(event + "\t" + sequence + "\t" + endOfBatch);
            }
        });

        ringBuffer.addGatingSequences(batchEventProcessor.getSequence());

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++)
                    ringBuffer.publish(ringBuffer.next());
            }
        });
        t1.start();
        Thread thread = new Thread(batchEventProcessor);
        thread.start();

    }

    static final class StubEvent {
        private int value;

        public StubEvent(int i) {
            this.value = i;
        }

        public void copy(StubEvent event) {
            value = event.value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public static final EventFactory<StubEvent> EVENT_FACTORY = new EventFactory<StubEvent>() {
            int i = 1;

            public StubEvent newInstance() {
                return new StubEvent(i++);
            }
        };

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + value;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            StubEvent other = (StubEvent) obj;

            return value == other.value;
        }

        @Override
        public String toString() {
            return "value->" + value;
        }
    }

}
