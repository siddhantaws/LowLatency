/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parallelarrays;

import extra166y.Ops;
import extra166y.Ops.ObjectAndIntToInt;
import extra166y.ParallelArray;
import extra166y.ParallelArrayWithDoubleMapping;
import extra166y.ParallelArrayWithLongMapping;
import jsr166y.ForkJoinPool;
import recursiveaction.Sample;
import recursiveaction.Sample.Employee;

/**
 *
 * @author StillWaterrz
 */
public class ParallelArrayMain2 {

    public static void main(String[] args) {
        Sample.Employee[] data = Sample.sampleArray();
        ForkJoinPool pool = new ForkJoinPool();
        ParallelArray<Sample.Employee> pa = ParallelArray.createFromCopy(data, pool);
        ParallelArrayWithLongMapping<Sample.Employee> paemp=pa.withFilter(new ConcurrencyDomain()).withFilter(new Affordable()).withMapping(new Experience());
        System.out.println("Concurrency People who are affordable:"+pa.get(paemp.summary().indexOfMax()));
        System.out.println("paemp.summary():"+paemp.summary());
        System.out.println("pa.summary():"+pa.summary());
        for (int i = 0; i < 150; i++) {
            Sample.Employee employee = data[i];
            System.out.println("Index:"+i+" :"+employee);
        }
    }
    public static class ConcurrencyDomain implements Ops.Predicate {
        @Override
        public boolean op(Object a) {
            Sample.Employee e=(Sample.Employee)a;
            return "Concurrency".equals(e.getDomain());
        }
    }
    public static class Affordable implements Ops.Predicate {
        @Override
        public boolean op(Object a) {
            Sample.Employee e=(Sample.Employee)a;
            return e.getSalary()<20000;
        }
    };
    public static class Experience implements Ops.ObjectToLong{

        @Override
        public long op(Object a) {
            return ((Sample.Employee)a).getExperience();
        }
    };
    public static class ConcurrencyAndAffordable implements Ops.Predicate{

        @Override
        public boolean op(Object a) {
            Sample.Employee e=(Sample.Employee)a;
            return "Concurrency".equals(e.getDomain()) && e.getSalary()<20000;
        }
    };
    
    public static class ForEach implements Ops.Procedure{

        @Override
        public void op(Object a) {
            ((Employee)a).salaryHike(SequentialParallelForEach.HIKE);
        }
    };
    
    /*static Ops.ObjectToInt experience =new Ops.ObjectToInt() {

        @Override
        public int op(Object a) {
            return ((Sample.Employee)a).getExperience();
        }
    };*/
    
}
