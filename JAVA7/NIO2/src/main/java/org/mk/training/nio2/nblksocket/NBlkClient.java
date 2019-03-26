/*
 * Focusing on the client side, the structure is almost the same, with a few differences:
 * 
 * • First, the client socket channel is registered with the SelectionKey.OP_CONNECT 
 *          option, since the client wants to be informed by the selector when the server 
 *          accepts the connection.
 * 
 * • Second, the client does not attempt a connection infinitely, since the server may
 *           not be active; therefore, the Selector.select() method with timeout is proper for 
 *           it (a timeout of 500 to 1,000 milliseconds will do the job).
 * 
 * • Third, the client must check if the key is connectable (i.e., if the SelectionKey.isConnectable() 
 *          method returns true). If the key is connectable, it mixes the socket channel isConnectionPending() 
 *          and finishConnect() methods in a conditional statement for closing the pending connections. 
 *          When you need to tell whether or not a connection operation is in progress on this channel, call the
 *          SocketChannel.isConnectionPending() method, which returns a Boolean value.
 *          Also, finishing the process of connecting a socket channel can be accomplished by the 
 *          SocketChannel.finishConnect() method.
 * 
 * Finally, the client is ready for I/O operations. We reproduced the same scenario as in the blocking 
 * client/server application: the client connects to our server and sends a “Hello!” message, and then keeps
 * sending random numbers between 0 and 100 until the number 50 is generated. When 50 is generated,
 * the client stops sending and closes the channel. The server will echo (write back) everything it reads
 * from the client.
 */
package org.mk.training.nio2.nblksocket;

import java.io.IOException;
import static java.lang.System.err;
import static java.lang.System.out;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
public class NBlkClient {

   public static void main(String[] args) {

      final int DEFAULT_PORT = 5555;
      final String IP = "127.0.0.1";

      ByteBuffer buffer = ByteBuffer.allocateDirect(2 * 1024);
      ByteBuffer randomBuffer;
      CharBuffer charBuffer;

      Charset charset = Charset.defaultCharset();
      CharsetDecoder decoder = charset.newDecoder();

      //1- Open Selector and ServerSocketChannel by calling the open() method
      try (Selector selector = Selector.open();
              SocketChannel socketChannel = SocketChannel.open()) { // 6- Closing the connection

         //check that both of them were successfully opened
         if ((socketChannel.isOpen()) && (selector.isOpen())) {

            //2- Configure non-blocking mode
            socketChannel.configureBlocking(false);

            //3- Set some options
            socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
            socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
            socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

            //4- Register the current channel with the given selector
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            //connect to remote host
            socketChannel.connect(new java.net.InetSocketAddress(IP, DEFAULT_PORT));

            out.println("Localhost: " + socketChannel.getLocalAddress());

            //waiting for the connection
            while (selector.select(1000) > 0) {

               //get keys
               Set keys = selector.selectedKeys();
               Iterator its = keys.iterator();

               //process each key
               while (its.hasNext()) {
                  SelectionKey key = (SelectionKey) its.next();

                  //remove the current key
                  its.remove();

                  //get the socket channel for this key
                  try (SocketChannel keySocketChannel = (SocketChannel) key.channel()) {

                     //attempt a connection
                     if (key.isConnectable()) {

                        //signal connection success
                        out.println("I am connected!");

                        //close pendent connections
                        if (keySocketChannel.isConnectionPending()) {
                           keySocketChannel.finishConnect();
                        }

                        //5- read/write from/to server
                        while (keySocketChannel.read(buffer) != -1) {

                           buffer.flip();

                           charBuffer = decoder.decode(buffer);
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
                              randomBuffer = ByteBuffer.wrap("Random number:".concat(String.valueOf(r)).getBytes("UTF-8"));
                              keySocketChannel.write(randomBuffer);
                           }
                        }
                     }
                  } catch (IOException ex) {
                     err.println(ex);
                  }
               }
            }
         } else {
            out.println("The socket channel or selector cannot be opened!");
         }
      } catch (IOException ex) {
         err.println(ex);
      }


   }
}
