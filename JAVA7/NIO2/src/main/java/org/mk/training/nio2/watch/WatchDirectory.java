package org.mk.training.nio2.watch;

import java.io.IOException;
import static java.lang.System.err;
import static java.lang.System.out;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class WatchDirectory {

   public void watchDir(Path path) throws IOException, InterruptedException {
      /*
       * 1. Creating a WatchService.
       * 9. Closing the Watch Service.
       */ 
      try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
         
         //2. Registering Objects with the Watch Service.
         path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                 StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
         /*
          * 3. Waiting for the Incoming Events.
          * start an infinite loop
          */
         while (true) {

            /*
             * 4.Getting a Watch Key.
             * retrieve and remove the next watch key
             */
            final WatchKey key = watchService.take();

            /*
             * 5. Retrieving Pending Events for a Key.
             * get list of pending events for the watch key * 
             */
            for (WatchEvent<?> watchEvent : key.pollEvents()) {
               
               /*
                * 6. Retrieving the Event Type and Count.
                * get the kind of event (create, modify, delete) * 
                */
               final Kind<?> kind = watchEvent.kind();

               //handle OVERFLOW event
               if (kind == StandardWatchEventKinds.OVERFLOW) {
                  continue;
               }

               /*
                * 7. Retrieving the File Name Associated with an Event.
                * get the filename for the event * 
                */
               final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
               final Path filename = watchEventPath.context();

               //print it out
               out.println(kind + " -> " + filename);
            }
            
            /*
             * 8. Putting the Key Back in Ready State.
             * reset the key
             */ 
            boolean valid = key.reset();

            //exit loop if the key is not valid (if the directory was deleted, for example)
            if (!valid) {
               break;
            }
         }
      }
   }

   public static void main(String[] args) {
      
      final Path path = Paths.get("src/resources");
      WatchDirectory watch = new WatchDirectory();
      try {
         watch.watchDir(path);
      } catch (IOException | InterruptedException ex) {
         err.println(ex);
      }
   }
}
