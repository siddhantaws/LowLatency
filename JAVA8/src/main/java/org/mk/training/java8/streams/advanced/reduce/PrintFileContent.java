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
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import java.util.stream.Stream;
import org.mk.training.java8.streams.advanced.BuildingStreams;

/**
 *
 * @author mohit
 */
public class PrintFileContent {

    
    public static void main(String[] args) throws IOException {
        String content=processFile("/../../../../eclipse-java/readme/readme_eclipse.html", new FileProcessor() {
            public String processStream(BufferedReader br) throws IOException {
                return br.readLine();
            }
        });
        System.out.println("Content:"+content);
        
        String oneline=processFile("/../../../../eclipse-java/readme/readme_eclipse.html",br->(br.readLine()));
        System.out.println("oneline:"+oneline);
        String fourlines=processFile("/../../../../eclipse-java/readme/readme_eclipse.html",br->(br.readLine()+br.readLine()+br.readLine()+br.readLine()));
        System.out.println("fourlines:"+fourlines);
    }
    
    public static String processFile(String relpath,FileProcessor p) throws IOException{
        System.out.println("processFile");
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream resourceStream = loader.getResourceAsStream("project.properties")) {
            props.load(resourceStream);
        } 

        String basedir = props.getProperty("project.base");
        
        
        StringBuilder sb = new StringBuilder(basedir);
        sb.append(relpath);
        
        try(BufferedReader br=new BufferedReader(new FileReader(sb.toString()))){
            return p.processStream(br);
        }
    }
}
