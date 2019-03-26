package org.mk.training.nio2.filedir;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import static java.lang.System.err;
import static java.lang.System.out;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import java.nio.file.Path;
import static java.nio.file.Paths.get;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

public class FileDirOperations {

   private static Path path = get("src/resources", "movielist.txt");

   public static void main(String[] args) {

      /* 
       * ------------------------------------------------
       * 7.1. Checking Methods for Files and Directories |
       * ------------------------------------------------
       */

      checkFilesAndDirectories();

      /* 
       * --------------------------------------
       * 7.2. Creating and Reading Directories |
       * --------------------------------------
       */

      creatingAndReadingDirectories();

      /* 
       * ------------------------------------------
       * 7.3. Creating, Reading, and Writing Files |
       * ------------------------------------------
       */

      creatingAndReadingWritingFiles();

      /* 
       * ---------------------------------------------
       * 7.4. Creating Temporary Directories and Files|
       * ---------------------------------------------
       */

      creatingTemporaryDirectoriesAndFiles();

      /* 
       * ---------------------------------------------------------
       * 7.5. Deleting, Copying, and Moving Directories and Files |
       * ---------------------------------------------------------
       */

      filesOperations();

   }

   private static void checkFilesAndDirectories() {
      try {

         out.println(" ------------------------------------------------ ");
         out.println("| 7.1. Checking Methods for Files and Directories |");
         out.println(" ------------------------------------------------ \n");

         out.println(" 1- Checking for the Existence of a File or Directory. "
                 + "\n ---------------------------------------------------- \n");
         /*
          * Note
          * ----
          * If both methods are applied to the same Path and both return false, then the checking cannot be 
          * performed. For example, if the application does not have access to the file, then the status is unknown and both 
          * methods return false. From here, it is easy to draw the conclusion that a file/directory's existence status can be: 
          * exist, not exist, or unknown.
          * 
          * Immedately after checking this status, the result is outdated, since a file that exists 
          * can be deleted just after check, therefore the result must "expire" immediately. 
          * 
          * If this method indicates the file exists then there is no guarantee that a subsequence access will succeed. 
          * 
          * In addition, a SecurityException may be thrown if one of these methods does not have permissions to read the file.
          * 
          */
         boolean pathExists = Files.exists(path, new LinkOption[]{NOFOLLOW_LINKS});

         boolean pathNotExists = Files.notExists(path, new LinkOption[]{NOFOLLOW_LINKS});

         out.println("Is file: " + path.toString() + " Exists ? " + pathExists);

         out.println("Is file: " + path.toString() + " not exists ? " + pathNotExists);

         out.println("\n------------------------------------------------------------\n");

         out.println("  2- Checking File Accessibility. "
                 + "\n ---------------------------------- \n");

         boolean isReadable = Files.isReadable(path);
         boolean isWritable = Files.isWritable(path);
         boolean isExecutable = Files.isExecutable(path);
         boolean isRegular = Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS);

         if ((isReadable) && (isWritable) && (isExecutable) && (isRegular)) {
            out.println("The checked file is accessible!");
         } else {
            out.println("The checked file is not accessible!");
         }

         // Or you can use shortcut version
         boolean isAccessible = Files.isRegularFile(path) & Files.isReadable(path)
                 & Files.isExecutable(path) & Files.isWritable(path);

         if (isAccessible) {
            out.println("The checked file is accessible 2!");
         } else {
            out.println("The checked file is not accessible 2!");
         }

         /*
          * Caution
          * --------
          * Even if these methods confirm the accessibility, there is no guarantee that the file can be accessed. 
          * 
          * The explanation resides in a well-known software bug, named time-of-check-to-time-of-use 
          * (TOCTTOU,pronounced “TOCK too”), which means that in the time between checking and using the checking result, the 
          * system may suffer different kinds of changes.
          * 
          * Unix fans are probably familiar with this concept, but it is applicable to any other system as well.
          * 
          */

         out.println("\n------------------------------------------------------------\n");

         out.println("  3- Checking the File Visibility. "
                 + "\n ------------------------------------ \n");

         boolean is_hidden = Files.isHidden(path);
         out.println("Is hidden ? " + is_hidden);

         out.println("\n------------------------------------------------------------\n");

         out.println(" 4- Checking If Two Paths Point to the Same File. "
                 + "\n ------------------------------------------------- \n");

         Path path1 = get("src/resources", "movielist.txt");
         Path path2 = get("src/resources/images/..", "movielist.txt");

         boolean is_same_file_12 = Files.isSameFile(path, path1);
         boolean is_same_file_13 = Files.isSameFile(path, path2);
         boolean is_same_file_23 = Files.isSameFile(path1, path2);

         out.println("is same file 1&2 ? " + is_same_file_12);
         out.println("is same file 1&3 ? " + is_same_file_13);
         out.println("is same file 2&3 ? " + is_same_file_23);

      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private static void creatingAndReadingDirectories() {
      try {

         out.println("\n -------------------------------------- ");
         out.println("| 7.2. Creating and Reading Directories |");
         out.println(" -------------------------------------- \n");

         out.println(" 1- Listing File System Root Directories. "
                 + "\n -----------------------------------------");

         /*
          * 
          * In Java 6, the file system root directories were extracted as an array of File objects. 
          * 
          * Starting with Java 7, NIO.2 gets the file system root directories as an Iterable of Path objects. 
          * This Iterable is returned by the "getRootDirectories(" method as follows:
          * 
          */
         out.println("Solution 1 \n---------- ");
         Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
         for (Path name : dirs) {
            out.println(name);
         }


         // Or You can easily get from Iterable into an array as follows:
         out.println("\nSolution 2 \n---------- ");
         ArrayList<Path> list = new ArrayList<>();
         for (Path name : dirs) {
            list.add(name);
         }


         Path[] arr = new Path[list.size()];
         list.toArray(arr);
         for (Path path : arr) {
            out.println(path);
         }

         //If you need to extract the file system root directories as an array of File, use the Java 6 solution:

         out.println("\nSolution 3 \n---------- ");
         File[] roots = File.listRoots();
         for (File root : roots) {
            out.println(root);
         }

         out.println("\n------------------------------------------------------------\n");

         out.println(" 2- Creating a New Directory. "
                 + "\n ----------------------------- \n");

         /* 
          * Note:
          * -----
          * If the directory exists, then the createDirectory() method will throw an exception.
          * 
          */

         Path newDir = get(path.getParent().toString(), "music");

         if (Files.notExists(newDir)) {
            newDir = Files.createDirectory(newDir);
            out.println("Newly created dir is: " + newDir.toString());
         } else {
            out.println("Dir: " + newDir.toString() + " is already existing.");
         }

         // To create more than just a single directory.

         Path newDires = get(newDir.getParent().toString(), "tulku", "my universe");


         newDires = Files.createDirectories(newDires);

         /*
          * 
          * Note: 
          * -----
          * If in the sequence of directories one or more directories already exist, then the createDirectories()
          * method will not throw an exception, but rather will just "jump" that directory and go to the next one. 
          * This method may fail after creation of some directories, but not all of them.
          * 
          */

         if (Files.exists(newDires)) {
            out.println("Dires: " + newDires.toString() + " are already existing.");

         } else {
            out.println("\nNewly created dirs are: " + newDires.toString());
         }

         out.println("\n------------------------------------------------------------\n");

         out.println(" 3- Listing a Directory’s Content as the following. "
                 + "\n ---------------------------------------------------- \n");
         /*
          * Working with directories and files usually involves looping a directory's content for different purposes. 
          * 
          * NIO.2 provides this facility through an iterable stream named "DirectoryStream", which is an interface 
          * that implements Iterable.
          */

         out.println("  Listing the Entire Content:\n");

         //no filter applied
         out.println("No filter applied:");
         try (DirectoryStream<Path> ds = Files.newDirectoryStream(path.getParent())) {
            for (Path file : ds) {
               out.println(file.getFileName());
            }
         }
         out.println("\n  --------------------------------");

         out.println("\n  Listing the Content by Applying a Glob Pattern:\n");

         /*
          * Sometimes, you may need to list only the content that meets certain criteria, 
          * which requires applying a filter to the directory's content.
          * 
          * Conforming to NIO.2 documentation, a glob pattern is just a string that is 
          * matched against other strings—in this case, directories and files names. Since this is a pattern, 
          * it must respect some rules, as follows:
          * 
          * • *: Represent (match) any number of characters, including none.
          * • **: Similar to *, but cross directories’ boundaries.
          * • ?: Represent (match) exactly one character.
          * • {}: Represent a collection of subpatterns separated by commas. For example,
          *       {A,B,C} matches A, B, or C.
          * • []: Convey a set of single characters or a range of characters if the hyphen character is present. 
          *       Some common examples include the following:
          *       • [0-9]: Matches any digit.
          *       • [A-Z]: Matches any uppercase letter.
          *       • [a-z,A-Z]: Matches any uppercase or lowercase letter.
          *       • [12345]: Matches any of 1, 2, 3, 4, or 5.
          * 
          * • Within the square brackets, *, ?, and \ match themselves.
          * • All other characters match themselves.
          * • To match *, ?, or the other special characters, you can escape them by using the 
          *   backslash character, \. For example, \\ matches a single backslash, and \?
          *   matches the question mark.
          * 
          */

         //glob pattern applied
         out.println("\nGlob pattern applied:");
         try (DirectoryStream<Path> ds = Files.newDirectoryStream(path.getParent(), "*.{txt}")) {
            for (Path file : ds) {
               out.println(file.getFileName());
            }
         }

         // Note: If passed path not a directory the newDirectoryStream() throws "java.nio.file.NotDirectoryException" exception.

         out.println("\n  --------------------------------");
         out.println("\n  Listing the Content by Applying a User-Defined Filter:\n");

         /*
          * If a glob pattern does not satisfy your needs, then is time to write your own filter.
          * This is a simple task that requires implementing the DirectoryStream.Filter<T> interface, 
          * which has a single method, named accept().
          */

         //user-defined filter - only directories are accepted
         DirectoryStream.Filter<Path> dirFilter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path path) throws IOException {
               return (Files.isDirectory(path, NOFOLLOW_LINKS));
            }
         };

         out.println("\nUser defined filter applied:");

         try (DirectoryStream<Path> ds = Files.newDirectoryStream(path.getParent(), dirFilter)) {
            for (Path file : ds) {
               out.println(file.getFileName());
            }
         }

         // The following list presents a set of commonly used filters:
         // • Filter that accepts only files/directories larger than 200KB:
         DirectoryStream.Filter<Path> sizeFilter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path path) throws IOException {
               return (Files.size(path) > 204800L);
            }
         };

         // • Filter that accepts only files modified in the current day:
         DirectoryStream.Filter<Path> timeFilter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path path) throws IOException {
               long currentTime = FileTime.fromMillis(System.currentTimeMillis()).to(TimeUnit.DAYS);
               long modifiedTime = ((FileTime) Files.getAttribute(path, "basic:lastModifiedTime",
                       NOFOLLOW_LINKS)).to(TimeUnit.DAYS);
               if (currentTime == modifiedTime) {
                  return true;
               }
               return false;
            }
         };

         // • Filter that accepts only hidden files/directories:
         DirectoryStream.Filter<Path> hiddenFilter = new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path path) throws IOException {
               return (Files.isHidden(path));
            }
         };



         out.println("\n------------------------------------------------------------\n");

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private static void creatingAndReadingWritingFiles() {
      try {

         out.println(" ------------------------------------------ ");
         out.println("| 7.3. Creating, Reading, and Writing Files |");
         out.println(" ------------------------------------------ \n");


         /*
          * 1- Using the Standard Open Options.
          * 
          * 
          * • READ               Opens file for read access.
          * • WRITE              Opens file for write access.
          * • CREATE             Creates a new file if it does not exist.
          * • CREATE_NEW         Creates a new file, failing with an exception if the file already exists.
          * • APPPEND            Appends data to the end of the file (used with WRITE and CREATE).
          * • DELETE_ON_CLOSE    Deletes the file when the stream is closed (used for deleting temporary files). 
          * • TRUNCATE_EXISTING  Truncates the file to 0 bytes (used with the WRITE option).
          * • SPARSE             Causes the newly created file to be sparse.
          * • SYNC               Keeps the file content and metadata synchronized with the underlying storage device.
          * • DSYNC              Keeps the file content synchronized with the underlying storage device.
          * 
          */

         out.println(" 2- Creating a New File. "
                 + "\n -----------------------");


         // (initially, the file must not exist; otherwise a "FileAlreadyExistsException" exception will be thrown

         Path newFile = path.resolveSibling("files/NIO2Wiki.txt");

         //Create the folder files first to avoid "java.nio.file.NoSuchFileException" exception.
         if (Files.notExists(newFile.getParent())) {
            Files.createDirectory(newFile.getParent());
         }


         newFile = Files.createFile(newFile);

         out.println("The newly created file path is: " + newFile.toString());

         /*
          * You can add a set of attributes at creation time as shown in the following code snippet. 
          * This code creates a new file on a POSIX file system that has specific permissions.
          */
         if (Files.getFileStore(path).supportsFileAttributeView(PosixFileAttributeView.class)) {
            newFile = newFile.resolveSibling("files/NewFileCreatedWithAttr.txt");

            Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);

            Files.createFile(newFile, attr);
         }

         out.println("\n------------------------------------------------------------\n");

         out.println(" 3- Writing a Small File. "
                 + "\n ------------------------");

         out.println(" 3.1- Writing bytes. "
                 + "\n -------------------");

         BufferedImage originalImage = ImageIO.read(path.resolveSibling("images/sdSample.png").toFile());
         byte[] imageInByte;

         try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(originalImage, "png", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
         }

         // Writing bytes
         Path image = Files.write(newFile.resolveSibling("newSDsample.png"), imageInByte);

         out.println("The image bytes written to file path is: " + image.toString());

         /*
          * Moreover, if you need to write text (String) and you want to use 
          * this method, then convert the text to a byte array.
          * 
          */

         String wiki = "JDK 7 includes a java.nio.file package which, with the java.nio.file.Path class (also new to JDK 7),\n"
                 + "among other features, provides extended capabilities for filesystem tasks,\n"
                 + "e.g. can work with symbolic/hard links and dump big directory listings "
                 + "into buffers more quickly than the old File class does.\n"
                 + "The java.nio.file package and its related package,\n"
                 + "java.nio.file.attribute, provide comprehensive support for file I/O and for accessing the file system.\n"
                 + "A zip file system provider is also available in JDK 7.";

         newFile = Files.write(newFile, wiki.getBytes("UTF-8"));

         out.println("The String bytes written to file path is: " + newFile.toString());

         out.println(" 3.2- Writing lines. "
                 + "\n -------------------");

         /*
          * Even if the above code works, it is much easier to use the "write()" method,
          * described next, to write text to files.
          */

         Charset charset = Charset.forName("UTF-8");
         List<String> lines = new ArrayList<>();
         lines.add(System.getProperty("line.separator"));
         lines.add("New Line");
         lines.add("Added to the file");
         lines.add("Using the write() lines method.");
         lines.add("Thanks NIO2.");

         // After each line, this method appends the platform’s line separator (line.separator system property).
         newFile = Files.write(newFile, lines, charset, StandardOpenOption.APPEND);

         out.println("The lines written to file path is: " + newFile.toString());

         out.println("\n------------------------------------------------------------\n");

         out.println(" 4- Reading a Small File. "
                 + "\n ------------------------\n");
         out.println(" 4.1- Reading bytes. "
                 + "\n -------------------");

         byte[] ballArray = Files.readAllBytes(image);
         Files.write(image.resolveSibling("bytes_to_sd.png"), ballArray);

         // Or you can use the ImageIO as follows.
         BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(ballArray));
         ImageIO.write(bufferedImage, "png", (image.resolveSibling("bytes_to_sd1.png")).toFile());

         //The readAllBytes() method can also read a text file.
         byte[] wikiArray = Files.readAllBytes(newFile);
         String wikiString = new String(wikiArray, "UTF-8");
         out.println(wikiString);

         out.println("\n 4.1- Reading lines. "
                 + "\n -------------------");

         lines = Files.readAllLines(newFile, charset);
         for (String line : lines) {
            out.println(line);
         }

         /*
          * Conforming to official documentation, this method recognizes the following as line terminators:
          * • \u000D followed by \u000A: CARRIAGE RETURN followed by LINE FEED
          * • \u000A: LINE FEED
          * • \u000D: CARRIAGE RETURN
          * 
          */

         out.println("\n------------------------------------------------------------\n");
         out.println(" 5- Working with streams. "
                 + "\n ------------------------");
         out.println("\n 5.1- Working with Buffered Streams. "
                 + "\n -------------------------------------");
         out.println("\n 5.1.1- Using the newBufferedWriter() Method. "
                 + "\n ----------------------------------------------");
         // The following code snippet uses a buffer to append data into the previously created NIO2Wiki.txt file.
         String name = "Pink Floyd";

         try (BufferedWriter writer = Files.newBufferedWriter(newFile, charset, StandardOpenOption.APPEND)) {

            writer.newLine();
            writer.append(name);
         }

         out.println("My name(" + name + ") is appended to the previously created wiki file.");

         out.println("\n 5.1.2- Using the newBufferedReader() Method. "
                 + "\n ----------------------------------------------");
         String line = null;
         try (BufferedReader reader = Files.newBufferedReader(newFile, charset)) {

            while ((line = reader.readLine()) != null) {
               out.println(line);
            }
         }

         out.println("\n 5.2- Working with unBuffered Streams. "
                 + "\n -----------------------------");
         out.println("\n 5.2.1- Using the newOutputStream() Method. "
                 + "\n --------------------------------------------");

         try (OutputStream os = Files.newOutputStream(newFile, StandardOpenOption.APPEND)) {

            os.write("\n newOutputStream() is used to write unbuffered output".getBytes());
         }

         out.println("Unbuffered stream was written successfully to file: " + newFile);

         //Moreover, if you decide that it is a better idea to use a buffered stream instead of the preceding code.

         try (OutputStream outputStream = Files.newOutputStream(newFile, StandardOpenOption.APPEND);
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            writer.write("\n new Appended text after conversion to buffered stream.");
         }

         out.println("\n 5.2.2- Using the newInputStream() Method. "
                 + "\n -------------------------------------------");

         int n = 0;
         try (InputStream in = Files.newInputStream(newFile)) {
            while ((n = in.read()) != -1) {
               out.print((char) n);
            }
         }
         out.println("\n++++++++++++++++++++++++++++++++++n");
         /*
          *As you probably already know from the java.io API, the InputStream class also provides a read() 
          * method that fills up a buffer array of type byte. Therefore, you can modify the preceding code as follows 
          * (keep in mind that you are still dealing with an unbuffered stream).
          * 
          * Note
          * ----
          * Calling the read(in_buffer) method is the same thing as calling the 
          *             read(in_buffer,0,in_buffer.length) method.
          * 
          */

         n = 0;
         byte[] inBuffer = new byte[1024];
         try (InputStream in = Files.newInputStream(newFile)) {
            while ((n = in.read(inBuffer)) != -1) {
               out.println(new String(inBuffer));
            }
         }
         out.println("\n++++++++++++++++++++++++++++++++++n");
         /*
          * Moreover, you can convert the unbuffered stream to a buffered stream by interoperating with the java.io API. 
          * The following example has the same effect as the preceding example, but it is more efficient:
          * 
          */

         try (InputStream in = Files.newInputStream(newFile);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            line = null;
            while ((line = reader.readLine()) != null) {
               out.println(line);
            }
         }
         out.println("\n------------------------------------------------------------\n");

      } catch (Exception e) {
         err.println(e);
      }
   }

   private static void creatingTemporaryDirectoriesAndFiles() {
      try {

         out.println(" --------------------------------------------- ");
         out.println("| 7.4. Creating Temporary Directories and Files |");
         out.println(" --------------------------------------------- \n");

         out.println(" 7.4.1 - Operation on Temporary Directories. "
                 + "\n -------------------------------------------\n");
         out.println(" 7.4.1.1 - Creating a temporary directory. "
                 + "\n -------------------------------------------");

         String prefix = "NIO2_";

         Path tempDir = Files.createTempDirectory(null);
         out.println("Temp dir without prefix: " + tempDir.toString());

         tempDir = Files.createTempDirectory(prefix);
         out.println("Temp dir with a prefix: " + tempDir.toString());

         /*
          * Note: 
          * -----
          * If you don’t know what the default location for temporary directories is,
          * you can use the following code:
          * 
          */

         out.println("The system default temp dir is: " + System.getProperty("java.io.tmpdir"));

         /*
          * Going further, you can specify the default directory in which a 
          * temporary directory is created by calling another createTempDirectory() method.
          */

         Path baseTempPath = get(path.getParent().toString(), "temp/dir");

         if (Files.notExists(baseTempPath)) {
            Files.createDirectories(baseTempPath);
         }

         final Path tempDir1 = Files.createTempDirectory(baseTempPath, prefix);

         out.println("The newly created Temp dir with a prefix 'NIO2' is: " + tempDir1.toString());

         // 7.4.1.2 - Deleting a Temporary Directory with Shutdown-Hook. "

         Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {

               /*
                * A directory cannot be deleted if it is not empty; therefore, you need to loop 
                * through the temporary directory content and delete each entry before deleting 
                * the temporary directory itself.
                * 
                */

               out.println("Deleting the temporary folder ...");
               try (DirectoryStream<Path> ds = Files.newDirectoryStream(tempDir1)) {
                  for (Path file : ds) {
                     Files.delete(file);
                  }
                  Files.delete(tempDir1);
               } catch (IOException e) {
                  err.println(e);
               }
               out.println("Shutdown-hook completed...");
            }
         });

         // 7.4.1.3 - Deleting a Temporary Directory with the deleteOnExit()"

         File asFile = tempDir.toFile();

         //EACH CREATED TEMPORARY ENTRY SHOULD BE REGISTERED FOR DELETE ON EXIT
         asFile.deleteOnExit();


         out.println("\n------------------------------------------------------------\n");
         out.println(" 7.4.2 - Operation on Temporary files. "
                 + "\n -------------------------------------------\n");
         out.println(" 7.4.2.1 - Creating a temporary file. "
                 + "\n -------------------------------------------");

         String sufix = ".txt";

         // pass null prefix / sufix -- if sufix is null then the default is ".tmp"

         Path tempFile = Files.createTempFile(null, null);

         out.println("Newly created temp file is with null (prefix/sufix):" + tempFile.toString());

         Path tempFile1 = Files.createTempFile(prefix, sufix);

         out.println("Newly created temp file is with (prefix/sufix):" + tempFile1.toString());

         // Specifying the default directory for the temp file creation

         final Path tempFile2 = Files.createTempFile(baseTempPath, prefix, sufix);

         // 7.4.2.2 - Deleting a Temporary File with Shutdown-Hook. "   
         Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
               try {
                  System.out.println("Deleting the temporary file ...");

                  Files.delete(tempFile2);

               } catch (IOException e) {
                  err.println(e);
               }
            }
         });


         // 7.4.2.3 - Deleting a Temporary File with the deleteOnExit()"

         tempFile.toFile().deleteOnExit();

         // 7.4.2.4 - Deleting a Temporary File with the deleteOnExit()"

         /*
          * An ingenious solution for deleting a temporary file is to use the 
          * DELETE_ON_CLOSE option. As its name suggests, this option deletes 
          * the file when the stream is closed.
          */

         try (OutputStream os = Files.newOutputStream(tempFile1, StandardOpenOption.DELETE_ON_CLOSE);
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {

            Thread.sleep(3000);
            //operations done
         }

         /*
          * Moreover, you can simulate a temporary file even without calling the createTempFile() method. 
          * Simply define a file name, and use the DELETE_ON_CLOSE option in conjunction with the CREATE option:
          * 
          * Files.newOutputStream(tmp_file, StandardOpenOption.CREATE, StandardOpenOption.DELETE_ON_CLOSE);
          */

         //simulate some I/O operations over the temporary file by sleeping 40 seconds
         //when the time expires, the temporary file is deleted
         Thread.sleep(40000);

      } catch (IOException | InterruptedException e) {
         err.println(e);
      }

      out.println("\n------------------------------------------------------------\n");
   }

   private static void filesOperations() {
      try {

         out.println(" --------------------------------------------------------- ");
         out.println("| 7.5. Deleting, Copying, and Moving Directories and Files |");
         out.println(" --------------------------------------------------------- \n");

         out.println("\n 7.5.1- Deleting Files and Directories."
                 + "\n ----------------------------------------");

         Path file = get(path.getParent().toString(), "files", "newCreatedFileToBeDeleted.txt");

         file = Files.createFile(file);

         out.println("Newly created file is: " + file.toString());

         Files.delete(file);

         boolean success = Files.deleteIfExists(file);

         out.println("Delete status: " + success);

         Files.deleteIfExists(get(path.getParent().toString(), "temp/dir"));
         Files.deleteIfExists(get(path.getParent().toString(), "temp"));

         if (Files.exists(get(path.getParent().toString(), "temp/dir"))) {
            out.println("Temp folder not deleted successfully.");
         } else {
            out.println("Temp folder deleted successfully.");
         }


         out.println("\n------------------------------------------------------------\n");

         out.println("\n 7.5.2- Copying Files and Directories:"
                 + "\n ----------------------------------------");
         out.println("\n 7.5.2.1- Copying Between Two Paths."
                 + "\n --------------------------------------");

         /*
          * Copying files and directories is a piece of cake in NIO.2. 
          * It provides three Files.copy() methods to accomplish this task and provides a 
          * set of options for controlling the copy process—the methods take a varargs argument 
          * represented by these options. These options are provided under the StandardCopyOption 
          * and LinkOption enums and are listed here:
          *  
          * • REPLACE_EXISTING: If the copied file already exists, then it is replaced 
          *                     (in the case of a nonempty directory, a FileAlreadyExistsException 
          *                     is thrown).When dealing with a symbolic link, the target of the link 
          *                     it is not copied; only the link is copied.
          *
          * • COPY_ATTRIBUTES:  Copy a file with its associated attributes (at least, the 
          *                     lastModifiedTime attribute is supported and copied).
          * 
          * • NOFOLLOW_LINKS:   Symbolic links should not be followed.
          * 
          */

         Path pathTo = get(path.getParent().toString(), "files", path.getFileName().toString());

         pathTo = Files.copy(path, pathTo, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES, NOFOLLOW_LINKS);

         out.println("The copied file path is: " + pathTo.toString() + "\n");

         out.println("\n--------------------------------------------\n");
         out.println("\n 7.5.2.2- Copying from an Input Stream to a File."
                 + "\n --------------------------------------------------");

         Path copyTo = get(pathTo.resolveSibling("newCopiedFile.txt").toString());

         try (InputStream is = new FileInputStream(path.toFile())) {
            Files.copy(is, copyTo, StandardCopyOption.REPLACE_EXISTING);

            is.close();
         }

         out.println("Copied file contents: \n" + Files.readAllLines(copyTo, Charset.forName("UTF-8")));
         out.println("\n--------------------------------------------\n");

         out.println("\n 7.5.2.3- Copying from a File to an Output Stream."
                 + "\n -----------------------------------------------------");

         try (OutputStream os = new FileOutputStream(copyTo.toFile())) {

            Files.copy(path, os);

            os.close();
         }

         out.println("\n------------------------------------------------------------\n");

         out.println("\n 7.5.3- Moving Files and Directories."
                 + "\n ----------------------------------------");

         /*
          * to move files and directories using the Files.move() method. 
          * This method gets the path to the file to move, the path to the target file, 
          * and a set of options that controls the moving process. These options are provided 
          * under the StandardCopyOption enum and are listed here:
          * 
          * • REPLACE_EXISTING: If the target file already exists, then the move is 
          *                     still performed and the target is replaced. 
          *                     When dealing with a symbolic link, the symbolic link 
          *                     is replaced but what it points to is not affected.
          * 
          * • ATOMIC_MOVE:      The file move will be performed as an atomic operation, which guarantees that 
          *                     any process that monitors the file’s directory will access a complete file.
          */

         Path movedFile = Files.move(copyTo, path.resolveSibling(copyTo.getFileName()), StandardCopyOption.REPLACE_EXISTING);

         out.println("Moved File is: " + movedFile.toString());

         out.println("\n------------------------------------------------------------\n");

         out.println("\n 7.5.4- Rename a File."
                 + "\n -----------------------");

         /*
          * Finally, with a little trick, you can rename a file using the Files.move() 
          * and Path.resolveSibling() methods.
          */

         Files.move(movedFile, movedFile.resolveSibling("newCopiedFile_Renamed.txt"), StandardCopyOption.REPLACE_EXISTING);

         out.println("\n------------------------------------------------------------\n");

      } catch (IOException | SecurityException e) {
         err.println(e);
      }
   }
}
