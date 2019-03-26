/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.jmc;

/**
 *
 * @author mohit
 */
import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;

public class PerformanceCountersMBeanMain {

    public static void main(String[] args) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, IOException {
        ManagementFactory.getPlatformMBeanServer().registerMBean(new PerformanceCounters(), PerformanceCounters.MBEAN_OBJECT_NAME);
        System.out.println("Press enter to quit!");
        System.in.read();
    }
}
