/*
 * There is a set of common walks that you can easily implement through the FileVisitor interface. 
 * This code shows you how to write and implement applications to perform a file search.
 */
package org.mk.training.nio2.walking;

import java.io.IOException;
import static java.lang.System.out;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 5. USE THE FILEVISITOR API TO DEVELOP RECURSIVE FILE OPERATIONS.
 *
 * @author mohamed_taman
 * @see java.nio.file.FileVisitor<T>
 * @see java.nio.file.FileVisitResult
 * @see java.nio.file.SimpleFileVisitor<T>
 */
public class Search implements FileVisitor {

   private final Path searchedFile;
   private final PathMatcher matcher;
   private final Long acceptedSize;
   public boolean found;

   public Search(String glob) {
      matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
      searchedFile = null;
      acceptedSize = null;
   }

   public Search(String glob, Long acceptedSize) {
      matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
      this.acceptedSize = acceptedSize;
      searchedFile = null;
   }

   public Search(Path searchedFile) {
      this.searchedFile = searchedFile;
      this.found = false;
      matcher = null;
      acceptedSize = null;
   }

   private void search0(Path file) throws IOException {
      Path name = file.getFileName();
      if (name != null && name.equals(searchedFile)) {
         out.println("Searched file was found: " + searchedFile
                 + " in " + file.toRealPath().toString());
         found = true;
      }
   }

   private void search1(Path file) throws IOException {
      Path name = file.getFileName();
      if (name != null && matcher.matches(name)) {
         out.println("Searched file was found: " + name
                 + " in " + file.toRealPath().toString());
      }
   }

   private void search2(Path file) throws IOException {
      Path name = file.getFileName();
      long size = (Long) Files.getAttribute(file, "basic:size");
      if (name != null && matcher.matches(name) && size <= acceptedSize) {
         out.println("Searched file was found: " + name + " in "
                 + file.toRealPath().toString() + " size (bytes):" + size);
      }
   }

   @Override
   public FileVisitResult postVisitDirectory(Object dir, IOException exc)
           throws IOException {
      out.println("Visited: " + (Path) dir);
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs)
           throws IOException {
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult visitFile(Object file, BasicFileAttributes attrs)
           throws IOException {
      if (searchedFile != null) {
         search0((Path) file);
         if (!found) {
            return FileVisitResult.CONTINUE;
         } else {
            return FileVisitResult.TERMINATE;
         }
      } else if (acceptedSize == null) {
         search1((Path) file);
         return FileVisitResult.CONTINUE;
      } else {
         search2((Path) file);
         return FileVisitResult.CONTINUE;
      }
   }

   @Override
   public FileVisitResult visitFileFailed(Object file, IOException exc)
           throws IOException {
      //report an error if necessary
      return FileVisitResult.CONTINUE;
   }
}