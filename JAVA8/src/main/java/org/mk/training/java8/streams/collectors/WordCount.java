/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.java8.streams.collectors;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.mk.training.java8.streams.advanced.BuildingStreams;
import static java.util.stream.Collectors.*;
import java.util.stream.Collectors;

/**
 *
 * @author mohit
 */
public class WordCount {

    public static void main(String... args) {
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream resourceStream = loader.getResourceAsStream("project.properties")) {
            props.load(resourceStream);
        } catch (IOException ex) {
            Logger.getLogger(BuildingStreams.class.getName()).log(Level.SEVERE, null, ex);
        }

        String basedir = props.getProperty("project.base");
        StringBuilder sb = new StringBuilder(basedir);
        sb.append(File.separatorChar + ".." + File.separatorChar + ".." + File.separatorChar+".." + File.separatorChar
                + "netbeans" + File.separatorChar + "LICENSE.txt");

        //long uniqueWords=0;
        try (Stream<String> lines = Files.lines(Paths.get(sb.toString()), Charset.defaultCharset())) {
            //uniqueWords = 
            Map<String, Long> wordcount = lines
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    //.map(word->new Pair(word, 1))

                    .collect(groupingBy(Function.identity(), counting()));
            System.out.println("" + wordcount);
        } catch (IOException ex) {
            Logger.getLogger(BuildingStreams.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Stream<String> lines = Files.lines(Paths.get(sb.toString()), Charset.defaultCharset())) {
            //uniqueWords = 
            Map<String, Long> wordcount = lines
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    .map(word -> new SimpleEntry<>(word, 1))
                    .reduce(new HashMap<>(), (acc, entry) -> {
                        acc.put(entry.getKey(), acc.compute(entry.getKey(), (k, v) -> v == null ? 1 : v + 1));
                        return acc;
                    }, (m1, m2) -> m1);
            System.out.println("word count:" + wordcount);
        } catch (IOException ex) {
            Logger.getLogger(BuildingStreams.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
}
