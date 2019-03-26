/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.jmc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;

import sun.misc.Perf;
import sun.management.counter.Counter;
import sun.management.counter.perf.PerfInstrumentation;

/**
 *
 * @author mohit
 */
public class PerformanceCounters implements DynamicMBean {

    public final static ObjectName MBEAN_OBJECT_NAME;
    public final Map<String, Counter> counterMap;
    private final MBeanInfo info;

    static {
        MBEAN_OBJECT_NAME = createObjectName("org.mk.training.jmc.management:type=PerformanceCounters");
    }

    public PerformanceCounters() {
        counterMap = setUpCounters();
        info = createMBeanInfo();
    }

    @Override
    public Object getAttribute(String attribute)
            throws AttributeNotFoundException, MBeanException,
            ReflectionException {
        if (attribute == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException(
                    "The attribute name cannot be null."),
                    "Cannot invoke getAttribute on " + MBEAN_OBJECT_NAME
                    + " with null as attribute name.");
        }
        Counter c = counterMap.get(attribute);
        if (c == null) {
            throw new AttributeNotFoundException(
                    "Could not find the attribute" + attribute);
        }
        return c.getValue();
    }

    @Override
    public void setAttribute(Attribute attribute)
            throws AttributeNotFoundException, InvalidAttributeValueException,
            MBeanException, ReflectionException {
        if (attribute == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException(
                    "The attribute name cannot be null."),
                    "Cannot invoke setAttribute on " + MBEAN_OBJECT_NAME
                    + " with null as attribute name.");
        }
        Counter c = counterMap.get(attribute);
        if (c == null) {
            throw new AttributeNotFoundException(
                    "Could not find the attribute " + attribute + ".");
        }
        throw new RuntimeOperationsException(
                new UnsupportedOperationException(),
                "All attributes on the PerfCounters MBean are read only.");
    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        AttributeList attributeList = new AttributeList();
        for (String attribute : attributes) {
            try {
                attributeList.add(new Attribute(attribute,
                        getAttribute(attribute)));
            } catch (AttributeNotFoundException | MBeanException | ReflectionException e) {
                // Seems this one is not supposed to throw exceptions. Try to
                // get as many as possible.
            }
        }
        return attributeList;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        // Seems this one is not supposed to throw exceptions.
        // Just ignore.
        return null;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature)
            throws MBeanException, ReflectionException {
        throw new MBeanException(new UnsupportedOperationException(
                MBEAN_OBJECT_NAME + " does not have any operations."));
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return info;
    }

    private static ObjectName createObjectName(String name) {
        try {
            return new ObjectName(name);
        } catch (MalformedObjectNameException e) {
            // This will not happen – known to be wellformed.
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Counter> setUpCounters() {
        Map<String, Counter> counters = new HashMap<>();
        Perf p = Perf.getPerf();
        try {
            ByteBuffer buffer = p.attach(0, "r");
            PerfInstrumentation perfInstrumentation = new PerfInstrumentation(
                    buffer);
            for (Counter counter : perfInstrumentation.getAllCounters()) {
                counters.put(counter.getName(), counter);
            }
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Failed to access performance counters. No counters will be available!");
            e.printStackTrace();
        }
        return counters;
    }

    private MBeanInfo createMBeanInfo() {
        Collection<Counter> counters = counterMap.values();
        List<MBeanAttributeInfo> attributes = new ArrayList<>(counters.size());
        for (Counter c : counters) {
            if (!c.isVector()) {
                String typeName = "java.lang.String";
                synchronized (c) {
                    Object value = c.getValue();
                    if (value != null) {
                        typeName = value.getClass().getName();
                    }
                }
                attributes.add(new MBeanAttributeInfo(c.getName(), typeName,
                        String.format("%s [%s,%s]", c.getName(), c.getUnits(),
                        c.getVariability()), true, false, false));
            }
        }
        MBeanAttributeInfo[] attributesArray = attributes.toArray(new MBeanAttributeInfo[attributes.size()]);
        return new MBeanInfo(
                this.getClass().getName(),
                "An MBean exposing the available JVM Performance Counters as attributes.",
                attributesArray, null, null, null);
    }
}
