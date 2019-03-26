/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parallelarrays;

import extra166y.ParallelArray;
import extra166y.ParallelArrayWithFilter;
import extra166y.ParallelArrayWithLongMapping;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import jsr166y.ForkJoinPool;
import recursiveaction.Sample;
import recursiveaction.Sample.EmployeeDomainComparator;

/**
 *
 * @author StillWaterrz
 */
public class SequentialParallelComparison {

    static ForkJoinPool pool = new ForkJoinPool();

    public static void main(String[] args) {
        int numiterations = 1;
        Sample.Employee[] sampledata = Sample.sampleArray();
        
        long sumforconcurrencyprogrammers = 0;
        for (int i = 0; i < numiterations; i++) {
            long concurrencyprogrammers = concurrentProgrammers(sampledata);
            sumforconcurrencyprogrammers += concurrencyprogrammers;
            System.out.println("timing concurrentProgrammers():"+concurrencyprogrammers);
        }
        double sumforconcurrencyprogrammersaverage = sumforconcurrencyprogrammers / numiterations;
        System.out.println("Average for :concurrentProgrammers()" + sumforconcurrencyprogrammersaverage);
        
        long sumforconcurrencyprogrammerswithpa = 0;
        for (int i = 0; i < numiterations; i++) {
            long concurrencyprogrammerswithpa = concurrentProgrammersWithPA(sampledata);
            System.out.println("timing concurrentProgrammersWithPA():"+concurrencyprogrammerswithpa);
            sumforconcurrencyprogrammerswithpa+=concurrencyprogrammerswithpa;
        }
        double sumforconcurrencyprogrammerswithpaaverage = sumforconcurrencyprogrammerswithpa / numiterations;
        System.out.println("Average for :concurrentProgrammerswithpa()" + sumforconcurrencyprogrammerswithpaaverage);
        System.out.println("improvement:" + ((double) sumforconcurrencyprogrammersaverage /sumforconcurrencyprogrammerswithpaaverage));

        long sumforconcurrencyaffordableprogrammers = 0;
        for (int i = 0; i < numiterations; i++) {
            long concurrencyaffordableprogrammers = concurrentAffordableProgrammers(sampledata);
            sumforconcurrencyaffordableprogrammers += concurrencyaffordableprogrammers;
            System.out.println("timing concurrentAffordableProgrammers():"+concurrencyaffordableprogrammers);
        }
        double sumforconcurrencyaffordableprogrammersaverage = sumforconcurrencyaffordableprogrammers / numiterations;
        System.out.println("Average for :concurrentAffordableProgrammers()" + sumforconcurrencyaffordableprogrammersaverage);
        
        
        long sumforconcurrencyaffordableprogrammerswithpa = 0;
        for (int i = 0; i < numiterations; i++) {
            long concurrencyaffordableprogrammerswithpa = concurrentAffordableProgrammersWithPA(sampledata);
            sumforconcurrencyaffordableprogrammerswithpa += concurrencyaffordableprogrammerswithpa;
            System.out.println("timing concurrentAffordableProgrammersWithPA():"+concurrencyaffordableprogrammerswithpa);
        }
        double sumforconcurrencyaffordableprogrammerswithpaaverage = sumforconcurrencyaffordableprogrammerswithpa / numiterations;
        System.out.println("Average for :concurrentAffordableProgrammersWithPA()" + sumforconcurrencyaffordableprogrammerswithpaaverage);
        System.out.println("improvement:" + ((double) sumforconcurrencyaffordableprogrammersaverage /sumforconcurrencyaffordableprogrammerswithpaaverage));
        
        long sumforconcurrencyaffordableprogrammerswithpa2 = 0;
        for (int i = 0; i < numiterations; i++) {
            long concurrencyaffordableprogrammerswithpa2 = concurrentAffordableProgrammersWithPA2(sampledata);
            sumforconcurrencyaffordableprogrammerswithpa2 += concurrencyaffordableprogrammerswithpa2;
            System.out.println("timing concurrentAffordableProgrammersWithPA2():"+concurrencyaffordableprogrammerswithpa2);
        }
        double sumforconcurrencyaffordableprogrammerswithpaaverage2 = sumforconcurrencyaffordableprogrammerswithpa2 / numiterations;
        System.out.println("Average for :concurrentAffordableProgrammersWithPA2()" + sumforconcurrencyaffordableprogrammerswithpaaverage2);
        System.out.println("improvement:" + ((double) sumforconcurrencyaffordableprogrammersaverage /sumforconcurrencyaffordableprogrammerswithpaaverage2));
    }

    private static long concurrentProgrammers(Sample.Employee[] sampledata) {
        Sample.Employee[] data0 = new Sample.Employee[sampledata.length];
        System.arraycopy(sampledata, 0, data0, 0, sampledata.length);
        long start = System.nanoTime();
        Arrays.sort(data0, new EmployeeDomainComparator());
        int concindex = Arrays.binarySearch(data0, new Sample.Employee(0, null, "Concurrency", 0, 0), new EmployeeDomainComparator());
        List<Sample.Employee> l = new ArrayList();
        int j = 0;
        for (int i = concindex; i < data0.length; i++) {
            Sample.Employee employee = data0[i];
            if ("Concurrency".equals(employee.getDomain())) {
                l.add(employee);
                j++;
            } else {
                break;
            }
        }
        for (int i = concindex - 1; i < data0.length; i--) {
            Sample.Employee employee = data0[i];
            if ("Concurrency".equals(employee.getDomain())) {
                l.add(employee);
                j++;
            } else {
                break;
            }
        }
        Collections.sort(l, new Sample.EmployeeExperienceComparator());
        Collections.binarySearch(l, new Sample.Employee(0, null, "Concurrency", 0, 0), new Sample.EmployeeExperienceComparator());
        System.out.println("Concurrency People most Experienced:" + l.get(l.size() - 1));
        long end = System.nanoTime();
        return end - start;
    }

    private static long concurrentAffordableProgrammers(Sample.Employee[] sampledata) {
        Sample.Employee[] data0 = new Sample.Employee[sampledata.length];
        System.arraycopy(sampledata, 0, data0, 0, sampledata.length);
        long start = System.nanoTime();
        Arrays.sort(data0, new EmployeeDomainComparator());
        int concindex = Arrays.binarySearch(data0, new Sample.Employee(0, null, "Concurrency", 0, 0), new EmployeeDomainComparator());
        List<Sample.Employee> l = new ArrayList();
        int j = 0;
        for (int i = concindex; i < data0.length; i++) {
            Sample.Employee employee = data0[i];
            if ("Concurrency".equals(employee.getDomain())) {
                l.add(employee);
                j++;
            } else {
                break;
            }
        }
        for (int i = concindex - 1; i < data0.length; i--) {
            Sample.Employee employee = data0[i];
            if ("Concurrency".equals(employee.getDomain())) {
                l.add(employee);
                j++;
            } else {
                break;
            }
        }
        Collections.sort(l, new Sample.EmployeeSalaryComparator());
        int salindex = Collections.binarySearch(l, new Sample.Employee(0, null, null, 20000, 0), new Sample.EmployeeSalaryComparator());
        if (salindex < 0) {
            salindex = -(salindex);
            salindex--;
        }
        List<Sample.Employee> list = new ArrayList<>(l.subList(0, salindex));
        System.out.println("list.size():" + list.size());
        /*for (int i = 0; i < list.size(); i++) {
            Sample.Employee employee = list.get(i);
            System.out.println(i+":"+employee);
        }*/
        Collections.sort(list, new Sample.EmployeeExperienceComparator());
        System.out.println("Concurrency People who are affordable and most Experienced:" + list.get(list.size()-1));
        long end = System.nanoTime();
        return end - start;
    }

    private static long concurrentProgrammersWithPA(Sample.Employee[] data0) {
        ParallelArray<Sample.Employee> pa = ParallelArray.createFromCopy(data0, pool);
        long start = System.nanoTime();
        ParallelArrayWithLongMapping<Sample.Employee> paemp = pa.withFilter(new ParallelArrayMain2.ConcurrencyDomain()).
                withMapping(new ParallelArrayMain2.Experience());
        System.out.println("Concurrency People most Experienced:" + pa.get(paemp.summary().indexOfMax()));
        long end = System.nanoTime();
        return end - start;
    }

    private static long concurrentAffordableProgrammersWithPA(Sample.Employee[] data0) {
        ParallelArray<Sample.Employee> pa = ParallelArray.createFromCopy(data0, pool);
        long start = System.nanoTime();
        ParallelArrayWithLongMapping<Sample.Employee> paemp = pa.withFilter(new ParallelArrayMain2.ConcurrencyDomain()).
                withFilter(new ParallelArrayMain2.Affordable()).
                withMapping(new ParallelArrayMain2.Experience());
        System.out.println("Concurrency People most Experienced:" + pa.get(paemp.summary().indexOfMax()));
        long end = System.nanoTime();
        return end - start;
    }

    private static long concurrentAffordableProgrammersWithPA2(Sample.Employee[] data0) {
        ParallelArray<Sample.Employee> pa = ParallelArray.createFromCopy(data0, pool);
        long start = System.nanoTime();
        ParallelArrayWithLongMapping<Sample.Employee> paemp = pa.withFilter(new ParallelArrayMain2.ConcurrencyAndAffordable()).
                withMapping(new ParallelArrayMain2.Experience());
        System.out.println("Concurrency People most Experienced:" + pa.get(paemp.summary().indexOfMax()));
        long end = System.nanoTime();
        return end - start;
    }
}
