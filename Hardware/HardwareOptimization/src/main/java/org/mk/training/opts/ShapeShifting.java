/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.opts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mohit
 */
public final class ShapeShifting {
    private static class ShapeList{
        Shape head;
        Shape tail;

        public ShapeList(List<Shape> shape) {
            for (Shape shape1 : shape) {
                if(this.head==null){
                    this.head=this.tail=shape1;
                }
                else{
                    this.tail.next=shape1;    
                    this.tail=shape1;
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder sb=new StringBuilder();
            Shape hasmore=this.head;
            while (hasmore!=null) {
                sb.append(hasmore.toString());
                hasmore=hasmore.next;
                
            }
            return "Shape{" + "next=" + sb.toString()  + '}';
        }
        
    }
    private static class Shape{
        public Shape(List<Point> shape) {
            for (Point shape1 : shape) {
                if(this.head==null){
                    this.head=this.tail=shape1;
                    size++;
                }
                else{
                    this.tail.next=shape1;
                    size++;    
                    this.tail=shape1;
                }
            }
        }
        Shape next;
        Point head;
        Point tail;
        int size;
        int xsum(){
            int xsum=0;
            Point tmpnext=this.head;
            while(tmpnext!=null){
                xsum+=tmpnext.x;
                tmpnext=tmpnext.next;
            }
            return xsum;
        }
        int ysum(){
            int ysum=0;
            Point tmpnext=this.head;
            while(tmpnext!=null){
                ysum+=tmpnext.y;
                tmpnext=tmpnext.next;
            }
            return ysum;
        }
        void movex(int offset){
            Point tmpnext=this.head;
            while(tmpnext!=null){
                tmpnext.x=tmpnext.x+offset;
                tmpnext=tmpnext.next;
            }
        }
        void movey(int offset){
            Point tmpnext=this.head;
            while(tmpnext!=null){
                tmpnext.y=tmpnext.y+offset;
                tmpnext=tmpnext.next;
            }
        }
        
        int numpoints(){
            return size;
        }

        @Override
        public String toString() {
            
            StringBuilder sb=new StringBuilder();
            Point hasmore=this.head;
            while (hasmore!=null) {
                sb.append(hasmore.toString());
                hasmore=hasmore.next;
                
            }
            return "Shape{" + "head=" + sb.toString() + ", size=" + size + '}';
        }
        
    }
    
    
    public static void main(String[] args) {
        
        Shape tri1=new Shape(Arrays.asList(new Point(2, 3),new Point(4, 4),new Point(7, 7),new Point(2, 3),new Point(4, 4),new Point(7, 7)));
        Shape tri2=new Shape(Arrays.asList(new Point(-3, 3),new Point(2, 4),new Point(10, 1),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri3=new Shape(Arrays.asList(new Point(7, 7),new Point(-1,4),new Point(6, 6),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri4=new Shape(Arrays.asList(new Point(4, 4),new Point(7, 7),new Point(-1,4),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri5=new Shape(Arrays.asList(new Point(10, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri6=new Shape(Arrays.asList(new Point(2, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri7=new Shape(Arrays.asList(new Point(-3, 3),new Point(2, 4),new Point(10, 1),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri8=new Shape(Arrays.asList(new Point(7, 7),new Point(-1,4),new Point(6, 6),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri9=new Shape(Arrays.asList(new Point(4, 4),new Point(7, 7),new Point(-1,4),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri10=new Shape(Arrays.asList(new Point(10, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri11=new Shape(Arrays.asList(new Point(2, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri12=new Shape(Arrays.asList(new Point(-3, 3),new Point(2, 4),new Point(10, 1),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri13=new Shape(Arrays.asList(new Point(7, 7),new Point(-1,4),new Point(6, 6),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri14=new Shape(Arrays.asList(new Point(4, 4),new Point(7, 7),new Point(-1,4),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri15=new Shape(Arrays.asList(new Point(10, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri16=new Shape(Arrays.asList(new Point(2, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri17=new Shape(Arrays.asList(new Point(-3, 3),new Point(2, 4),new Point(10, 1),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri18=new Shape(Arrays.asList(new Point(7, 7),new Point(-1,4),new Point(6, 6),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri19=new Shape(Arrays.asList(new Point(4, 4),new Point(7, 7),new Point(-1,4),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape tri20=new Shape(Arrays.asList(new Point(10, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        
        
        //System.out.println(""+poly);
        
        ShapeList shapelist=new ShapeList(Arrays.asList(tri1,tri2,tri3,tri4,tri5,tri6,tri7,tri8,tri9,tri10,tri11,tri12,tri13,tri14,tri15,tri16,tri17,tri18,tri19,tri20));
        
        //ShapeList shapelist=new ShapeList(Arrays.asList(tri1));
        //System.out.println(""+shapelist);
        
        long start=System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            int totalx=0;
            int totaly=0;
            int totalpoints=0;
            Shape s=shapelist.head;
            while(s!=null){
                totalx += s.xsum();
                totaly += s.ysum();
                totalpoints += s.numpoints();
                s=s.next;
            }
            int offsetx=totalx/totalpoints;
            int offsety=totaly/totalpoints;
            s=shapelist.head;
            while(s!=null){
                s.movex(offsetx);
                s.movey(offsety);
                s=s.next;
            }    
        }
        long end=System.nanoTime();
        
        System.out.println("Duration="+(end-start));
    }
}
