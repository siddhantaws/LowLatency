/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.invoke;

import org.mk.training.dynamic.Employee;
import java.lang.invoke.MethodHandles;

/**
 *
 * @author StillWaterrz
 */
public class MethodHandleMain {
    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup lk = MethodHandles.lookup();
        System.out.println("lk.lookupClass()"+lk.lookupClass());
        //lk.in(Employee.class);
        lk.findGetter(Employee.class,"name",String.class);
    }    
}
