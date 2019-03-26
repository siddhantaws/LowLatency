/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parallelarrays;


import extra166y.Ops;


import jsr166y.ForkJoinPool;
import jsr166y.RecursiveAction;

/**
 *
 * @author StillWaterrz
 */
public class ParallelForEachAction{
    
    private static ParallelForEachAction INSTANCE=new ParallelForEachAction();
    private final ForkJoinPool threadPool = SequentialParallelForEach.pool;
    private final int SIZE_THRESHOLD = 1000;
    Ops.Procedure proc;
        
    private ParallelForEachAction() {
    }

    public static ParallelForEachAction getINSTANCE() {
        return INSTANCE;
    }

    public ForkJoinPool getThreadPool() {
        return threadPool;
    }
    
    
    public void apply(Object[] a,Ops.Procedure proc) {
        apply(a, 0, a.length-1,proc);
    }
 
    public void apply(Object[] a, int lo, int hi,Ops.Procedure proc) {
        this.proc=proc;
        if (hi - lo < SIZE_THRESHOLD) {
            return;
        }
        threadPool.invoke(new SortTask(a, lo, hi));
    }
 
    /**
     * This class replaces the recursive function that was
     * previously here.
     */
    class SortTask extends RecursiveAction {
        Object[] a;
        int lo, hi;
        public SortTask(Object[] a, int lo, int hi) {
            this.a = a;
            this.lo = lo;
            this.hi = hi;
        }
 
        @Override
        protected void compute() {
            if (hi - lo < SIZE_THRESHOLD) {
                applyProcedure(a, lo, hi, proc);
                return;
            }
 
            int m = (lo + hi) / 2;
            // the two recursive calls are replaced by a call to invokeAll
            invokeAll(new SortTask(a, lo, m), new SortTask(a, m+1, hi));
            //merge(a, tmp, lo, m, hi);
        }
    }
    private void applyProcedure(Object[] a, int lo, int hi,Ops.Procedure proc) {
        for (int i = lo+1; i <= hi; i++) {
            proc.op(a[i]);
        }
    }
}
