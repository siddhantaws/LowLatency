/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.java8.streams.advanced.reduce;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;

/**
 *
 * @author mohit
 */
public class WorkingWithReduce {

    public static void main(String[] args) throws IOException {
        System.out.println("Wordcount:"+wordcount("/../../../../netbeans/LICENSE.txt"));
        System.out.println("Wordcount:"+wordcountv2("/../../../../eclipse-java/readme/readme_eclipse.html"));
        

    }

    public static Map<String, Long> wordcount(String relpath) throws IOException {
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream resourceStream = loader.getResourceAsStream("project.properties")) {
            props.load(resourceStream);
        }
        String basedir = props.getProperty("project.base");
        StringBuilder sb = new StringBuilder(basedir);
        sb.append(relpath);
        
        try (Stream<String> lines = Files.lines(Paths.get(sb.toString()), Charset.defaultCharset()).onClose(() -> System.out.println("File closed"))) {
            //return lines;
            Map<String, Long> wordcount=
            lines.flatMap(line -> Arrays.stream(line.split(" ")))
            .map(word -> new AbstractMap.SimpleEntry<>(word, 1))
            .reduce(new HashMap<String, Long>(), (acc, entry) -> {
                        acc.put(entry.getKey(), acc.compute(entry.getKey(), (k, v) -> v == null ? 1 : v + 1));
                        return acc;
                    }, (m1, m2) -> m1);
            return wordcount;
        }
    }
    public static Map<String, Long> wordcountv2(String relpath) throws IOException {
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream resourceStream = loader.getResourceAsStream("project.properties")) {
            props.load(resourceStream);
        }
        String basedir = props.getProperty("project.base");
        StringBuilder sb = new StringBuilder(basedir);
        sb.append(relpath);
        
        try (Stream<String> lines = Files.lines(Paths.get(sb.toString()), Charset.defaultCharset()).onClose(() -> System.out.println("File closed"))) {
            //return lines;
            Map<String, Long> wordcount=
            lines.flatMap(line -> Arrays.stream(line.split(" ")))
            .map(word -> new AbstractMap.SimpleEntry<>(word, 1))
            .collect(groupingBy(AbstractMap.SimpleEntry::getKey,counting()));
            return wordcount;
        }
    }
    
    /*public static Map<String, Long> wordcountv3(String relpath) throws IOException {
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream resourceStream = loader.getResourceAsStream("project.properties")) {
            props.load(resourceStream);
        }
        String basedir = props.getProperty("project.base");
        StringBuilder sb = new StringBuilder(basedir);
        sb.append(relpath);
        
        try (Stream<String> lines = Files.lines(Paths.get(sb.toString()), Charset.defaultCharset()).onClose(() -> System.out.println("File closed"))) {
            //return lines;
            Map<String, Long> wordcount=
            lines.flatMap(line -> Arrays.stream(line.split(" ")))
            .map(word -> new AbstractMap.SimpleEntry<>(word, 1))
                       .collect(toMap(e -> e.getKey(), e -> e.getValue(), (v1, v2) -> v1 + v2));

            return wordcount;
        }
    }*/
}
