/*
 * Sometimes you need to acquire an exclusive lock on a channel’s file before performing another I/O
 * operation, such as reading or writing. AsynchronousFileChannel provides a lock() method for the Future
 * form and a lock() method for CompletionHandler (both also have signatures for locking regions of a file,
 * more details of which you can find in the official documentation at http://download.oracle.com/javase/7/docs/api/):
 */
package org.mk.training.nio2.asyncchannel;

import static java.lang.System.err;
import static java.lang.System.out;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

public class ExclusiveFileWriter {

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

         Future<FileLock> featureLock = asynchronousFileChannel.lock();
         out.println("Waiting the file to be locked ...");
         FileLock lock = featureLock.get();
         //or, use shortcut
         //FileLock lock = asynchronousFileChannel.lock().get();

         if (lock.isValid()) {
            Future<Integer> featureWrite = asynchronousFileChannel.write(buffer, 0);
            out.println("Waiting the bytes to be written ...");
            int written = featureWrite.get();

            //or, use shorcut
            //int written = asynchronousFileChannel.write(buffer,0).get();
            Thread.sleep(5000);
            out.println("written " + written + " bytes into " + path.getFileName() + " locked file!");

            lock.release();
         }

      } catch (Exception ex) {
         err.println(ex);
      }

   }
}
