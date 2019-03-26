package org.mk.training.accesspatterns;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.EnumSet;
import sun.misc.Unsafe;

/**
 *
 * @author mohit
 */
public class MatrixMultiplicationOptimization {
    private static final String RUNALL = "all";
    private static int SET_SIZE = 2000;
    static double[][] mul1;
    static double[][] mul2;
    static double[][] res;
    private static final int CACHELINESIZE = 64;
    private static final int DOUBLESIZE = 8;
    private static final int SINGLELINE = CACHELINESIZE / DOUBLESIZE;
    private static final int ITERATIONS = 1000000;

    public static void main(String... args) {
        EnumSet<MatrixMultiplication> mmstorun=initialize(args);
        System.out.println("correctness check");
        for (MatrixMultiplication matrixMultiplication : mmstorun) {
            initDummyArrays();
            matrixMultiplication.multiply(mul1, mul2, res, 4, 2);
            print2DArray(res);
        }
        int size = 4;
        int linesize= 2;
        System.out.println("Warming up...");
        for (MatrixMultiplication matrixMultiplication : mmstorun) {
            for (int i = 0; i < ITERATIONS; i++) {
                initArrays(size);
                matrixMultiplication.multiply(mul1, mul2, res,size, linesize);
            }
            
        }
        System.out.println("Testing...");
        long originaltiming=0;
        for (MatrixMultiplication matrixMultiplication : mmstorun) {
            initArrays(SET_SIZE);
            long time=matrixMultiplication.multiply(mul1, mul2, res, SET_SIZE, SINGLELINE);
            if(MatrixMultiplication.STRAIGHT_FORWARD==matrixMultiplication){
                originaltiming=time;
            }
            System.out.printf("%30s %14d nanos %02.2f times %03.2f percent%n",matrixMultiplication.name(),time,((double)originaltiming)/time,((double)originaltiming-time)/originaltiming*100);
        }
        
    }

    public enum MatrixMultiplication {

        STRAIGHT_FORWARD 
        {
            @Override
            public long multiply(double[][] set1, double[][] set2, double[][] res, int size,int linesize) {
                long start = System.nanoTime();
                for (int i = 0; i < size; ++i) {
                    for (int j = 0; j < size; ++j) {
                        for (int k = 0; k < size; ++k) {
                            res[i][j] += set1[i][k] * set2[k][j];
                        }
                    }
                }
                long end = System.nanoTime();
                return end - start;
            }
        },
        STRAIGHT_FORWARD_CACHELINE 
        {
            @Override
            public long multiply(double[][] set1, double[][] set2, double[][] res, int size,int linesize) {
                long start = System.nanoTime();
                double[] rres;
                int i2 = 0;
                double[] rmul1;
                double[] rmul2;
                int k2 = 0;
                int lengthmod = size - 1;
                for (int i = 0; i < size; i += linesize) {
                    for (int j = 0; j < size; j += linesize) {
                        for (int k = 0; k < size; k += linesize) {
                            for (i2 = 0; i2 < linesize;
                                    ++i2) {
                                rres = res[i2 + i];
                                rmul1 = mul1[i2 + i];
                                int prefetch = k;
                                for (k2 = 0;
                                        k2 < linesize; ++k2) {
                                    rmul2 = mul2[k2];
                                    for (int j2 = 0; j2 < linesize; ++j2) {
                                        rres[j + j2] += rmul1[k + k2] * rmul2[j + j2];
                                    }
                                }
                            }
                        }
                    }
                }
                long end = System.nanoTime();
                return (end - start);
            }
        },
        STRAIGHT_FORWARD_CACHELINE_PF {
            @Override
            public long multiply(double[][] set1, double[][] set2, double[][] res, int size,int linesize) {
                long start = System.nanoTime();
                double[] rres;
                int i2 = 0;
                double[] rmul1;
                double[] rmul2;
                int k2 = 0;
                int lengthmod = size - 1;
                for (int i = 0; i < size; i += linesize) {
                    for (int j = 0; j < size; j += linesize) {
                        for (int k = 0; k < size; k += linesize) {
                            for (i2 = 0; i2 < linesize;
                                    ++i2) {
                                rres = res[i2 + i];
                                rmul1 = mul1[i2 + i];
                                Unsafe.prefetchReadStatic(rmul1, 0);
                                int prefetch = k;
                                for (k2 = 0;
                                        k2 < linesize; ++k2) {
                                    rmul2 = mul2[k2];
                                    Unsafe.prefetchReadStatic(rmul1, (prefetch += linesize));
                                    for (int j2 = 0; j2 < linesize; ++j2) {
                                        rres[j + j2] += rmul1[k + k2] * rmul2[j + j2];
                                    }
                                }
                            }
                        }
                    }
                }
                long end = System.nanoTime();
                return (end - start);
            }
        },
        TRANSPOSE 
                
        {
            @Override
            public long multiply(double[][] set1, double[][] set2, double[][] res, int size,int linesize) {
                long start = System.nanoTime();
                double[][] tmp = new double[size][size];
                for (int i = 0; i < size; ++i) {
                    for (int j = 0; j < size; ++j) {
                        tmp[i][j] = mul2[j][i];
                    }
                }
                for (int i = 0; i < size; ++i) {
                    for (int j = 0; j < size; ++j) {
                        for (int k = 0; k < size; ++k) {
                            res[i][j] += mul1[i][k] * tmp[j][k];
                        }
                    }
                }
                long end = System.nanoTime();
                return (end - start);
            }
        }/*,
        TRANSPOSE_CACHELINE {
            @Override
            public long multiply(double[][] set1, double[][] set2, double[][] res, int size,int linesize) {
                long start = System.nanoTime();
                double[][] tmp = new double[size][size];
                for (int i = 0; i < size; ++i) {
                    for (int j = 0; j < size; ++j) {
                        tmp[i][j] = mul2[j][i];
                    }
                }
                for (int i=0,j=0; i < size; i += linesize,j += linesize) {
                    //for (int j = 0; j < size; j += linesize) {
                        for (int k = i; k < i+linesize; ++k) {
                            System.out.println("i:"+i+":j:"+k+":k:"+k+":"+linesize);
                            res[i][j] += mul1[i][k] * tmp[j][k];
                            //print2DArray(res);
                        }
                    //}
                }
                long end = System.nanoTime();
                return (end - start);
            }
        }*/
        ;

        public abstract long multiply(double[][] set1, double[][] set2, double[][] res, int size,int linesize);
    }

    private static void initArrays(int size) {
        mul1 = new double[size][size];
        mul2 = new double[size][size];
        res = new double[size][size];
    }

    private static void initDummyArrays() {
        mul1 = new double[][]{
            {1, 2, 3, 4},
            {4, 3, 2, 1},
            {1, 2, 3, 4},
            {4, 3, 2, 1}
        };
        mul2 = new double[][]{
            {1, 2, 3, 4},
            {4, 3, 2, 1},
            {1, 2, 3, 4},
            {4, 3, 2, 1}
        };
        res = new double[4][4];
    }

    private static void print2DArray(double[][] array) {
        for (int i = 0; i < array.length; i++) {
            double[] is = array[i];
            printArray("", is);
            System.out.println();
        }
    }

    private static void printArray(String msg, double[] array) {
        System.out.println(msg);
        for (int j = 0; j < array.length; j++) {
            double k = array[j];
            System.out.print(k + ",");
        }
    }
    
    private static EnumSet<MatrixMultiplication> initialize(String... args) {
        if (args.length == 0) {
            throwStartupMessage();
        }
        EnumSet<MatrixMultiplication> matrixmultiplications=EnumSet.allOf(MatrixMultiplication.class);
        //List<StrideType> stridestorun = new ArrayList<>(6);
        EnumSet<MatrixMultiplication> matrixmultiplicationstorun=EnumSet.noneOf(MatrixMultiplication.class);
        for (int i = 0; i < args.length; i++) {
            String string = args[i];
            try {
                int typenum = Integer.parseInt(string);
                MatrixMultiplication type=MatrixMultiplication.values()[typenum];
                if (matrixmultiplications.contains(type)) {
                    matrixmultiplicationstorun.add(type);
                }
            } catch (NumberFormatException nfe) {
                if (RUNALL.equalsIgnoreCase(string)) {
                    matrixmultiplicationstorun=EnumSet.allOf(MatrixMultiplication.class);
                    break;
                } else {
                    throwStartupMessage();
                }
            }
        }
        return matrixmultiplicationstorun;
    }

    private static void throwStartupMessage() {
        System.out.println("Available Memory Strides are " + (EnumSet.allOf(MatrixMultiplication.class)) + " " + "or all");
        System.exit(0);
    }
}
