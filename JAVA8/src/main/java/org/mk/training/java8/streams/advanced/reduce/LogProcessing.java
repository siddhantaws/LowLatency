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
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;
import static java.util.Comparator.comparing;

/**
 *
 * @author mohit
 */
public class LogProcessing {

    public static void main(String[] args) throws IOException {
        //System.out.println("Wordcount:"+wordcount("/../../../../netbeans/LICENSE.txt"));
        //System.out.println("Wordcount:"+
                wordcount("/../../../../Work/BigData/input/apache/access_log");
        

    }

    public static void wordcount(String relpath) throws IOException {
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream resourceStream = loader.getResourceAsStream("project.properties")) {
            props.load(resourceStream);
        }
        String basedir = props.getProperty("project.base");
        StringBuilder sb = new StringBuilder(basedir);
        sb.append(relpath);
        
        //1
        try (Stream<String> lines = Files.lines(Paths.get(sb.toString()), Charset.defaultCharset()).onClose(() -> System.out.println("File closed"))) {
            Stream<ApacheAccessLog> logs=lines.map(ApacheAccessLog::parseFromLogLine)
                .filter(optaag->optaag.isPresent())
                .map(optaag->optaag.get());
            System.out.println(logs.collect(summarizingLong(ApacheAccessLog::getContentSize)).toString());
            
        }
         //2.
        try (Stream<String> lines = Files.lines(Paths.get(sb.toString()), Charset.defaultCharset()).onClose(() -> System.out.println("File closed"))) {
            Stream<ApacheAccessLog> logs=lines.map(ApacheAccessLog::parseFromLogLine)
                .filter(optaag->optaag.isPresent())
                .map(optaag->optaag.get());
        
            System.out.println(logs.map(log -> new SimpleEntry<>(log.getResponseCode(), 1))
                    .collect(groupingBy(e->e.getKey()
                            ,counting())));
        }
        //3.
        try (Stream<String> lines = Files.lines(Paths.get(sb.toString()), Charset.defaultCharset()).onClose(() -> System.out.println("File closed"))) {
            Stream<ApacheAccessLog> logs=lines.map(ApacheAccessLog::parseFromLogLine)
                .filter(optaag->optaag.isPresent())
                .map(optaag->optaag.get());
            System.out.println(logs.map(log -> new SimpleEntry<>(log.getIpAddress(), 1))
                    .collect(groupingBy(e->e.getKey()
                            ,counting())).entrySet().stream()
                    .filter(e->e.getValue()>10).collect(toList()));
        }
        
            //4.
        try (Stream<String> lines = Files.lines(Paths.get(sb.toString()), Charset.defaultCharset()).onClose(() -> System.out.println("File closed"))) {
            Stream<ApacheAccessLog> logs=lines.map(ApacheAccessLog::parseFromLogLine)
                .filter(optaag->optaag.isPresent())
                .map(optaag->optaag.get());
            System.out.println(logs.map(log -> log.getEndpoint())
                    .collect(groupingBy(ep->ep
                            ,counting())).entrySet().stream()
                    .sorted((e1,e2)->-e1.getValue().compareTo(e2.getValue())).filter(e->e.getValue()>10).collect(toList()));
        }
        
        //4.
        try (Stream<String> lines = Files.lines(Paths.get(sb.toString()), Charset.defaultCharset()).onClose(() -> System.out.println("File closed"))) {
            Stream<ApacheAccessLog> logs=lines.map(ApacheAccessLog::parseFromLogLine)
                .filter(optaag->optaag.isPresent())
                .map(optaag->optaag.get());
            System.out.println(logs.map(log -> log.getEndpoint())
                    .collect(groupingBy(ep->ep
                            ,counting())).entrySet().stream()
                    .sorted(comparing((Entry<String,Long> e)->e.getValue()).reversed()).filter(e->e.getValue()>10).collect(toList()));
        }
        
        //4.
        try (Stream<String> lines = Files.lines(Paths.get(sb.toString()), Charset.defaultCharset())
                .onClose(() -> System.out.println("File closed"))) {
            Stream<ApacheAccessLog> logs=lines.map(ApacheAccessLog::parseFromLogLine)
                .filter(optaag->optaag.isPresent())
                .map(optaag->optaag.get());
            System.out.println(logs.map(log -> log.getEndpoint())
                    .collect(groupingBy(ep->ep
                            ,counting())).entrySet().stream()
                    .sorted(comparing(Entry<String,Long>::getValue).reversed())
                    .filter(e->e.getValue()>10).collect(toList()));
        }
        
           
        
    }
    
}
