package org.mk.training.nio2.links;

import java.io.IOException;
import static java.lang.System.err;
import static java.lang.System.out;
import java.nio.file.Files;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import java.nio.file.Path;
import static java.nio.file.Paths.get;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/**
 * 3. MANAGING SYMBOLIC AND HARD LINKS.
 *
 * @author mohamed_taman
 * @see java.nio.file.Files
 */
public class SymbAndHardLinksOperations {

   private static Path target = get("src/resources", "movielist.txt");
   private static Path link = get("G:", "links");
   private static Path finaLink;

   public static void main(String[] args) {

      try {

         /*
          * ----------------------------
          * 2- Creating a Symbolic Link.|
          * ----------------------------
          */


         finaLink = get(link.toString(), "movielist_L1.txt");

         Path newLink = Files.createSymbolicLink(finaLink, target);

         out.println("New Created link path is: " + newLink.toString()+"\n");

         /*
          * When you want to modify the default attributes of the link, 
          * you can use the third argument of the createSymbolicLink() method.
          */
         boolean iSupported = Files.getFileStore(target).supportsFileAttributeView(PosixFileAttributeView.class);

         if (iSupported) {

            PosixFileAttributes attrs = Files.readAttributes(target, PosixFileAttributes.class);

            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(attrs.permissions());

            finaLink = get(link.toString(), "movielist_L2.txt");

            Files.createSymbolicLink(finaLink, target, attr);

         } else {
            out.println("Posix view NOT supported on current OS.\n");
         }

         /*
          * In addition, you can use the setAttribute() method to modify the 
          * link attributes after creation.
          */
         finaLink = get(link.toString(), "movielist_L3.txt");

         Files.createSymbolicLink(finaLink, target);

         FileTime lm = (FileTime) Files.getAttribute(target, "basic:lastModifiedTime", NOFOLLOW_LINKS);

         FileTime la = (FileTime) Files.getAttribute(target, "basic:lastAccessTime", NOFOLLOW_LINKS);

         Files.setAttribute(finaLink, "basic:lastModifiedTime", lm, NOFOLLOW_LINKS);

         Files.setAttribute(finaLink, "basic:lastAccessTime", la, NOFOLLOW_LINKS);

         /*
          * ------------------------
          * 3- Creating a Hard Link.|
          * ------------------------
          */


         finaLink = get(link.toString(), "movielist_L4.png");

         Path hardLink = Files.createLink(finaLink, get(target.getRoot().toString(), target.subpath(0, 4).toString(), "images", "sd_sample.png"));

         out.println("The hard link (" + hardLink.toString() + ") was successfully created!"+"\n\n");

         /*
          * -----------------------------
          * 4- Checking a Symbolic Link.|
          * ----------------------------
          */

         //solution 1 - check if a path is a symbolic link

         boolean hlink_isSymbolicLink_1 = Files.isSymbolicLink(hardLink);

         finaLink = get(link.toString(), "movielist_L3.txt");

         boolean link_isSymbolicLink_1 = Files.isSymbolicLink(finaLink);

         boolean target_isSymbolicLink_1 = Files.isSymbolicLink(target);

         out.println(hardLink.toString() + " is a symbolic link ? " + hlink_isSymbolicLink_1);

         out.println(finaLink.toString() + " is a symbolic link ? " + link_isSymbolicLink_1);

         out.println(target.toString() + " is a symbolic link ? " + target_isSymbolicLink_1+"\n");

         /*
          * Solution 2
          * ----------
          * You can view the isSymbolicLink attribute through the readAttributes() method
          * (not recommended in this case since it returns a bulk list of attributes) 
          * or, much more easily, through the getAttribute() method.
          * 
          */

         hlink_isSymbolicLink_1 = (boolean) Files.getAttribute(hardLink, "basic:isSymbolicLink");

         link_isSymbolicLink_1 = (boolean) Files.getAttribute(finaLink, "basic:isSymbolicLink");

         target_isSymbolicLink_1 = (boolean) Files.getAttribute(target, "basic:isSymbolicLink");

         out.println(hardLink.toString() + "- Attr: is a symbolic link ? " + hlink_isSymbolicLink_1);

         out.println(finaLink.toString() + "- Attr: is a symbolic link ? " + link_isSymbolicLink_1);

         out.println(target.toString() + "- Attr: is a symbolic link ? " + target_isSymbolicLink_1+"\n\n");

         /*
          * ---------------------------------
          * 5- Locating the Target of a Link.|
          * ---------------------------------
          * 
          * If the passed path is not a link, then a "NotLinkException" exception will be thrown.
          */

         Path linkedpath = Files.readSymbolicLink(finaLink);
         out.println(finaLink.getFileName().toString()+" Linked path is: " + linkedpath.toString()+"\n\n");

         /*
          * ----------------------------------------------------------
          * 6- Checking If a Link and a Target Point to the Same File.|
          * ----------------------------------------------------------
          */

         boolean isSameFile = Files.isSameFile(finaLink, target);

         out.println((isSameFile ? (finaLink.getFileName().toString() + " and " + target.toString() + " points to the same location") : (finaLink.getFileName().toString() + " and " + target.toString() + " doesn't points to the same location")));


      } catch (IOException | UnsupportedOperationException | SecurityException e) {
         e.printStackTrace();
      }

   }
}
