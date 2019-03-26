/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.hpc;

import java.io.OptionalDataException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author mohit
 */
public class Employee1 {
    private int age;
    private String name;
    private int experience;
    static ThreadLocalRandom RAND=ThreadLocalRandom.current();
    
    public Employee1(int age, String name, int experience) {
        this.age = age;
        this.name = name;
        this.experience = experience;
    }

    public Employee1() {
        age=RAND.nextInt(0, 75);
        experience=RAND.nextInt(0,55);
        name=Long.toHexString(RAND.nextLong(0,1000000000));
    }

    @Override
    public String toString() {
        return "Employee{" + "age=" + age + ", name=" + name + ", experience=" + experience + '}';
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.age;
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + this.experience;
        return hash;
    }

    //@Override
    public boolean equalsEmployee1(Object obj) {
        //return 
        return Optional.ofNullable(obj)
                .filter(that->that instanceof Employee1)
                .map(that->(Employee1)that)
                .filter(that->Objects.equals(this.name,that.name))
                .filter(that->Objects.equals(this.age,that.age))
                .filter(that->Objects.equals(this.experience,that.experience))
                .isPresent();
                
                
    }
    /*public boolean equalsEmployee1(Object obj) {
        //return 
        return Optional.ofNullable(obj)
                .filter(new Predicate<Object>() {

            @Override
            public boolean test(Object t) {
                return t instanceof Employee1;
            }
        })
                .map(new Function<Object, Employee1>() {

            @Override
            public Employee1 apply(Object t) {
                return (Employee1)t;
            }
        })
                .filter(new Predicate<Employee1>() {

            @Override
            public boolean test(Employee1 that) {
                
                return Objects.equals(Employee1.this.age,that.age);
            }
        })
                .filter(new Predicate<Employee1>() {

            @Override
            public boolean test(Employee1 that) {
                return Objects.equals(Employee1.this.name,that.name);
            }
        })
                .filter(new Predicate<Employee1>() {

            @Override
            public boolean test(Employee1 that) {
                return Objects.equals(Employee1.this.experience,that.experience);
            }
        })
                .isPresent();
                
                
    }*/
    
    private static int benchCondLoop(Employee1[] data) {       
        Employee1 e=new Employee1();
        int count=0;
        for (Employee1 data1 : data) {
            if(e.equalsEmployee1(data1))count++;
        }
        return count;
    }
    
    static Employee1[] createData(int count, boolean warmup, boolean predict) {
        Employee1[] data = new Employee1[count];
        for (int i = 0; i < count; i++) {
            data[i] = new Employee1();
        }
        return data;
    }
    public static void main(String[] args) throws InterruptedException {
        int count=0;
        for (int i = 0; i < 1000000; i++) {
            Employee1[] data = createData(1024, false, true);
            count += benchCondLoop(data);
        }
        System.out.println("warmup done");
        
        Thread.sleep(1000);
        
        for (int i = 0; i < 10000; i++) {
            Employee1[] data = createData(1024, false, true);
            count += benchCondLoop(data);
        }
        
        System.out.println(count);
        
    }
    
}
