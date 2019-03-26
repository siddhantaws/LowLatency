/*
 * So far, you've seen at work only the first AsynchronousFileChannel.open() method, 
 * which uses the default pool thread. 
 * 
 * It is time to see the second open() method at work, which allows us to specify a 
 * custom thread pool through an ExecutorService object.
 */
package org.mk.training.nio2.asyncchannel;

import static java.lang.System.err;
import static java.lang.System.out;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class FileReaderExecutor {

   private static Set withOptions() {
      final Set options = new TreeSet<>();
      options.add(StandardOpenOption.READ);
      return options;
   }

   public static void main(String[] args) {

      final int THREADS = 5;
      ExecutorService taskExecutor = Executors.newFixedThreadPool(THREADS);

      String encoding = System.getProperty("file.encoding");
      List<Future<ByteBuffer>> list = new ArrayList<>();

      int attendees = 0;

      Path path = Paths.get("src/resources", "musiclist.txt");

      try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, withOptions(), taskExecutor)) {

         for (int i = 0; i < 50; i++) {
            Callable<ByteBuffer> worker = new Callable<ByteBuffer>() {
               @Override
               public ByteBuffer call() throws Exception {
                  ByteBuffer buffer = ByteBuffer.allocateDirect(ThreadLocalRandom.current().nextInt(100, 200));
                  //if is too fast, de-comment this
                  //Thread.sleep(1000 + new Random().nextInt(10000));
                  asynchronousFileChannel.read(buffer, ThreadLocalRandom.current().nextInt(0, 100));

                  return buffer;
               }
            };

            Future<ByteBuffer> future = taskExecutor.submit(worker);

            list.add(future);
         }

         //this will make the executor accept no new threads
         // and finish all existing threads in the queue
         taskExecutor.shutdown();

         //wait until all threads are finish
         while (!taskExecutor.isTerminated()) {
            //do something else while the buffers are prepared
            out.println("Counting sheeps while fill up some buffers! So far I counted: " + (attendees += 1));
         }

         out.println("\nDone! Here are the buffers:\n");
         for (Future<ByteBuffer> future : list) {

            ByteBuffer buffer = future.get();

            out.println("\n\n" + buffer);
            out.println("______________________________________________________");
            buffer.flip();
            out.print(Charset.forName(encoding).decode(buffer));
            buffer.clear();
         }

      } catch (Exception ex) {
         err.println(ex);
      }
   }
}
