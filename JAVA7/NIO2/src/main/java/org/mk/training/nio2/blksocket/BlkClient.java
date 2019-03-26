/*
 * Let's develop a client for our echo server. 
 * 
 * Suppose the following scenario: 
 * --------------------------------
 * the client connects to our server, sends a “Hello!” message, and then keeps sending 
 * random numbers between 0 and 100 until the number 50 is generated. When the number 50 
 * is generated, the client stops sending and closes the channel. 
 * 
 * The server will echo (write back) everything it reads from the client. 
 * 
 * Now that we have a scenario, let’s see the steps for implementing it.
 */
package org.mk.training.nio2.blksocket;

import java.io.IOException;
import static java.lang.System.err;
import static java.lang.System.out;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Random;

public class BlkClient {

   public static void main(String[] args) {

      final int DEFAULT_PORT = 5555;
      final String IP = "127.0.0.1";

      ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
      ByteBuffer helloBuffer = ByteBuffer.wrap("Hello !".getBytes());
      ByteBuffer randomBuffer;
      CharBuffer charBuffer;

      Charset charset = Charset.defaultCharset();
      CharsetDecoder decoder = charset.newDecoder();

      //1- Create a new socket channel
      try (SocketChannel socketChannel = SocketChannel.open()) { // 6- Closing the Channel. 

         //continue if it was successfully created
         if (socketChannel.isOpen()) {

            //2- Set the blocking mode
            socketChannel.configureBlocking(true);

            //3- Set some options
            socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
            socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
            socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            socketChannel.setOption(StandardSocketOptions.SO_LINGER, 5);

            //4- Connect this channel's socket
            socketChannel.connect(new InetSocketAddress(IP, DEFAULT_PORT));

            //check if the connection was successfully accomplished
            if (socketChannel.isConnected()) {

               //5- Transmitting data
               socketChannel.write(helloBuffer);

               while (socketChannel.read(buffer) != -1) {

                  buffer.flip();

                  charBuffer = decoder.decode(buffer);

                  // Echo the data received from server
                  out.println(charBuffer.toString());

                  if (buffer.hasRemaining()) {
                     buffer.compact();
                  } else {
                     buffer.clear();
                  }

                  int r = new Random().nextInt(100);
                  if (r == 50) {
                     out.println("50 was generated! Close the socket channel!");
                     break;
                  } else {
                     randomBuffer = ByteBuffer.wrap("Random number:".concat(String.valueOf(r)).getBytes());
                     socketChannel.write(randomBuffer);
                  }
               }
            } else {
               err.println("The connection cannot be established!");
            }
         } else {
            err.println("The socket channel cannot be opened!");
         }
      } catch (IOException ex) {
         err.println(ex);
      }
      out.println("Client finished its job and will exit.!");

   }
}
