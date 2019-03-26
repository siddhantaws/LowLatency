/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parallelarrays;

import extra166y.ParallelArray;
import extra166y.ParallelArrayWithLongMapping;
import jsr166y.ForkJoinPool;
import recursiveaction.Sample;

/**
 *
 * @author StillWaterrz
 */
public class SequentialParallelForEach {
    public final static int HIKE=14;
    public static ForkJoinPool pool = new ForkJoinPool();
    public static void main(String[] args) {
        int numiterations = 4;
        System.out.println("timing sequentialHikeSalary():"+pool.getParallelism());
        Sample.Employee[] sampledata = Sample.sampleArray();
        
        long sumforconcurrencyprogrammers = 0;
        for (int i = 0; i < numiterations; i++) {
            long concurrencyprogrammers = sequentialHikeSalary(sampledata);
            sumforconcurrencyprogrammers += concurrencyprogrammers;
            System.out.println("timing sequentialHikeSalary():"+concurrencyprogrammers);
        }
        double sumforconcurrencyprogrammersaverage = sumforconcurrencyprogrammers / numiterations;
        System.out.println("Average for :sequentialHikeSalary()" + sumforconcurrencyprogrammersaverage);
        
        long sumforconcurrencyprogrammerswithpa = 0;
        for (int i = 0; i < numiterations; i++) {
            long concurrencyprogrammerswithpa = parallelHikeSalary(sampledata);
            System.out.println("timing parallelHikeSalary():"+concurrencyprogrammerswithpa);
            sumforconcurrencyprogrammerswithpa+=concurrencyprogrammerswithpa;
        }
        double sumforconcurrencyprogrammerswithpaaverage = sumforconcurrencyprogrammerswithpa / numiterations;
        System.out.println("Average for :parallelHikeSalary()" + sumforconcurrencyprogrammerswithpaaverage);
        System.out.println("improvement:" + ((double) sumforconcurrencyprogrammersaverage /sumforconcurrencyprogrammerswithpaaverage));
        
        long sumforconcurrencyprogrammerswithfj = 0;
        for (int i = 0; i < numiterations; i++) {
            long concurrencyprogrammerswithfj = parallelHikeSalary2(sampledata);
            System.out.println("timing parallelHikeSalary2():"+concurrencyprogrammerswithfj);
            sumforconcurrencyprogrammerswithfj+=concurrencyprogrammerswithfj;
        }
        double sumforconcurrencyprogrammerswithpaaveragefj = sumforconcurrencyprogrammerswithfj / numiterations;
        System.out.println("Average for :parallelHikeSalary2()" + sumforconcurrencyprogrammerswithpaaveragefj);
        System.out.println("improvement:" + ((double) sumforconcurrencyprogrammersaverage /sumforconcurrencyprogrammerswithpaaveragefj));
    }
    
    private static long sequentialHikeSalary(Sample.Employee[] sampledata){
        Sample.Employee[] data = new Sample.Employee[sampledata.length];
        System.arraycopy(sampledata, 0, data, 0, sampledata.length);
        int index=((int) (Math.random() * 1000000)) & data.length;
        System.out.println("Before Hike:"+data[index]);
        long start=System.nanoTime();
        for (Sample.Employee employee : data) {
            employee.salaryHike(HIKE);
        }
        long end = System.nanoTime();
        System.out.println("After Hike:"+data[index]);
        return end-start;
    }
    private static long parallelHikeSalary(Sample.Employee[] sampledata){
        ParallelArray<Sample.Employee> pa = ParallelArray.createFromCopy(sampledata,pool);
        int index=((int) (Math.random() * 1000000)) & pa.size()-1;
        System.out.println("Before Hike:"+pa.get(index));
        long start = System.nanoTime();
        pa.apply(new ParallelArrayMain2.ForEach());
        long end = System.nanoTime();
        System.out.println("Before Hike:"+pa.get(index));
        return end - start;
    }
    private static long parallelHikeSalary2(Sample.Employee[] sampledata){
        Sample.Employee[] data = new Sample.Employee[sampledata.length];
        System.arraycopy(sampledata, 0, data, 0, sampledata.length);
        int index=((int) (Math.random() * 1000000)) & data.length-1;
        System.out.println("Before Hike:"+data[index]);
        ParallelForEachAction employees=ParallelForEachAction.getINSTANCE();
        long start = System.nanoTime();
        employees.apply(data,new ParallelArrayMain2.ForEach());
        long end = System.nanoTime();
        System.out.println("Before Hike:"+data[index]);
        return end - start;
    }
}
