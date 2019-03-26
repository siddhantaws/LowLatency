/*
 * The easiest approach for a better understanding of how to accomplish this task 
 * is to follow a straightforward set of steps accompanied by chunks of codes that 
 * will develop a single-thread blocking TCP server that will echo to the client
 * everything that it gets from it. 
 * 
 * Many of the steps to accomplish this are transferable to other blocking TCP servers as well.
 * 
 */

package org.mk.training.nio2.blksocket;

import java.io.IOException;
import static java.lang.System.err;
import static java.lang.System.out;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class BlkServer {

   public static void main(String[] args) {

      final int DEFAULT_PORT = 5555;
      final String IP = "127.0.0.1";

      ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

      // 1- Create a new server-socket channel
      try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) { // 6- Closing the channel

         //continue if it was successfully created
         if (serverSocketChannel.isOpen()) {

            // 2- Set the blocking mode
            serverSocketChannel.configureBlocking(true);

            //3- Set some options (optional step)
            serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

            //4- Bind the server-socket channel to local address
            serverSocketChannel.bind(new InetSocketAddress(IP, DEFAULT_PORT));

            //display a waiting message while ... waiting clients
            out.println("Waiting for connections ...");

            //5- Wait for incoming connections
            while (true) {
               try (SocketChannel socketChannel = serverSocketChannel.accept()) {
                  // This method is new in Java 7 (NIO.2). 
                  out.println("Incoming connection from: " + socketChannel.getRemoteAddress());

                  //5- Transmitting data
                  while (socketChannel.read(buffer) != -1) {

                     buffer.flip();// make buffer ready for write

                     socketChannel.write(buffer);

                     if (buffer.hasRemaining()) {
                        buffer.compact();// In case of partial write
                     } else {
                        buffer.clear();
                     }
                  }
               } catch (IOException ex) {
                  err.println(ex);
               }
            }
         } else {
            out.println("The server socket channel cannot be opened!");
         }
      } catch (IOException ex) {
         err.println(ex);
      }
   }
}
