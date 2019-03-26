package org.mk.training.nio2.path;

import java.io.File;
import java.io.IOException;
import static java.lang.System.err;
import static java.lang.System.out;
import java.net.URI;
import java.nio.file.FileSystem;
import static java.nio.file.FileSystems.getDefault;
import static java.nio.file.Files.isSameFile;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.Paths.get;

public class PathOperations {

    private static Path path = get("C:", "imdb/NIO2", "src/resources", "movielist.txt");

    public static void main(String[] args) {

        /* 
         * --------------------
         * 4.1 Defining a Path |
         * --------------------
         */

        definingAPath();

        /* 
         * -------------------------------------
         * 4.2 Getting Information about a Path |
         * -------------------------------------
         */

        gettingPathInformation();

        /* 
         * ---------------------
         * 4.3 Converting a Path  |
         * ---------------------
         */

        convertingAPath();

        /* 
         * -------------------------
         * 4.4 Combining Two Paths  |
         * -------------------------
         */

        combiningTwoPaths();

        /* 
         * -----------------------------------------------
         * 4.5 Constructing a Path between Two Locations  |
         * -----------------------------------------------
         */
        constructingPathBetweenTwoLocations();

        /* 
         * -------------------------
         * 4.6 Comparing Two Paths  |
         * -------------------------
         */

        comparingTwoPaths();
        /* 
         * ---------------------------------------------
         * 4.7 Iterate over the Name Elements of a Path |
         * ---------------------------------------------
         */

        getPathNamesElements();

    }

    private static void definingAPath() {

        out.println("--------------------- ");
        out.println("| 4.1 Defining a Path |");
        out.println("--------------------- \n");

        /* -------------------------------
         * 4.1.1- Define an Absolute Path |
         * -------------------------------
         * 
         * Alternative ways to get the path of same file.
         */

        Path absFilePath = Paths.get("C:/imdb/NIO2/src/resources", "movielist.txt");
        Path absFilePath1 = get("C:", "imdb/NIO2/src/resources", "movielist.txt");
        Path absFilePath2 = get("C:", "imdb/NIO2", "src/resources", "movielist.txt");
        Path absFilePath3 = get("C:", "imdb", "NIO2", "src", "resources", "movielist.txt");

        out.println("4.1.1- Define an Absolute Path");
        // output should be the same
        out.println(absFilePath.toString() + "\n" + absFilePath1.toString() + "\n" + absFilePath2.toString() + "\n" + absFilePath3.toString() + "\n");

        /* -----------------------------------------------------
         * 4.1.2- Define a Path Relative to the File Store Root |
         * -----------------------------------------------------
         * 
         * Relative path should start with file delimiter
         */

        Path relFilePath = get("/NIO2/src/resources", "movielist.txt");
        Path relFilePath1 = get("/NIO2", "src/resources", "movielist.txt");

        out.println("-----------------------------------------\n 4.1.2- Define a Path Relative to the File Store Root");
        // output should be the same
        out.println(relFilePath.toAbsolutePath() + "\n" + relFilePath1.toAbsolutePath() + "\n");


        /* ----------------------------------------------------
         * 4.1.3- Define a Path Relative to the Working Folder |
         * ----------------------------------------------------
         * 
         * Relative to working directory should not start with anything
         */


        Path wdFilePath = get("src/resources", "movielist.txt");
        Path wdFilePath1 = get("src", "resources", "movielist.txt");

        out.println("-----------------------------------------\n 4.1.3- Define a Path Relative to the Working Folder");
        // output should be the same
        out.println(wdFilePath.toAbsolutePath() + "\n" + wdFilePath1.toAbsolutePath() + "\n");

        /* -------------------------------------
         * 4.1.4- Define a Path Using Shortcuts |
         * -------------------------------------
         */

        Path scFilePath = get("C:/imdb/NIO2/src/resources/dumy/../movielist.txt").normalize();
        Path scFilePath1 = get("C:/imdb/NIO2/./src/resources/dumy/../movielist.txt").normalize();
        /*
         * If you want to see the effect of the normalize() method, try to define the 
         * same Path with and without normalize(), as follows, and print the result to the console:
         */
        Path noNormalize = get("C:/imdb/NIO2/./src/resources/dumy/../movielist.txt");
        Path normalize = get("C:/imdb/NIO2/./src/resources/dumy/../movielist.txt").normalize();

        out.println("-----------------------------------------\n 4.1.4- Define a Path Using Shortcuts");

        out.println(scFilePath.toAbsolutePath() + "\n" + scFilePath1.toAbsolutePath());

        out.println("\n Deferrence between None Normalized and Normalized paths: \n" + noNormalize.toAbsolutePath() + "\n" + normalize.toAbsolutePath() + "\n");

        /* --------------------------------
         * 4.1.5- Define a Path from a URI |
         * --------------------------------
         * 
         * To create URI from a given path use URI.create() method.
         */

        Path uriFilePath = get(URI.create("file:///imdb/NIO2/src/resources/movielist.txt"));
        Path uriFilePath1 = get(URI.create("file:///C:/imdb/NIO2/src/resources/movielist.txt"));

        out.println("-----------------------------------------\n 4.1.5- Define a Path from a URI:");

        out.println(uriFilePath.toAbsolutePath() + "\n" + uriFilePath1.toAbsolutePath());

        /* ---------------------------------------------------------------------
         * 4.1.6- Define a Path using FileSystems.getDefault().getPath() Method |
         * ---------------------------------------------------------------------
         */

        FileSystem fs = getDefault();

        Path fsFilePath = fs.getPath("/imdb/NIO2/src/resources/movielist.txt");
        Path fsFilePath1 = fs.getPath("src/resources/movielist.txt");
        Path fsFilePath2 = fs.getPath("C:\\imdb\\NIO2", "src\\resources\\movielist.txt");
        Path fsFilePath3 = fs.
                getPath("\\imdb\\NIO2\\.\\src\\resources\\dumy\\..\\movielist.txt").normalize();

        out.println("-----------------------------------------\n 4.1.6- Define a Path using FileSystems.getDefault().getPath() Method");
        // output should be the same
        out.println(fsFilePath.toAbsolutePath() + "\n" + fsFilePath1.toAbsolutePath() + "\n" + fsFilePath2 + "\n" + fsFilePath3.toAbsolutePath() + "\n");


        /* ------------------------------------------
         * 4.1.7- Get the Path of the Home Directory |
         * ------------------------------------------
         * 
         * The returned home directory is dependent on each machine and each operating system
         */

        Path path1 = get(System.getProperty("user.home"), "downloads", "game.exe");

        out.println("-----------------------------------------\n 4.1.7- Get the Path of the Home Directory");

        out.println(path1 + "\n");

    }

    private static void gettingPathInformation() {

        out.println("\n-------------------------------------");
        out.println("| 4.2 Getting Information about a Path |");
        out.println("-------------------------------------\n");

        /* ----------------------------------------
         * 4.2.1- Get the Path File/Directory Name |
         * ----------------------------------------
         * 
         */

        out.println("4.2.1- Get the Path File/Directory Name");

        out.println("The file/directory indicated by path: " + path.getFileName() + "\n");

        /* -------------------------
         * 4.2.2- Get the Path Root |
         * -------------------------
         * if the Path does not have a root, it returns 'null'
         */

        out.println("-----------------------------------------\n 4.2.2- Get the Path Root");

        out.println("Root of this path: " + path.getRoot() + "\n");


        /* ---------------------------
         * 4.2.3- Get the Path Parent |
         * ---------------------------
         * 
         * if the Path does not have a parent, it returns 'null'
         */


        out.println("-----------------------------------------\n 4.2.3- Get the Path Parent");

        out.println("Parent: " + path.getParent() + "\n");


        /* ------------------------------
         * 4.2.4- Get Path Name Elements |
         * ------------------------------
         * 
         * You can get the number of elements in a path with the getNameCount() method 
         * and get the name of each element with the getName() method
         */

        out.println("-----------------------------------------\n 4.2.4- Get Path Name Elements \n");

        for (int i = 0; i < path.getNameCount(); i++) {
            out.println("Name element of " + i + " is: " + path.getName(i) + "\n");
        }

        /* --------------------------
         * 4.2.5- Get a Path Subpath |
         * --------------------------
         * 
         * You can extract a relative path with the subpath() method, which gets 
         * two parameters, the start index and the end index, representing 
         * the subsequence of elements
         */

        out.println("-----------------------------------------\n 4.2.5- Get a Path Subpath \n");

        out.println("subpath(1, 3) Is :" + path.subpath(1, 3) + "\n");

    }

    private static void convertingAPath() {

        out.println("\n-----------------------");
        out.println("| 4.3 Converting a Path |");
        out.println("-----------------------\n");

        /* -----------------------------------
         * 4.3.1- Convert a Path to a String. |
         * -----------------------------------
         * 
         * String conversion of a path can be achieved by the toString() method
         */

        out.println("4.3.1- Convert a Path to a String.");

        String filePath = path.toString();

        out.println("File Path is: " + filePath + "\n");

        /* -------------------------------
         * 4.3.2- Convert a Path to a URI |
         * -------------------------------
         */

        out.println("-----------------------------------------\n 4.3.2- Convert a Path to a URI");

        URI fileUri = path.toUri();

        out.println("URI of the file path is: " + fileUri.toString() + "\n");


        /* ----------------------------------------------------
         * 4.3.3- Convert a Relative Path to an Absolute Path. |
         * ----------------------------------------------------
         */

        out.println("-----------------------------------------\n 4.3.3- Convert a Relative Path to an Absolute Path");

        Path relPath = get("src/resources", "movielist.txt");
        out.println("Absolute Path: " + relPath.toAbsolutePath() + "\n");


        /* --------------------------------------
         * 4.3.4- Convert a Path to a Real Path. |
         * --------------------------------------
         * 
         * The toRealPath() method returns the real path of an existing file—this 
         * means that the file must exist, which is not necessary if you use the 
         * toAbsolutePath() method. 
         * 
         * If no argument is passed to this method and the file system supports 
         * symbolic links, this method resolves any symbolic links in the path.
         * 
         * If you want to ignore symbolic links, then pass to the method the 
         * LinkOption.NOFOLLOW_LINKS enum constant.
         * 
         * Moreover, if the Path is relative, it returns an absolute path, 
         * and if the Path contains any redundant elements, it returns a path 
         * with those elements removed. 
         * 
         * This method throws an IOException if the file does not exist or cannot 
         * be accessed.
         */

        out.println("-----------------------------------------\n 4.3.4- Convert a Path to a Real Path. \n");
        try {
            out.println("java.io.File path is: " + relPath.toRealPath(NOFOLLOW_LINKS) + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        /* -------------------------------
         * 4.3.5- Convert a Path to a File |
         * -------------------------------
         * 
         * A Path can also be converted to a File object using the toFile() method. 
         * This a great bridge between Path and File since the File class also 
         * contains a method named toPath() for reconversion.
         */

        out.println("-----------------------------------------\n 4.3.5- Convert a Path to a File \n");


        File path_to_file = path.toFile();

        Path file_to_path = path_to_file.toPath();

        out.println("Path to file name: " + path_to_file.getName());
        out.println("File to path: " + file_to_path.toString());

    }

    private static void combiningTwoPaths() {

        out.println("\n------------------------");
        out.println("| 4.4 Combining Two Paths |");
        out.println("--------------------------\n");

        Path base = get("C:/imdb/NIO2/src/resources");

        Path path1 = base.resolve("movielist.txt");

        out.println(path1);

        Path path2 = base.resolve("CON2718.txt");

        out.println(path2);

        /*
         * There is also a method dedicated to sibling paths, named resolveSibling().
         * 
         * It resolves the passed path against the current path’s parent path.
         * 
         * Practically, this method replaces the file name of the current path 
         * with the file name of the given path.
         */

        //define the fixed path
        Path fixedBase = get("C:/imdb/NIO2/src/resources/movielist.txt");

        //resolve sibling movielist_Copy.txt file
        Path sibPath = fixedBase.resolveSibling("CON2718.txt");

        out.println(sibPath);

    }

    private static void constructingPathBetweenTwoLocations() {

        out.println("\n-----------------------------------------------");
        out.println("| 4.5 Constructing a Path between Two Locations |");
        out.println("-----------------------------------------------\n");

        Path path01 = get("movielist.txt");
        Path path02 = get("CON2718.txt");

        /*
         * In this case, it is assumed that movielist.txt and CON2718.txt are siblings, 
         * which means that you can navigate from one to the other by going 
         * up one level and then down one level. 
         * Applying the 'relativize()' method outputs ..\CON2718.txt and ..\movielist.txt.
         */

        Path path01_to_path02 = path01.relativize(path02);

        out.println(path01_to_path02);

        Path path02_to_path01 = path02.relativize(path01);

        out.println(path02_to_path01);

        //Another typical situation involves two paths that contain a root element.

        path01 = get("/JavaOne/2011", "movielist.txt");
        path02 = get("/JavaOne/2012");

        /*
         * In this case, both paths contain the same root element, /imdb/NIO2/src. 
         * To navigate from path01 to path02, you will go up two levels and down one level (..\..\2011). 
         * To navigate from path02 to path01, you will go up one level and down two levels (..\2011\movielist.txt).
         * 
         */

        path01_to_path02 = path01.relativize(path02);
        out.println(path01_to_path02);

        path02_to_path01 = path02.relativize(path01);
        out.println(path02_to_path01);

    }

    private static void comparingTwoPaths() {

        out.println("\n------------------------- ");
        out.println("| 4.6 Comparing Two Paths. |");
        out.println("------------------------- \n");

        /*
         * You can test whether two paths are equal by calling the Path.equals() method.
         * This method respects the Object.equals() specification.
         * 
         * So don't use it when comparing paths.
         */

        Path path01 = get("src/resources", "movielist.txt");
        Path path02 = get("../NIO2", "src/resources", "movielist.txt");

        if (path01.equals(path02)) {
            out.println("The paths are equal!");
        } else {
            out.println("The paths are not equal!");
        } //true


        /*
         * to check if two paths are the same file/folder. 
         * You can easily accomplish this by calling the java.nio.file.Files.isSameFile() method.
         * 
         * Notice: that this method requires that the compared files exist on the file 
         * system; otherwise, it throws an IOException.
         */


        try {
            if (isSameFile(path01, path02)) {
                out.println("The paths locate the same file!");
            } //true
            else {
                out.println("The paths does not locate the same file!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * Since the Path class implements the Comparable interface, you can compare paths 
         * by using the compareTo() method, which compares two abstract paths lexicographically.
         * 
         * This can be useful for sorting. 
         * 
         * 1- It returns zero if the argument is equal to this path,
         * 2- A value less than zero if this path is lexicographically less than the argument,
         * 3- Or a value greater than zero if this path is lexicographically greater than the argument.
         */

        int compare = path01.compareTo(path02);
        out.println(compare);

        compare = path01.compareTo(path);
        out.println(compare);

        compare = path02.compareTo(path01);
        out.println(compare);


        /*
         * Partial comparison can be accomplished by using the startsWith() and endsWith() methods.
         */
        out.println("Is path ends with 'movielist.txt' " + path.endsWith("movielist.txt"));
        out.println("Is path starts with 'JavaOne2020' " + path.startsWith("movielist.txt"));
    }

    private static void getPathNamesElements() {

        out.println("\n---------------------------------------------");
        out.println("| 4.7 Iterate over the Name Elements of a Path |");
        out.println("-----------------------------------------------\n");

        out.println("This outputs the elements starting with the closest to the root, as follows:\n");
        for (Path name : path) {
            out.println(name.toString());
        }
    }
}
