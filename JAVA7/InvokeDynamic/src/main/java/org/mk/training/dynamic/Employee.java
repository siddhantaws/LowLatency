/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.dynamic;

/**
 *
 * @author StillWaterrz
 */
public class Employee {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Employee{" + "name=" + name + ", id=" + id + '}';
    }
}
