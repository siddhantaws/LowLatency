/*
 * The first step of any application that involves an asynchronous file channel is to create a new 
 * AsynchronousFileChannel instance for a file by calling one of the two open() methods. 
 * 
 * The easiest to use will receive the path of the file to open or create and, optionally, 
 * a set of options specifying how the file is opened, as shown next. 
 * 
 * This open() method will associate the channel with a system-dependent default 
 * thread pool that may be shared with other channels (the default group).
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class FileFutureReader {

   public static void main(String[] args) {

      ByteBuffer buffer = ByteBuffer.allocate(560);
      String encoding = System.getProperty("file.encoding");

      Path path = Paths.get("src/resources", "musiclist.txt");

      try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {

         Future<Integer> result = asynchronousFileChannel.read(buffer, 0);

         while (!result.isDone()) {
            out.println("Do something else while reading ...");
         }

         out.println("Read done: " + result.isDone());
         out.println("Bytes read: " + result.get());

      } catch (Exception ex) {
         err.println(ex);
      }

      buffer.flip();
      out.print(Charset.forName(encoding).decode(buffer));
      buffer.clear();

   }
}
