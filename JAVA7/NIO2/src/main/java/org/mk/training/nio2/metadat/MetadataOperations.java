package org.mk.training.nio2.metadat;

import java.io.IOException;
import static java.lang.System.err;
import static java.lang.System.out;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import static java.nio.file.Files.getAttribute;
import static java.nio.file.Files.getFileAttributeView;
import static java.nio.file.Files.getFileStore;
import static java.nio.file.Files.readAttributes;
import static java.nio.file.Files.setAttribute;
import static java.nio.file.Files.setLastModifiedTime;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.Paths.get;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Set;

public class MetadataOperations {

   private static FileSystem fs = FileSystems.getDefault();
   private static Path path = get("src/resources", "movielist.txt");

   public static void main(String... args) {
      /* 
       * -------------------------------------------------------------
       * 5.1. Determining Views Supported by a Particular File System |
       * -------------------------------------------------------------
       */

      determiningViewsSupportedByParticularFS();

      /* 
       * ----------------
       * 5.2. Basic View |
       * ----------------
       */

      basicViewsOperations();

      /* 
       * -------------------------
       * 5.3. DOS or (Samba) view |
       * -------------------------
       */

      dosSambaViewsOperations();

      /* 
       * ---------------------
       * 5.4. File owner view |
       * ---------------------
       */

      fileOwnerViewsOperations();

      /* 
       * ----------------
       * 5.5. POSIX view |
       * ----------------
       */

      posixViewsOperations();

      /* 
       * --------------
       * 5.6. ACL view |
       * --------------
       */

      aclViewsOperations();

      /* 
       * ---------------------------------------
       * 5.7. User-Defined File Attributes View |
       * ---------------------------------------
       */

      userDefinedViewsOperations();

      /* 
       * ---------------------------
       * 5.8. File Store Attributes |
       * ---------------------------
       */

      fileStoreAttributes();
   }

   private static void determiningViewsSupportedByParticularFS() {

      out.println(" ------------------------------------------------------------- ");
      out.println("| 5.1 Determining Views Supported by a Particular File System |");
      out.println(" ------------------------------------------------------------- \n");


      /*
       * • BasicFileAttributeView: This is a view of basic attributes that must be supported 
       *                           by all file system implementations. 
       *                           The attribute view name is basic.
       * 
       * • DosFileAttributeView: This view provides the standard four supported attributes 
       *                         on file systems that support the DOS attributes. 
       *                         The attribute view name is dos.
       * 
       * • PosixFileAttributeView: This view extends the basic attribute view with attributes
       *                           supported on file systems that support the POSIX (Portable Operating System
       *                           Interface for Unix) family of standards, such as Unix. 
       *                           The attribute view name is posix.
       * 
       * • FileOwnerAttributeView: This view is supported by any file system 
       *                           implementation that supports the concept of a file owner. 
       *                           The attribute view name is owner.
       * 
       * • AclFileAttributeView: This view supports reading or updating a file’s ACL. The 
       *                         NFSv4 ACL model is supported. 
       *                         The attribute view name is acl.
       * 
       * • UserDefinedFileAttributeView: This view enables support of metadata that is user defined.
       * 
       */

      out.println(" Get all supported file system views "
              + "\n ---------------------------------- \n");

      for (String sfsView : fs.supportedFileAttributeViews()) {
         out.println(sfsView);
      }

      /*
       * You can test a particular view on a file store by calling the FileStore.supportsFileAttributeView() method.
       * You can pass the desired view as a String or as a class name.
       */

      out.println("\n Check if the file store support a specific view "
              + "\n -------------------------------------------------- \n");

      for (FileStore fileStore : fs.getFileStores()) {
         boolean supported = fileStore.supportsFileAttributeView(BasicFileAttributeView.class);
         out.println("Is " + fileStore.name() + ": supports \'basic file attribute view\' ---> " + (supported ? "Yes" : "No"));
      }


      // Moreover, you can check if a file store in which a particular file resides supports a single view.

      out.println("\n Check if a file store in which a particular file resides supports a single view "
              + "\n -------------------------------------------------------------------------------- \n");
      try {
         FileStore store = getFileStore(path);
         boolean supported = store.supportsFileAttributeView("acl");
         out.println("Is " + store.name() + ": supports \'ACL file attribute view\' ---> " + (supported ? "Yes" : "No"));
      } catch (IOException ex) {
         out.println(ex);
      }
   }

   private static void basicViewsOperations() {
      out.println("\n ------------------ ");
      out.println("|  5.2. Basic View |");
      out.println(" ------------------ \n");

      /* 
       * Most file system implementations support a set of common attributes 
       * (size, creation time, last accessed time, last modified time).
       * ---------------------------------------------
       * 1- Get Bulk Attributes with readAttributes().|
       * ---------------------------------------------
       */

      out.println("1- Get Bulk Attributes with readAttributes() "
              + "\n --------------------------------------------- \n");

      BasicFileAttributes attr = null;

      try {
         attr = readAttributes(path, BasicFileAttributes.class);
      } catch (IOException e) {
         err.println(e);
      }
      out.println("File size: " + attr.size());
      out.println("File creation time: " + attr.creationTime());
      out.println("File was last accessed at: " + attr.lastAccessTime());
      out.println("File was last modified at: " + attr.lastModifiedTime());

      out.println("Is directory? " + attr.isDirectory());
      out.println("Is regular file? " + attr.isRegularFile());
      out.println("Is symbolic link? " + attr.isSymbolicLink());
      out.println("Is other? " + attr.isOther());

      out.println("\n -------------------------------------------------- \n");

      /* 
       * ---------------------------------------------
       * 2- Get a Single Attribute with getAttribute().|
       * ---------------------------------------------
       * If you need to extract a single attribute instead of all the attributes 
       * in bulk, use the getAttribute() method.
       * 
       * Basic attribute names are listed here:
       * • lastModifiedTime
       * • lastAccessTime
       * • creationTime
       * • size
       * • isRegularFile
       * • isDirectory
       * • isSymbolicLink
       * • isOther
       * • fileKey
       * 
       * The generally accepted form for retrieving a single attribute is 
       *      
       * [view-name:]attribute-name. 
       * 
       * The view-name is basic.
       */

      out.println("2- Get a Single Attribute with getAttribute(). "
              + "\n ---------------------------------------- \n");
      try {
         long size = Long.class.cast(getAttribute(path, "basic:size", NOFOLLOW_LINKS));
         System.out.println("Size: " + size);
      } catch (IOException e) {
         err.println(e);
      }
      out.println("\n -------------------------------------------------- \n");

      /* 
       * ----------------------------
       * 3- Update a Basic Attribute.|
       * ----------------------------
       * If any one of lastModifiedTime, lastAccessTime, or creationTime has the 
       * value null, then the corresponding timestamp is not changed.
       */

      out.println(" 3- Update a Basic Attribute. "
              + "\n ------------------------------\n");

      long time = System.currentTimeMillis();
      FileTime fileTime = FileTime.fromMillis(time);

      try {

         // three defirrent ways to set attributes

         getFileAttributeView(path, BasicFileAttributeView.class).setTimes(null, fileTime, null);
         setLastModifiedTime(path, fileTime);
         setAttribute(path, "basic:creationTime", fileTime, NOFOLLOW_LINKS);

         /*
          * Obviously, now you have to extract the three attributes' 
          * values to see the changes. You can do so by using the getAttribute() method:
          */

         FileTime lastModifiedTime = FileTime.class.cast(getAttribute(path,
                 "basic:lastModifiedTime", NOFOLLOW_LINKS));
         FileTime creationTime = FileTime.class.cast(getAttribute(path,
                 "basic:creationTime", NOFOLLOW_LINKS));
         FileTime lastAccessTime = FileTime.class.cast(getAttribute(path, "basic:lastAccessTime", NOFOLLOW_LINKS));

         out.println("New last modified time: " + lastModifiedTime);
         out.println("New creation time: " + creationTime);
         out.println("New last access time: " + lastAccessTime);

      } catch (IOException e) {
         err.println(e);
      }
      out.println("\n -------------------------------------------------- \n");
   }

   private static void dosSambaViewsOperations() {
      out.println("\n -------------------------- ");
      out.println("| 5.3. DOS or (Samba) view |");
      out.println(" -------------------------- \n");

      out.println("\n Get Bulk Attributes with readAttributes() "
              + "\n ------------------------------------------ \n");
      /*
       * Specific to the DOS file system (or Samba), the DosFileAttributeView view extends 
       * the basic view with the DOS attributes (which means that you can 
       * access the basic view directly from DOS view). 
       * 
       * There are four attributes, which are mapped by the following methods:
       * 
       * • isReadOnly(): Returns the readonly attribute’s value 
       *                 (if true, the file can’t be deleted or updated)
       * 
       * • isHidden(): Returns the hidden attribute’s value 
       *               (if true, the file is not visible to the users)
       * 
       * • isArchive(): Returns the archive attribute’s value (specific to backup programs)
       * 
       * • isSystem(): Returns the system attribute’s value 
       *               (if true, the file belongs to the operating system)
       * 
       */

      try {

         DosFileAttributes attr = readAttributes(path, DosFileAttributes.class, NOFOLLOW_LINKS);

         out.println("Is read only ? " + attr.isReadOnly());
         out.println("Is Hidden ? " + attr.isHidden());
         out.println("Is archive ? " + attr.isArchive());
         out.println("Is system ? " + attr.isSystem() + "\n");

         //setting the hidden attribute to true

         setAttribute(path, "dos:hidden", true, NOFOLLOW_LINKS);

         //getting the hidden attribute

         boolean hidden = (Boolean) getAttribute(path, "dos:hidden", NOFOLLOW_LINKS);
         out.println("\n Is hidden ? " + hidden);

         setAttribute(path, "dos:hidden", false, NOFOLLOW_LINKS);

         hidden = (Boolean) getAttribute(path, "dos:hidden", NOFOLLOW_LINKS);
         out.println("\n Is hidden ? " + hidden);

      } catch (IOException e) {
         err.println(e);
      }

      out.println("\n -------------------------------------------------- \n");

   }

   private static void fileOwnerViewsOperations() {
      out.println("\n --------------------- ");
      out.println("| 5.4. File owner view |");
      out.println(" --------------------- \n");

      UserPrincipal owner = null;

      /* 
       * -------------------------------------------
       * 1- Set a File Owner Using Files.setOwner().|
       * -------------------------------------------
       */
      out.println("\n 1- Set/get a File Owner Using Files.setOwner() / getOwner(). "
              + " \n 2- Set/get a File Owner Using FileOwnerAttributeView.setOwner() / getOwner(). "
              + " \n 3- Set/get a File Owner Using Files.setAttribute() / getAttribute(). "
              + "\n -------------------------------------------------------------------------------- \n");
      try {

         owner = path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName("mohamed_taman");

         out.println("Owner Name before: " + Files.getOwner(path, NOFOLLOW_LINKS).getName());

         // 1- Set a File Owner Using Files.setOwner().
         Files.setOwner(path, owner);

         // 2- Get a File Owner Using Files.getOwner(). 
         owner = Files.getOwner(path, NOFOLLOW_LINKS);

         out.println("Owner Name after: " + owner.getName());

         out.println("------------------------------------- ");

         owner = path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName("ora_dba");


         out.println("Owner Name before: " + ((UserPrincipal) getAttribute(path, "owner:owner", NOFOLLOW_LINKS)).getName());

         // 3- Set a File Owner Using Files.setAttribute(). 
         setAttribute(path, "owner:owner", owner, NOFOLLOW_LINKS);

         // 4- Get a File Owner Using Files.getAttribute(). 
         owner = (UserPrincipal) getAttribute(path, "owner:owner", NOFOLLOW_LINKS);

         out.println("Owner Name after: " + owner.getName());
         out.println("------------------------------------- ");

         owner = path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName("administrator");

         FileOwnerAttributeView foav = Files.getFileAttributeView(path,
                 FileOwnerAttributeView.class);

         out.println("Owner Name before: " + foav.getOwner().getName());

         // 5- Set a File Owner Using FileOwnerAttributeView.setOwner().
         foav.setOwner(owner);

         // 6- Get a File Owner Using FileOwnerAttributeView.getOwner().
         owner = foav.getOwner();

         out.println("Owner Name after: " + owner.getName());

      } catch (IOException e) {
         err.println(e);
      }

      out.println("\n -------------------------------------------------- \n");



   }

   private static void posixViewsOperations() {
      out.println("\n ---------------- ");
      out.println("| 5.5. POSIX view |");
      out.println(" ---------------- \n");

      /*
       * Based on the PosixFileAttributes class, you can extract the POSIX attributes as follows:
       */
      out.println("\n Extract the POSIX attributes "
              + "\n -------------------------------- \n");

      PosixFileAttributes attr = null;

      try {

         if (getFileStore(path).supportsFileAttributeView("posix")) {

            attr = readAttributes(path, PosixFileAttributes.class);

            out.println("File owner: " + attr.owner().getName());
            out.println("File group: " + attr.group().getName());
            out.println("File permissions: " + attr.permissions().toString());

// Or you can use the "long way" by calling the Files.getFileAttributeView() method:

            attr = null;

            attr = getFileAttributeView(path, PosixFileAttributeView.class, NOFOLLOW_LINKS).readAttributes();

            out.println("File owner: " + attr.owner().getName());
            out.println("File group: " + attr.group().getName());
            out.println("File permissions: " + attr.permissions().toString());

            out.println("\n POSIX Permissions "
                    + "\n--------------------------------\n");

            /*
             * You can extract the POSIX permissions of a file and create 
             * another file with the same attributes as follows.
             */

            Path newPath = Paths.get(path.getParent().toString(), "posix", "NewHOL2846.txt");

            FileAttribute<Set<PosixFilePermission>> posixAttrs = PosixFilePermissions.asFileAttribute(attr.permissions());

            Files.createFile(newPath, posixAttrs);

            /*
             * Moreover, you can set a file’s permissions as a hard-coded 
             * string by calling the fromString() method.
             */

            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rw-r--r--");

            Files.setPosixFilePermissions(newPath, permissions);

            permissions = null;

            permissions = Files.getPosixFilePermissions(newPath, NOFOLLOW_LINKS);

            for (PosixFilePermission perm : permissions) {
               out.println(perm.name());
            }

            out.println("\n POSIX Group Owner "
                    + "\n--------------------------------\n");

            GroupPrincipal group = newPath.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByGroupName("Administrators");

            Files.getFileAttributeView(newPath, PosixFileAttributeView.class).setGroup(group);

            /*
             * You can easily find out the group by calling the Files.getAttribute() method.
             * 
             * You can gain access to owners by calling FileOwnerAttributeView.getOwner() and 
             * FileOwnerAttributeView.setOwner(), which are inherited in the POSIX view.
             */

            group = (GroupPrincipal) Files.getAttribute(newPath, "posix:group", NOFOLLOW_LINKS);

            out.println(group.getName());


         } else {
            out.println(path.toAbsolutePath() + ", 'Dosen't support the POSIX system. try running the code on linux or unix.");
         }

      } catch (Exception e) {
         err.println(e);
      }

      out.println("\n -------------------------------------------------- \n");
   }

   private static void aclViewsOperations() {
      out.println("\n --------------- ");
      out.println("| 5.6. ACL view |");
      out.println(" --------------- \n");

      try {

         List<AclEntry> acllist = null;
         AclFileAttributeView aclview = null;

         /* 
          * --------------------------------------------------
          * 1- Read an ACL Using Files.getFileAttributeView().|
          * --------------------------------------------------
          */


         aclview = Files.getFileAttributeView(path, AclFileAttributeView.class);

         acllist = aclview.getAcl();

         /* 
          * ------------------------------------------
          * 2- Read an ACL Using Files.getAttribute().|
          * ------------------------------------------
          *
          * ACL attributes can be required with the following names:
          * 
          * • acl
          * • owner
          * 
          * The generally accepted form is [view-name:]attribute-name. 
          * The view-name is acl.
          */

         acllist = (List<AclEntry>) Files.getAttribute(path, "acl:acl", NOFOLLOW_LINKS);

         /* 
          * --------------------
          * 3- Read ACL Entries.|
          * --------------------
          * 
          * The previous two examples showed you how to extract the ACL for a specified path. 
          * The result was a list of AclEntry—a class that maps an entry from an ACL. 
          * Each entry has four components:
          * 
          * • Type: Determines if the entry grants or denies access. 
          *         It can be ALARM, ALLOW, AUDIT, or DENY.
          * 
          * • Principal: The identity to which the entry grants or denies access. 
          *              This is mapped as a UserPrincipal.
          * 
          * • Permissions: A set of permissions. Mapped as Set<AclEntryPermission>.
          * 
          * • Flags: A set of flags to indicate how entries are inherited and propagated. 
          *          Mapped as Set<AclEntryFlag>.
          * 
          */

         out.println("\n 3- Read ACL Entries. "
                 + "\n -------------------------------------------------- \n");

         for (AclEntry aclentry : acllist) {
            out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
            out.println("Principal: " + aclentry.principal().getName());
            out.println("Type: " + aclentry.type().toString());
            out.println("Permissions: " + aclentry.permissions().toString());
            out.println("Flags: " + aclentry.flags().toString());
         }
         out.println("\n -------------------------------------------------- \n");

         /* 
          * ---------------------------------
          * 4- Grant a New Access in an ACL. |
          * ---------------------------------
          * Note You can gain access to owners by calling FileOwnerAttributeView.getOwner()
          * and FileOwnerAttributeView.setOwner(), which are inherited in the ACL view.
          * 
          * ACL entries are created using an associated AclEntry.Builder object 
          * by invoking its build() method. 
          * 
          * For example, if you want to grant a new access to a principal, 
          * then you must follow this process:
          */


         //1- Lookup for the principal
         UserPrincipal user = path.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName("stillwaterrz");

         //2- Get the ACL view

         AclFileAttributeView view = Files.getFileAttributeView(path, AclFileAttributeView.class);

         //3- Create a new entry
         AclEntry entry = AclEntry.newBuilder().setType(AclEntryType.ALLOW).setPrincipal(user).setPermissions(AclEntryPermission.READ_DATA, AclEntryPermission.APPEND_DATA).build();

         //4- read ACL
         List<AclEntry> acl = view.getAcl();

         //5- Insert the new entry
         acl.add(0, entry);

         //6- rewrite ACL
         view.setAcl(acl);

         //or, like this
         //Files.setAttribute(path, "acl:acl", acl, NOFOLLOW_LINKS);

      } catch (Exception e) {

         err.println(e);
      }
      out.println("\n -------------------------------------------------- \n");
   }

   private static void userDefinedViewsOperations() {
      out.println("\n ---------------------------------------- ");
      out.println("| 5.7. User-Defined File Attributes View |");
      out.println(" ----------------------------------------");

      try {
         // Check User-Defined Attributes Supportability

         if (getFileStore(path).supportsFileAttributeView(UserDefinedFileAttributeView.class)) {
            /* 
             * ---------------------------
             * 1- Define a User Attribute.|
             * ---------------------------
             */
            out.println("1- Define a User Attribute. "
                    + "\n------------------------------");

            UserDefinedFileAttributeView udfav = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
            out.println("Attrs. before deletion. its size: " + udfav.list().size());
            for (String name : udfav.list()) {
               out.println(udfav.size(name) + " " + name);
            }

            int written = udfav.write("file.description", Charset.defaultCharset().
                    encode("This file contains private information about HOL2846!"));

            out.println("\n--------------------------------------------------");

            /* 
             * -----------------------------------------------------
             * 2- List User-Defined Attribute Names and Value Sizes.|
             * -----------------------------------------------------
             */
            out.println("\n2- List User-Defined Attribute Names and Value Sizes. "
                    + "\n------------------------------------------------------");

            for (String name : udfav.list()) {
               out.println(udfav.size(name) + " " + name);
            }

            out.println("\n--------------------------------------------------");

            /* 
             * ---------------------------------------------
             * 3- Get the Value of a User-Defined Attribute.|
             * ---------------------------------------------
             */
            out.println("\n3- Get the Value of a User-Defined Attribute. "
                    + "\n-----------------------------------------------\n");

            int size = udfav.size("file.description");
            ByteBuffer bb = ByteBuffer.allocateDirect(size);
            udfav.read("file.description", bb);
            bb.flip();

            out.println(Charset.defaultCharset().decode(bb).toString());

            /*
             * Note: Using the UserDefinedFileAttributeView.size() method, 
             * you can easily set the correct size of the buffer that represents 
             * the value of the user-defined attribute.
             * 
             * Note: You can also read an attribute by using the getAttribute() method. 
             * The value is returned as byte array (byte[]).
             * 
             */
            out.println("\n--------------------------------------------------");

            /* 
             * -----------------------------------------
             * 4- Delete a File’s User-Defined Attribute.|
             * -----------------------------------------
             */
            out.println("4- Delete a File’s User-Defined Attribute. "
                    + "\n-------------------------------------------------- \n");

            out.println("Attrs. before deletion.");
            for (String name : udfav.list()) {
               out.println(udfav.size(name) + " " + name);
            }

            udfav.delete("file.description");

            out.println("Attrs. after deletion.");
            for (String name : udfav.list()) {
               out.println(udfav.size(name) + " " + name);
            }

         } else {

            out.println(path.toAbsolutePath().toString() + ", Doesn't supprot user defined attributes.");
         }
      } catch (Exception e) {
         err.println(e);
      }
      out.println("\n-------------------------------------------------- \n");
   }

   private static void fileStoreAttributes() {
      out.println("\n ---------------------------- ");
      out.println("| 5.8. File Store Attributes |");
      out.println(" ---------------------------- \n");

      try {
         /* 
          * -------------------------------------
          * 1- Get Attributes of All File Stores.|
          * -------------------------------------
          */

         out.println(" Get Attributes of All File Stores "
                 + "\n ------------------------------------ \n");


         for (FileStore store : fs.getFileStores()) {

            long totalSpace = store.getTotalSpace() / 1024;
            long usedSpace = (store.getTotalSpace() - store.getUnallocatedSpace()) / 1024;
            long availableSpace = store.getUsableSpace() / 1024;
            boolean isReadOnly = store.isReadOnly();

            out.println("--- " + store.name() + " --- " + store.type());
            out.println("Total space: " + totalSpace);
            out.println("Used space: " + usedSpace);
            out.println("Available space: " + availableSpace);
            out.println("Is read only? " + isReadOnly);
         }
         out.println("\n --------------------------------------------------");

         /* 
          * ----------------------------------------------------------------
          * 2- 2-	Get Attributes of the File Store in Which a File Resides.|
          * ----------------------------------------------------------------
          */

         out.println("\n 2- Get Attributes of the File Store in Which a File Resides "
                 + "\n ------------------------------------------------------------- \n");

         FileStore store = Files.getFileStore(path);

         long totalSpace = store.getTotalSpace() / 1024 / 1024 / 1024;
         long usedSpace = (store.getTotalSpace() - store.getUnallocatedSpace()) / 1024 / 1024 / 1024;
         long availableSpace = store.getUsableSpace() / 1024 / 1024 / 1024;
         boolean isReadOnly = store.isReadOnly();

         out.println("--- " + store.name() + " --- " + store.type());
         out.println("Total space: " + totalSpace + " Gbyte");
         out.println("Used space: " + usedSpace + " Gbyte");
         out.println("Available space: " + availableSpace + " Gbyte");
         out.println("Is read only? " + isReadOnly);

         /* 
          * A file store may support one or more FileStoreAttributeView classes 
          * that provide a read-only or updatable view of a set of file store attributes, 
          * as follows:
          */

         FileStoreAttributeView fsav = store.getFileStoreAttributeView(FileStoreAttributeView.class);

      } catch (Exception e) {
         err.println(e);
      }
   }
}
