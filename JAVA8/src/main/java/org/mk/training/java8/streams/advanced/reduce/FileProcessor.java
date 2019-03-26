/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.java8.streams.advanced.reduce;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Stream;

/**
 *
 * @author mohit
 */
public interface FileProcessor {
    public String processStream(BufferedReader br) throws IOException;
}
