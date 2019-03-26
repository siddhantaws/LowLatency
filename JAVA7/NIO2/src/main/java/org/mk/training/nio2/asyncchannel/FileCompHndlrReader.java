/*
 * Now that you've seen a few examples of how the Future form works, it's time to see how a
 * CompletionHandler can be written to read the story.txt content. 
 * 
 * After creating an asynchronous file channel for reading the content of the music.txt
 * file, we call the second read() method of AsynchronousFileChannnel class.
 */
package org.mk.training.nio2.asyncchannel;

import static java.lang.System.err;
import static java.lang.System.out;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileCompHndlrReader {

    static Thread current;

    public static void main(String[] args) {
        final ByteBuffer buffer = ByteBuffer.allocate(100);
        Path path = Paths.get("src/resources", "musiclist.txt");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path,
                        StandardOpenOption.READ)) {
            current = Thread.currentThread();
            asynchronousFileChannel.read(buffer, 0, "Read operation status ...", new CompletionHandler<Integer, Object>() {
                @Override
                public void completed(Integer result, Object attachment) {
                    out.println(attachment);
                    out.print("Read bytes: " + result);
                    out.print("Read bytes: " + new String(buffer.array()));
                    current.interrupt();
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    out.println(attachment);
                    out.println("Error:" + exc);
                    current.interrupt();
                }
            });

            out.println("\nWaiting for reading operation to end ...\n");

            try {
                current.join();
            } catch (InterruptedException e) {
                err.println(e);
            }
            //now the buffer contains the read bytes
            out.println("\n\nClose everything and leave! Bye, bye ...");
        } catch (Exception ex) {
            err.println(ex);
        }
    }
}
