/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.buffersize;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author mohit
 */
public class BufferSizeTest {
    public static final double RUN_TIME_NS = 1e9;

    
    public static void main(String args[]) throws IOException {
        for (int i = 1; i <= 1024 * 1024; i *= 2) {
            testBufferSize(i);
        }
    }

    private static void testBufferSize(int size) throws IOException {
        int fileSize = 16 * 1024 * 1024;

        byte[] bytes = new byte[size];
        File tmpFile = File.createTempFile("deleteme", "zeros");
        tmpFile.deleteOnExit();

        // test writing an unbuffered file.
        int count1 = 0;
        long start1 = System.nanoTime();
        do {
            count1++;
            OutputStream out = new FileOutputStream(tmpFile);
            for (int i = 0; i < fileSize; i += size)
                out.write(bytes);
            out.close();
        } while (start1 + RUN_TIME_NS > System.nanoTime());
        long time1 = (System.nanoTime() - start1) / count1;

        // test writing a buffered file.
        int count2 = 0;
        long start2 = System.nanoTime();
        do {
            count2++;
            OutputStream out = new BufferedOutputStream(new FileOutputStream(tmpFile));
            for (int i = 0; i < fileSize; i += size)
                out.write(bytes);
            out.close();
        } while (start2 + RUN_TIME_NS > System.nanoTime());
        long time2 = (System.nanoTime() - start2) / count2;

        // test reading unbuffered file.
        int count3 = 0;
        long start3 = System.nanoTime();
        do {
            count3++;
            InputStream in = new FileInputStream(tmpFile);
            while (in.read(bytes) > 0) ;
            in.close();
        } while (start3 + RUN_TIME_NS > System.nanoTime());
        long time3 = (System.nanoTime() - start3) / count3;

        // test reading buffered file.
        int count4 = 0;
        long start4 = System.nanoTime();
        do {
            count4++;
            InputStream in = new BufferedInputStream(new FileInputStream(tmpFile));
            while (in.read(bytes) > 0) ;
            in.close();
        } while (start4 + RUN_TIME_NS > System.nanoTime());
        long time4 = (System.nanoTime() - start4) / count4;

        long factor = 16 * 1000 * 1000 * 1000L; // 16 MB/ns in MB/s
        System.out.printf("<tr><td align=\"right\">%,d"
                + "</td><td align=\"right\">%,d MB/s"
                + "</td><td align=\"right\">%,d MB/s"
                + "</td><td align=\"right\">%,d MB/s"
                + "</td><td align=\"right\">%,d MB/s"
                + "</td></tr>%n", size,
                factor / time1, factor / time2, factor / time3, factor / time4
        );
    }
}
