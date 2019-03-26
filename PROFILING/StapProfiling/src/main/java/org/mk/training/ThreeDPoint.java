/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training;

/**
 *
 * @author mohit
 */
public class ThreeDPoint extends Point{
    private int z;

    public ThreeDPoint(int z, int x, int y) {
        //super();
        super(x, y);
        this.z = z;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "ThreeDPoint{" + "z=" + z + super.toString()+'}';
    }

    
    
}
