/*
 *As previously stated, the get() method waits if necessary for the operation to complete, 
 * after which it retrieves the result. This method also has a timeout version, 
 * in which we can specify precisely how long we can afford to wait. For this, we pass 
 * to the get() method a timeout and unit time. If the time expires, this method throws 
 * a TimeoutException and we can interrupt the thread to finish this task by calling 
 * the cancel() method with a true parameter. 
 * 
 * The following application reads the content of HOL2846_sec8.txt with a very short timeout.
 */
package org.mk.training.nio2.asyncchannel;

import static java.lang.System.err;
import static java.lang.System.out;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FileFutureTimeoutReader {

   public static void main(String[] args) {

      ByteBuffer buffer = ByteBuffer.allocate(100);
      int bytesRead = 0;
      Future<Integer> result = null;

      Path path = Paths.get("src/resources", "musiclist.txt");

      try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {

         result = asynchronousFileChannel.read(buffer, 0);

         bytesRead = result.get(1, TimeUnit.NANOSECONDS);

         if (result.isDone()) {
            out.println("The result is available!");
            out.println("Read bytes: " + bytesRead);
         }

      } catch (Exception ex) {
         if (ex instanceof TimeoutException) {
            if (result != null) {
               result.cancel(true);
            }
            out.println("The result is not available!");
            out.println("The read task was cancelled ? " + result.isCancelled());
            out.println("Read bytes: " + bytesRead);
         } else {
            err.println(ex);
         }
      }
   }
}