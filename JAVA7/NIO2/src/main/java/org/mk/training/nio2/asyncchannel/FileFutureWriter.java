package org.mk.training.nio2.asyncchannel;

import static java.lang.System.err;
import static java.lang.System.out;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 8. DEVELOPING ASYNCHRONOUS APPLICATIONS.
 *
 * File Write and Future
 *
 * @author mohamed_taman
 *
 * @see java.nio.channels.AsynchronousFileChannel
 * @see java.nio.file.StandardOpenOption
 * @see java.util.concurrent.Callable
 * @see java.util.concurrent.ExecutorService
 * @see java.util.concurrent.Executors
 * @see java.util.concurrent.Future
 * @see java.util.concurrent.ThreadLocalRandom
 *
 */
public class FileFutureWriter {

   public static void main(String[] args) {

      String text = "Asynchronous I/O and synchronous I/O serve different purposes."
              + "You can use synchronous I/O if you simply want to make a request and receive a response. "
              + "Synchronous I/O limits performance and scalability since it is one thread per I/O connection, "
              + "and running thousands of threads significantly increases overhead on the operating system. "
              + "Asynchronous I/O is a different programming model, because you don’t necessarily wait for a "
              + "response, but rather submit your work for execution and then come back for a response "
              + "either almost immediately or sometime later. ";

      ByteBuffer buffer = ByteBuffer.wrap(text.getBytes());

      Path path = Paths.get("src/resources", "musiclist.txt");

      try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE)) {

         Future<Integer> result = asynchronousFileChannel.write(buffer, 100);

         while (!result.isDone()) {
            out.println("Do something else while writing ...");
         }

         out.println("Written done: " + result.isDone());
         out.println("Bytes written: " + result.get());

      } catch (Exception ex) {
         err.println(ex);
      }

   }
}
