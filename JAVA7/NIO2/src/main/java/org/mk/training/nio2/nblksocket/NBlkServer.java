/*
 * I have written the following non-blocking echo server 
 * (every step is commented to help give you a good understanding).
 */
package org.mk.training.nio2.nblksocket;

import java.io.IOException;
import static java.lang.System.err;
import static java.lang.System.out;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NBlkServer {

   private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
   private ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);

   private void startEchoServer() {

      final int DEFAULT_PORT = 5555;

      //1- Open Selector and ServerSocketChannel by calling the open() method
      try (Selector selector = Selector.open();
              ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) { // 7- Closing the channel.

         //check that both of them were successfully opened
         if ((serverSocketChannel.isOpen()) && (selector.isOpen())) {

            //2- Configure non-blocking mode
            serverSocketChannel.configureBlocking(false);

            //3- Set some options
            serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 256 * 1024);
            serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

            //4- Bind the server socket channel to port
            serverSocketChannel.bind(new InetSocketAddress(DEFAULT_PORT));

            //5- Register the current channel with the given selector
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            //display a waiting message while ... waiting!
            System.out.println("Waiting for connections ...");

            while (true) {
               //wait for incomming events
               selector.select();

               //there is something to process on selected keys
               Iterator keys = selector.selectedKeys().iterator();

               while (keys.hasNext()) {
                  SelectionKey key = (SelectionKey) keys.next();

                  //prevent the same key from coming up again
                  keys.remove();

                  if (!key.isValid()) {
                     continue;
                  }

                  if (key.isAcceptable()) {
                     acceptOP(key, selector);
                  } else if (key.isReadable()) {
                     this.readOP(key);
                  } else if (key.isWritable()) {
                     this.writeOP(key);
                  }
               }
            }

         } else {
            err.println("The server socket channel or selector cannot be opened!");
         }
      } catch (IOException ex) {
         err.println(ex);
      }
   }

   //isAcceptable returned true
   private void acceptOP(SelectionKey key, Selector selector) throws IOException {

      ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
      SocketChannel socketChannel = serverChannel.accept();
      socketChannel.configureBlocking(false);

      out.println("Incoming connection from: " + socketChannel.getRemoteAddress());

      // write an welcome message
      socketChannel.write(ByteBuffer.wrap("Hello!\n".getBytes("UTF-8")));

      //register channel with selector for further I/O
      keepDataTrack.put(socketChannel, new ArrayList<byte[]>());
      socketChannel.register(selector, SelectionKey.OP_READ);
   }

   //isReadable returned true
   private void readOP(SelectionKey key) {
      try {
         SocketChannel socketChannel = (SocketChannel) key.channel();

         buffer.clear();

         int numRead = -1;
         try {
            numRead = socketChannel.read(buffer);
         } catch (IOException e) {
            err.println("Cannot read error!");
         }

         if (numRead == -1) {
            this.keepDataTrack.remove(socketChannel);
            out.println("Connection closed by: " + socketChannel.getRemoteAddress());
            socketChannel.close();
            key.cancel();
            return;
         }

         byte[] data = new byte[numRead];
         System.arraycopy(buffer.array(), 0, data, 0, numRead);
         out.println(new String(data, "UTF-8") + " from " + socketChannel.getRemoteAddress());

         //6- Write back to client
         doEchoJob(key, data);
      } catch (IOException ex) {
         err.println(ex);
      }
   }

   //isWritable returned true
   private void writeOP(SelectionKey key) throws IOException {

      SocketChannel socketChannel = (SocketChannel) key.channel();

      List<byte[]> channelData = keepDataTrack.get(socketChannel);
      Iterator<byte[]> its = channelData.iterator();

      while (its.hasNext()) {
         byte[] it = its.next();
         its.remove();
         socketChannel.write(ByteBuffer.wrap(it));
      }

      key.interestOps(SelectionKey.OP_READ);
   }

   private void doEchoJob(SelectionKey key, byte[] data) {
      SocketChannel socketChannel = (SocketChannel) key.channel();
      List<byte[]> channelData = keepDataTrack.get(socketChannel);
      channelData.add(data);

      key.interestOps(SelectionKey.OP_WRITE);
   }

   public static void main(String[] args) {
      NBlkServer server = new NBlkServer();
      server.startEchoServer();
   }
}
