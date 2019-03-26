/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recursiveaction;

import extra166y.ParallelArray;
import java.util.Arrays;
import jsr166y.ForkJoinPool;

/**
 *
 * @author StillWaterrz
 */
public class ForkandJoinMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int numiterations=2;
        Sample.Employee[] data = Sample.sampleArray();
        long sumforarraysort = 0;
        for (int i = 0; i < numiterations; i++) {
            Sample.Employee[] data0 = new Sample.Employee[data.length];
            System.arraycopy(data, 0, data0, 0, data.length);
            long start = System.nanoTime();
            Arrays.sort(data0);
            long end = System.nanoTime();
            sumforarraysort += end - start;
            System.out.println("Iteration:" + i + " :" + (end - start));
        }
        float sumforarraysortaverage = sumforarraysort / numiterations;
        System.out.println("Average for Arrays.sort:" + sumforarraysortaverage);

        ForkJoinPool pool = new ForkJoinPool();
        long sumfordpquicksortfj = 0;
        for (int i = 0; i < numiterations; i++) {
            ParallelArray<Sample.Employee> pa = ParallelArray.createFromCopy(data, pool);
            long start = System.nanoTime();
            ParallelArray<Sample.Employee> spa = pa.sort();
            long end = System.nanoTime();
            sumfordpquicksortfj += end - start;
            System.out.println("Iteration:" + i + " :" + (end - start));
        }
        float sumfordpquicksortfjaverage = sumfordpquicksortfj / numiterations;
        System.out.println("Average for dpquicksortwithpa:" + sumfordpquicksortfjaverage);
        System.out.println("Improvement:" + sumforarraysortaverage / sumfordpquicksortfjaverage + " times.");

        long sumformergeinsertionsort = 0;
        for (int i = 0; i < numiterations; i++) {
            Sample.Employee[] data0 = new Sample.Employee[data.length];
            System.arraycopy(data, 0, data0, 0, data.length);
            long start = System.nanoTime();
            MergeSort.getINSTANCE().sort(data0);
            long end = System.nanoTime();
            sumformergeinsertionsort += end - start;
            System.out.println("Iteration:" + i + " :" + (end - start));
        }
        float sumformergeinsertionsortaverage = sumformergeinsertionsort / numiterations;
        System.out.println("Average for mergeinsertion:" + sumformergeinsertionsortaverage);
        System.out.println("Improvement:" + sumforarraysortaverage / sumformergeinsertionsortaverage + " times.");
        
        long sumformergeinsertionsortfj = 0;
        for (int i = 0; i < numiterations; i++) {
            Sample.Employee[] data0 = new Sample.Employee[data.length];
            System.arraycopy(data, 0, data0, 0, data.length);
            long start = System.nanoTime();
            ParallelMergeSort.getINSTANCE().sort(data0);
            long end = System.nanoTime();
            sumformergeinsertionsortfj += end - start;
            System.out.println("Iteration:" + i + " :" + (end - start));
        }
        float sumformergeinsertionsortfjaverage = sumformergeinsertionsortfj / numiterations;
        System.out.println("Average for mergeinserationfj:" + sumformergeinsertionsortfjaverage);
        System.out.println("Improvement:" + sumforarraysortaverage / sumformergeinsertionsortfjaverage + " times.");
        System.out.println("Improvement on mergeinsertionsort by FJ:" + sumformergeinsertionsortaverage / sumformergeinsertionsortfjaverage + " times.");
        
        long sumformergedpquicksort = 0;
        for (int i = 0; i < numiterations; i++) {
            Sample.Employee[] data0 = new Sample.Employee[data.length];
            System.arraycopy(data, 0, data0, 0, data.length);
            long start = System.nanoTime();
            MergeSort mergein=MergeSort.getINSTANCE();
            mergein.setAlgo(1);
            mergein.sort(data0);
            long end = System.nanoTime();
            sumformergedpquicksort += end - start;
            System.out.println("Iteration:" + i + " :" + (end - start));
        }
        float sumformergedpquicksortaverage = sumformergedpquicksort / numiterations;
        System.out.println("Average for mergedpquicksort:" + sumformergedpquicksortaverage);
        System.out.println("Improvement:" + sumforarraysortaverage / sumformergedpquicksortaverage + " times.");
        
        long sumformergedpquicksortfj = 0;
        for (int i = 0; i < numiterations; i++) {
            Sample.Employee[] data0 = new Sample.Employee[data.length];
            System.arraycopy(data, 0, data0, 0, data.length);
            long start = System.nanoTime();
            ParallelMergeSort pamergein=ParallelMergeSort.getINSTANCE();
            pamergein.setAlgo(1);
            pamergein.sort(data0);
            long end = System.nanoTime();
            sumformergedpquicksortfj += end - start;
            System.out.println("Iteration:" + i + " :" + (end - start));
        }
        float sumformergedpquicksortfjaverage = sumformergedpquicksortfj / numiterations;
        System.out.println("Average for mergedpquicksortfj:" + sumformergedpquicksortfjaverage);
        System.out.println("Improvement:" + sumforarraysortaverage / sumformergedpquicksortfjaverage + " times.");
        System.out.println("Improvement on mergedpquicksort by FJ:" + sumformergedpquicksortaverage / sumformergedpquicksortfjaverage + " times.");
    }
}