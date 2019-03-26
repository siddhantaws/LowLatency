/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.accesspatterns;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.mk.training.util.Utilities;

/**
 *
 * @author mohit
 */
public class CacheSizeDeducePatterns {

    private static final int LONG_SIZE = 8;
    private static final int ONE_KILO = 1024;
    private static long[] memorysize = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024,
        2 * ONE_KILO, 3 * ONE_KILO, 4 * ONE_KILO,6 * ONE_KILO, 8 * ONE_KILO, 16 * ONE_KILO,
        32 * ONE_KILO, 64 * ONE_KILO, 128 * ONE_KILO, 256 * ONE_KILO,
        512 * ONE_KILO, 1024 * ONE_KILO, 2 * 1024 * ONE_KILO};
    private static final int[] strides = {1, 3, 7, 15 ,31, 63,127,255,513};
    
    private static final int steps = 64 * 1024 * 1024;
    private static double[][] durstrides;
    private static final String RUNALL = "all";

    public static void main(final String[] args) throws IOException {
        List<Integer> stridestorun = initialize(args);

        double[] durations = null;
        for (int j = 0; j < durstrides.length; j++) {
            durations = durstrides[j];
            int stride = stridestorun.get(j);
            System.out.println("Running Stride :" + stride);
            for (int i = 0; i < memorysize.length; i++) {
                durations[i] = perfTest(memorysize[i], stride);

            }
        }

        StringBuilder header = new StringBuilder();
        StringBuilder filename=new StringBuilder();
        header.append("workingset");
        for (int stride : stridestorun) {
            header.append(",");
            header.append("Stride"+Integer.toString(stride));
            filename.append(Integer.toString(stride));
        }
        header.append("\n");
        FileWriter fw = new FileWriter(Utilities.getTmpStoreLocation() + "/"+filename.toString()+"cachesizededuceresults.txt");
        fw.write(header.toString());
        for (int i = 0; i < memorysize.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            for (int j = 0; j < durstrides.length; j++) {
                sb.append("," + Double.toString(durstrides[j][i]));
            }
            sb.append("\n");
            fw.write(sb.toString());
        }

        fw.close();

    }

    public static double perfTest(long k, int stride) {
        long[] array = initArray(k);
        return perfTestArray(k, array, stride);
    }

    public static double perfTestArray(long k, long[] array, int stride) {
        int lengthMod = array.length - 1;
        long start = System.nanoTime();

        for (int i = 0; i < steps; i++) {
            array[(i * stride) & lengthMod]++; // (x & lengthMod) is equal to (x % arr.Length)
        }
        long end = System.nanoTime();
        long duration = end - start;
        System.out.println("duration:" + duration);
        Double nanossperelement = Double.valueOf((duration / steps));

        System.out.format("Size:%d,Stride:%d - %.2fns \n",
                Long.valueOf(k),
                Long.valueOf(stride),
                nanossperelement);
        return nanossperelement;
    }

    public static long[] initArray(long size) {
        long arraysize = size * ONE_KILO / LONG_SIZE;
        long array[] = new long[(int) arraysize];
        return array;
    }

    private static List<Integer> initialize(String... args) {
        if (args.length == 0) {
            throwStartupMessage();
        }
        List<Integer> stridestorun = new ArrayList<>(6);
        for (int i = 0; i < args.length; i++) {
            String string = args[i];
            try {
                int stride = Integer.parseInt(string);
                if (Arrays.binarySearch(strides, stride) >= 0) {
                    stridestorun.add(stride);
                }
            } catch (NumberFormatException nfe) {
                if (RUNALL.equalsIgnoreCase(string)) {
                    stridestorun.clear();
                    for (int j = 0; j < strides.length; j++) {
                        int stride = strides[j];
                        stridestorun.add(stride);
                    }
                    break;
                } else {
                    throwStartupMessage();
                }
            }
        }
        durstrides = new double[stridestorun.size()][];
        Collections.sort(stridestorun);
        for (int i = 0; i < stridestorun.size(); i++) {
            int stride = stridestorun.get(i);
            durstrides[i] = new double[memorysize.length];
        }
        return stridestorun;
    }

    private static void throwStartupMessage() {
        System.out.println("Available Memory Strides are " + (strides.toString()) + " " + "or all");
        System.exit(0);
    }
}
