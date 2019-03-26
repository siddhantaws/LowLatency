/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.dynamic;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 *
 * @author StillWaterrz
 */
public class SimpleDynamicLinkageExample {

    private static MethodHandle sayHello;

    public static void sayHello() {
        System.out.println("There we go!");
    }

    public static CallSite bootstrapDynamic(MethodHandles.Lookup caller, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        System.out.println("Call back from VM on an invokedynamic instruction....");
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Class thisClass = lookup.lookupClass(); // (who am I?)
        sayHello = lookup.findStatic(thisClass, "sayHello", MethodType.methodType(void.class));
        return new ConstantCallSite(sayHello.asType(type));
    }
    /*private static int sayHello() {
     System.out.println("Returning an int !");
     return 0;
     }

     public static CallSite bootstrapDynamic(MethodHandles.Lookup caller, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
     MethodHandles.Lookup lookup = MethodHandles.lookup();
     System.out.println("caller.lookupClass():" + caller.lookupClass());
     Class thisClass = lookup.lookupClass(); // (who am I?)
     sayHello = lookup.findStatic(thisClass, "sayHello", MethodType.methodType(int.class));
     return new ConstantCallSite(sayHello.asType(type));
     }*/
}
