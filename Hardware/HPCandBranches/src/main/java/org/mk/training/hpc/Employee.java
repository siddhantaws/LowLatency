/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.hpc;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author mohit
 */
public class Employee {
    private int age;
    private String name;
    private int experience;
    static ThreadLocalRandom RAND=ThreadLocalRandom.current();
    
    public Employee(int age, String name, int experience) {
        this.age = age;
        this.name = name;
        this.experience = experience;
    }

    public Employee() {
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
    public boolean equalsEmployee(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Employee other = (Employee) obj;
        if (this.age != other.age) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.experience != other.experience) {
            return false;
        }
        return true;
    }
    
    private static int benchCondLoop(Employee[] data) {       
        Employee e=new Employee();
        int count=0;
        for (Employee data1 : data) {
            if(e.equalsEmployee(data1))count++;
        }
        return count;
    }
    
    static Employee[] createData(int count, boolean warmup, boolean predict) {
        Employee[] data = new Employee[count];
        for (int i = 0; i < count; i++) {
            data[i] = new Employee();
        }
        return data;
    }
    
    public static void main(String[] args) throws InterruptedException {
        int count=0;
        for (int i = 0; i < 100000; i++) {
            Employee[] data = createData(1024, false, true);
            count += benchCondLoop(data);
        }
        System.out.println("warmup done");
        
        Thread.sleep(1000);
        
        for (int i = 0; i < 10000; i++) {
            Employee[] data = createData(1024, false, true);
            count += benchCondLoop(data);
        }
        
        System.out.println(count);
        
    }
    
}
