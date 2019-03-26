package org.mk.training.nio2.walking;

import java.io.IOException;
import static java.lang.System.err;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;

public class FileDirWalksApp {

   public static void main(String[] args) {
      try {
         searchFileByName();

         searchFileGlobPatrn();
         
         searchFileComplex();
         
      } catch (IOException ex) {
         err.println(ex);
      }


   }

   /*
    * Searching for Files by Name:
    * ---------------------------
    * This Method will search for the file "Search.java" in the entire default 
    * file system and will stop the search when it finds it.
    */
   private static void searchFileByName() throws IOException {
      Path searchFile = Paths.get("Search.java");
      Search walk = new Search(searchFile);
      EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
      Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
      for (Path root : dirs) {
         if (!walk.found) {
            Files.walkFileTree(root, opts, Integer.MAX_VALUE, walk);
         }
      }
      if (!walk.found) {
         System.out.println("The file " + searchFile + " was not found!");
      }
   }

   /*
    * Searching for Files by Glob Pattern:
    * -----------------------------------
    * Sometimes you may have only partial information about the file you want to search for, such as only its 
    * name or extension or perhaps even just a chuck of its name or extension. Based on this small piece of
    * information, you can write a glob pattern, as described in the section 4 section "Listing the Content by
    * Applying a Glob Pattern." The search will locate all files in a file store that match the glob pattern, and 
    * from the results you'll probably be able to find the file you needed to locate.
    * 
    * The following code snippet searches all files of type *.png in the C:\JavaOne2012 file tree. The 
    * process will stop only after the entire tree has been traversed.
    */
   private static void searchFileGlobPatrn() throws IOException {
      String glob = "*.png";
      Path fileTree = Paths.get("src/resources", "movielist.txt");
      Search walk = new Search(glob);
      EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
      Files.walkFileTree(fileTree, opts, Integer.MAX_VALUE, walk);
   }

   /*
    * Searching for Files Complex:
    * -----------------------------------
    * If you have additional information about the file you are looking for, then you can create a more
    * complex search. For example, besides the small piece of information about the file name and type,
    * perhaps you know that the file size is smaller than a certain number of kilobytes, or perhaps you know a
    * detail such as when the file was created, when the file was last modified, whether the file is hidden or
    * read-only, or who owns it.
    * 
    * The following code snippet searches all files of type *.png in the NIO file tree. combined  
    * with a size of file smaller then 100KB (as you probably know, the size is a basic attribute).
    */
   private static void searchFileComplex() throws IOException {
      String glob = "*.png";
      long size = 102400; //100 kilobytes in bytes
      Path fileTree = Paths.get("src/resources", "movielist.txt");
      Search walk = new Search(glob, size);
      EnumSet opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
      Files.walkFileTree(fileTree, opts, Integer.MAX_VALUE, walk);
   }
}
