/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.accesspatterns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.Properties;
import org.mk.training.util.Utilities;

/**
 *
 * @author mohit
 */
public class CacheLineDeducePatterns {

    private static final int INT_SIZE = 4;
    private static final int ONE_GIG = 1024 * 1024 * 1024;
    private static final long TWO_GIG = 2L * ONE_GIG;
    private static final int ARRAY_SIZE = (int) (TWO_GIG / INT_SIZE);
    private static final int[] memory = new int[ARRAY_SIZE];
    private static int[] stridesize = {1, 2, 3, 7, 15, 31, 63, 127, 255, 511};
    public static void main(final String[] args) throws IOException {
        long[] durations = new long[stridesize.length];
        for (int i = 0; i < stridesize.length; i++) {
            durations[i] = perfTest(stridesize[i]);

        }
        try (FileWriter fw = new FileWriter(Utilities.getTmpStoreLocation()+"/cachelinededuceresults.txt")) {
            for (int i = 0; i < durations.length; i++) {
                fw.write(Integer.toString(stridesize[i]) + "," + durations[i] + "\n");

            }
        }
    }
    public static long perfTest(int k) {
        long start = System.nanoTime();
        for (int i = 0; i < memory.length; i += k) {
            memory[i] *= 3;
        }
        long end = System.nanoTime();
        long duration = end - start;

        System.out.format("%d - %.2fns \n",
                Long.valueOf(k),
                Double.valueOf((duration)));
        return duration;
    }
    static {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            memory[i] = 777;
        }
    }

}
