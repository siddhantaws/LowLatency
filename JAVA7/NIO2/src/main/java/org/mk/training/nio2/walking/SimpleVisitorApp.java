/*
 * This section will explain how to copy, move, or delete an entire directory; 
 * I will show you how to do all that through the brand new FileVisitor API. 
 * You also find out how to develop a common walks File tool.
 */
package org.mk.training.nio2.walking;

import java.io.IOException;
import static java.lang.System.err;
import static java.lang.System.out;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.Paths.get;
import java.nio.file.SimpleFileVisitor;
import java.util.EnumSet;

/**
 * 5. USE THE FILEVISITOR API TO DEVELOP RECURSIVE FILE OPERATIONS.
 *
 * @author mohamed_taman
 * @see java.nio.file.FileVisitor<T>
 * @see java.nio.file.FileVisitResult
 * @see java.nio.file.SimpleFileVisitor<T>
 */
public class SimpleVisitorApp extends SimpleFileVisitor<Path> {

   //define the starting file tree
   private static Path path = get("C:", "JavaOne2012/HOL2846-NIO2", "src");

   /*
    * In this example, you may want to traverse a file tree and list the names of all directories. 
    * To accomplish this, it is sufficient to use only the postVisitDirectory() and visitFileFailed() 
    * methods, as shown in the following code.
    * 
    * As you can see, the preVisitDirectory() and visitFile() methods were skipped.
    */
   @Override
   public FileVisitResult postVisitDirectory(Path dir, IOException ioe) {

      out.println("Visited directory: " + dir.toString());
      return FileVisitResult.CONTINUE;

   }

   @Override
   public FileVisitResult visitFileFailed(Path file, IOException exc) {
      err.println(exc);
      return FileVisitResult.CONTINUE;
   }

   /*
    * Starting the Recursive Process
    * ------------------------------
    * Once you have created the recursive mechanism (by implementing the FileVisitor 
    * interface or extending the SimpleFileVisitor class),you can start the process 
    * by calling one of the two Files.walkFileTree() methods.
    * 
    */
   public static void main(String[] args) {
      /*
       * 1- The simplest walkFileTree() method gets the starting file 
       *    (this is usually the file tree root) and the file visitor to invoke 
       *    for each file (this is an instance of the recursive mechanism class).
       */

      startWalk1();

     // Invoke second version of walkFileTree
     // startWalk2();

   }

   private static void startWalk1() {
      try {
         SimpleVisitorApp sfvApp = new SimpleVisitorApp(); //instantiate the walk

         Files.walkFileTree(path, sfvApp); //start the walk

      } catch (Exception e) {
         err.println(e);
      }
   }

   private static void startWalk2() {
      /*
       * This second walkFileTree() method version gets the starting file, 
       * options to customize the walk, the maximum number of directory levels to visit 
       * (to ensure that all levels are traversed, you can specify Integer.MAX_VALUE 
       * for the maximum depth argument), and the walk instance. The accepted options are 
       * the constants of the FileVisitOption enum. 
       * 
       * Actually, this enum contains a single constant, named FOLLOW_LINKS, indicating 
       * that the symbolic links are followed in the walk (by default, they are not followed).
       */
      try {
         SimpleVisitorApp sfvApp = new SimpleVisitorApp(); //instantiate the walk
         EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS); //follow links
         
         // Try to change Integer.MAX_VALUE to 1, 2 or any other value and see the output.
         Files.walkFileTree(path,opts, Integer.MAX_VALUE, sfvApp); //start the walk
         
      } catch (Exception e) {
         err.println(e);
      }
   }
}
