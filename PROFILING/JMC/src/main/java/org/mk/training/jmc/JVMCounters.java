/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.jmc;

import java.io.IOException;
import java.nio.ByteBuffer;

import sun.management.counter.Counter;
import sun.management.counter.perf.PerfInstrumentation;
import sun.misc.Perf;

/**
 *
 * @author mohit
 */
public class JVMCounters {
       public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: JVMCounters <PID>");
            System.exit(2);
        }
        Perf p = Perf.getPerf();
        ByteBuffer buffer = p.attach(Integer.parseInt(args[0]), "r");
        PerfInstrumentation perfInstrumentation = new PerfInstrumentation(buffer);
        for (Counter counter : perfInstrumentation.getAllCounters()) {
            System.out.println(String.format(
                    "%s = %s [Variability: %s, Units: %s]", counter.getName(),
                    String.valueOf(counter.getValue()),
                    counter.getVariability(), counter.getUnits()));
        }

    }

}
