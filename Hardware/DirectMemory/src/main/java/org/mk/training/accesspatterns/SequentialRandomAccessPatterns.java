/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.accesspatterns;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import static org.mk.training.accesspatterns.CacheSizeDeducePatterns.initArray;
import static org.mk.training.accesspatterns.CacheSizeDeducePatterns.perfTest;
import static org.mk.training.accesspatterns.CacheSizeDeducePatterns.perfTestArray;
import org.mk.training.util.Utilities;
import sun.misc.Unsafe;

/**
 *
 * @author mohit
 */
public class SequentialRandomAccessPatterns {
    private static final Unsafe unsafe;
    static
    {
        try
        {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe)field.get(null);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    private static final ThreadLocalRandom rand = ThreadLocalRandom.current();
    private static final int LONG_SIZE = 8;
    private static final int ONE_KILO = 1024;
    private static final int PRIME_INC = 1033499;
    private static long[] memorysize = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024,
        2 * ONE_KILO, 3 * ONE_KILO, 4 * ONE_KILO,6 * ONE_KILO, 8 * ONE_KILO, 16 * ONE_KILO,
        32 * ONE_KILO, 64 * ONE_KILO, 128 * ONE_KILO, 256 * ONE_KILO,
        512 * ONE_KILO, 1024 * ONE_KILO, 2 * 1024 * ONE_KILO};
    private static int steps = 64 * 1024 * 1024;
    private static final String RUNALL = "all";
    private static double[][] durstrides;

    public static void main(final String[] args) throws IOException {
        EnumSet<StrideType> stridestorun=initialize(args);
        
        double[] durations = null;
        //StrideType[] stridetypes = StrideType.values();
        int k=0;
        for (StrideType strideType : stridestorun) {
            durations = durstrides[k];
            System.out.println("Running:"+strideType);
            k++;
            for (int i = 0; i < memorysize.length; i++) {
                durations[i] = perfTest(memorysize[i], strideType);

            }
        }

        StringBuilder header = new StringBuilder();
        StringBuilder filename=new StringBuilder();
        header.append("workingset");
        for (StrideType stride : stridestorun) {
            header.append(",");
            header.append(stride.name());
            filename.append(stride);
        }
        header.append("\n");
        FileWriter fw = new FileWriter(Utilities.getTmpStoreLocation() + "/"+filename.toString()+"seqrandresults.txt");
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

    public static double perfTest(long k, StrideType strideType) {
        long[] array=null;
        if(strideType== StrideType.RRAND /*|| strideType==StrideType.RRANDPFR
                || strideType==StrideType.RRANDPFW*/){
            array = initArray(k, true);
        }
        else{
            array = initArray(k, false);
        }
        return perfTestArray(k, array, strideType);
    }

    public static double perfTestArray(long k, long[] array, StrideType strideType) {
        int lengthMod = array.length - 1;
        long start = System.nanoTime();
        int pos =0;
        for (int i = 0; i < steps; i++) {
            pos = (strideType.next(pos,array)) & lengthMod;
            array[pos]++;
            
        }
        long end = System.nanoTime();
        long duration = end - start;
        System.out.println("duration:" + duration);
        Double nanossperelement = Double.valueOf((duration / steps));
        
        System.out.format("Size:%d - %.2fns \n",
                Long.valueOf(k),
                nanossperelement);
        return nanossperelement;
    }

    
    public enum StrideType {
        
        SEQ {
            public int next(final int pos,long array[]) {
                return (pos + 1);
            }
        },
        
        RAND{
            public int next(final int pos,long array[]) {
                return (pos + PRIME_INC);
            }
        },
        
        RRAND {
            public int next(final int pos,long array[]) {
                return (int) (array[pos]);
            }
        };
        
        /*
         * Have to finish this later 
         * ,
        
        RRANDPFR {
            public int next(final int pos,long array[]) {
                //Unsafe.prefetchReadStatic(array, pos);
                return (int) (array[pos]);
            }
        },
        
        RRANDPFW {
            public int next(final int pos,long array[]) {
                //Unsafe.prefetchWriteStatic(array, pos);
                return (int) (array[pos]);
            }
        };
        private static void prefetch(int howmany, int pos,long array[]){
            Unsafe.prefetchReadStatic(array, pos);
        }*/
        public abstract int next(int i,long array[]);
    }
    public static long[] initArray(long size, boolean random) {

        //long arraysize = size * ONE_KILO / LONG_SIZE;
        long arraysize = size;
        long array[] = new long[(int) arraysize];
        if (random) {
            for (int i = 0; i < array.length; i++) {
                array[i] = rand.nextLong(array.length);
            }
        }
        return array;
    }

    private static EnumSet<StrideType> initialize(String... args) {
        if (args.length == 0) {
            throwStartupMessage();
        }
        EnumSet<StrideType> stridetypes=EnumSet.allOf(StrideType.class);
        EnumSet<StrideType> stridestorun=EnumSet.noneOf(StrideType.class);
        for (int i = 0; i < args.length; i++) {
            String string = args[i];
            try {
                int stride = Integer.parseInt(string);
                StrideType type=StrideType.values()[stride];
                if (stridetypes.contains(type)) {
                    stridestorun.add(type);
                }
            } catch (NumberFormatException nfe) {
                if (RUNALL.equalsIgnoreCase(string)) {
                    stridestorun=EnumSet.allOf(StrideType.class);
                    break;
                } else {
                    throwStartupMessage();
                }
            }
        }
        durstrides = new double[stridestorun.size()][];
        int i=0;
        for (StrideType strideType : stridestorun) {
            durstrides[i] = new double[memorysize.length];
            i++;
        }
        
        return stridestorun;
    }

    private static void throwStartupMessage() {
        System.out.println("Available Memory Strides are " + (EnumSet.allOf(StrideType.class)) + " " + "or all");
        System.exit(0);
    }
}
