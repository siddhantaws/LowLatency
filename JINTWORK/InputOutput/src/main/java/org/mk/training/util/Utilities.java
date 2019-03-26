/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author mohit
 */
public class Utilities {
    private Utilities() {
    }
    
    public static String getTmpStoreLocation() throws IOException{
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream("project.properties")) {
            props.load(resourceStream);
        }
        String basedir = props.getProperty("project.base");
        StringBuilder sb=new StringBuilder(basedir);
        sb.append(File.separatorChar+".."+ File.separatorChar+".."+File.separatorChar+"resources"+File.separatorChar+"tmp");
        System.out.println("storelocation:"+sb.toString());
        return sb.toString();
        
    }
}
