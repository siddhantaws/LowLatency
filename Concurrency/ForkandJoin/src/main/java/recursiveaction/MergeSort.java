/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recursiveaction;

import java.util.Arrays;

public class MergeSort {
    private static MergeSort INSTANCE=new MergeSort();
    private final int SIZE_THRESHOLD = 5000;
    
    private int algo=0;
    
    private MergeSort() {
    }

    public int getAlgo() {
        return algo;
    }

    public void setAlgo(int algo) {
        this.algo = algo;
    }

    public static MergeSort getINSTANCE() {
        return INSTANCE;
    }
    
    public void sort(Comparable[] a) {
        sort(a, 0, a.length-1);
    }
 
    public void sort(Comparable[] a, int lo, int hi) {
        if (hi - lo < SIZE_THRESHOLD) {
            insertionsort(a, lo, hi);
            return;
        }
 
        Comparable[] tmp = new Comparable[((hi - lo) / 2) + 1];
        mergeSort(a, tmp, lo, hi);
    }
 
    private void mergeSort(Comparable[] a, Comparable[] tmp, int lo, int hi) {
        if (hi - lo < SIZE_THRESHOLD) {
            if(algo==0)insertionsort(a, lo, hi);
            else dualPivotQuickSort(a, lo, hi);
            return;
        }
 
        int m = (lo + hi) / 2;
        mergeSort(a, tmp, lo, m);
        mergeSort(a, tmp, m + 1, hi);
        merge(a, tmp, lo, m, hi);
    }
 
    private void merge(Comparable[] a, Comparable[] b, int lo, int m, int hi) {
        if (a[m].compareTo(a[m+1]) <= 0)
            return;
 
        System.arraycopy(a, lo, b, 0, m-lo+1);
 
        int i = 0;
        int j = m+1;
        int k = lo;
 
        // copy back next-greatest element at each time
        while (k < j && j <= hi) {
            if (b[i].compareTo(a[j]) <= 0) {
                a[k++] = b[i++];
            } else {
                a[k++] = a[j++];
            }
        }
 
        // copy back remaining elements of first half (if any)
        System.arraycopy(b, i, a, k, j-k);
 
    }
 
    private void insertionsort(Comparable[] a, int lo, int hi) {
        for (int i = lo+1; i <= hi; i++) {
            int j = i;
            Comparable t = a[j];
            while (j > lo && t.compareTo(a[j - 1]) < 0) {
                a[j] = a[j - 1];
                --j;
            }
            a[j] = t;
        }
    }
    private void dualPivotQuickSort(Comparable[] a, int lo, int hi) {
        Arrays.sort(a, lo, hi);
    }
}
