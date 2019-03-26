package org.mk.training.java8.streams.advanced;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.IntSupplier;
import java.util.stream.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildingStreams {

    public static void main(String... args){

        // Stream.of
        Stream<String> stream = Stream.of("Java 8", "Lambdas", "are", "really", "cool");
        stream.map(String::toUpperCase).forEach(System.out::println);
        System.out.println("---");
        // Stream.empty
        Stream<String> emptyStream = Stream.empty();

        // Arrays.stream
        int[] numbers = {2, 3, 5, 7, 11, 13};
        System.out.println(Arrays.stream(numbers).sum());
        System.out.println("---");
        // Stream.iterate
        Stream.iterate(0, n -> n + 2)
                .limit(10)
                .forEach(System.out::println);
        System.out.println("---");
        // fibonnaci with iterate
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
                .forEach(t -> System.out.println("(" + t[0] + ", " + t[1] + ")"));
        System.out.println("---");
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
                .map(t -> t[0])
                .forEach(System.out::println);
        System.out.println("---");
        // random stream of doubles with Stream.generate
        Stream.generate(Math::random)
                .limit(10)
                .forEach(System.out::println);
        System.out.println("---");
        // stream of 1s with Stream.generate
        IntStream.generate(() -> 1)
                .limit(5)
                .forEach(System.out::println);
        System.out.println("---");
        IntStream.generate(new IntSupplier() {
            public int getAsInt() {
                return 2;
            }
        }).limit(5)
                .forEach(System.out::println);
        System.out.println("---");
        IntSupplier fib = new IntSupplier() {
            private int previous = 0;
            private int current = 1;

            public int getAsInt() {
                int nextValue = this.previous + this.current;
                this.previous = this.current;
                this.current = nextValue;
                return this.previous;
            }
        };
        IntStream.generate(fib).limit(10).forEach(System.out::println);
        System.out.println("---");
        
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream resourceStream = loader.getResourceAsStream("project.properties")) {
            props.load(resourceStream);
        } catch (IOException ex) {
            Logger.getLogger(BuildingStreams.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String basedir = props.getProperty("project.base");
        StringBuilder sb = new StringBuilder(basedir);
        sb.append(File.separatorChar + ".." + File.separatorChar + ".." + File.separatorChar + "resources" + File.separatorChar
                + "netbeans" + File.separatorChar + "LICENSE.txt");
        
        
        long uniqueWords=0;
        try (Stream<String> lines=Files.lines(Paths.get(sb.toString()), Charset.defaultCharset())){
            uniqueWords = lines
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();
        } catch (IOException ex) {
            Logger.getLogger(BuildingStreams.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("There are " + uniqueWords + " unique words in LICENSE.txt");

    }
}
