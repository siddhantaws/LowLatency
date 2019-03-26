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
import java.util.TreeMap;
import jsr166y.ForkJoinPool;
import recursiveaction.Sample;
import recursiveaction.Sample.Employee;

/**
 *
 * @author StillWaterrz
 */
/*public class ParallelArrayMainWithLamda {

    public static void main(String[] args) {
        Sample.Employee[] data = Sample.sampleArray();
        ForkJoinPool pool = new ForkJoinPool();
        ParallelArray<Sample.Employee> pa = ParallelArray.createFromCopy(data, pool);
        ParallelArrayWithLongMapping<Sample.Employee> paemp=pa.withFilter(concdomain).withMapping(experience);
        System.out.println("paemp.summary():"+paemp.summary());
        System.out.println("pa.summary():"+pa.summary());
    
        for (int i = 0; i < 150; i++) {
            Sample.Employee employee = data[i];
            System.out.println("Index:"+i+" :"+employee);
        }
        
    }
    
    static final Ops.Predicate<Sample.Employee> concdomain = #{s -> s.domain.equals("Concurrency")};
    static final Ops.ObjectToLong<Sample.Employee> experience = #{s -> s.experience};
}*/
