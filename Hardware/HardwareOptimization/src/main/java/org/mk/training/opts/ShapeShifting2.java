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
public class ShapeShifting2 {
    private static class ShapeList{
        Shape2 head;
        Shape2 tail;

        public ShapeList(List<Shape2> shape) {
            for (Shape2 shape1 : shape) {
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
            Shape2 hasmore=this.head;
            while (hasmore!=null) {
                sb.append(hasmore.toString());
                hasmore=hasmore.next;
                
            }
            return "Shape{" + "next=" + sb.toString()  + '}';
        }
        
    }
    private static class Shape2{
        public Shape2(List<Point> shape) {
            
            /*for (Point shape1 : shape) {
                if(this.head==null){
                    this.head=this.tail=shape1;
                    size++;
                }
                else{
                    this.tail.next=shape1;
                    size++;    
                    this.tail=shape1;
                }
            }*/
            if(shape!=null){
                size=shape.size();
                xcoords=new int[size];
                ycoords=new int[size];
                int i=0;
                for (Point shape1 : shape) {
                    xcoords[i]=shape1.x;
                    ycoords[i]=shape1.y;
                    i++;
                }
            }
        }
        Shape2 next;
        int [] xcoords;
        int [] ycoords;
        int size;
        int xsum(){
            int xsum=0;
            for (int i = 0; i < xcoords.length; i++) {
                xsum += xcoords[i];
            }
            return xsum;
        }
        int ysum(){
            int ysum=0;
            for (int i = 0; i < ycoords.length; i++) {
                ysum += ycoords[i];
            }
            return ysum;
        }
        void movex(int offset){
            for (int i = 0; i < xcoords.length; i++) {
                xcoords[i]=xcoords[i]+offset;
            }
        }
        void movey(int offset){
            for (int i = 0; i < ycoords.length; i++) {
                ycoords[i]=ycoords[i]+offset;
            }
        }
        
        int numpoints(){
            return size;
        }

        @Override
        public String toString() {
            
            
            return "Shape{size=" + size + '}';
        }
        
    }
    private static class Point{
        private int x;
        private int y;
        Point next;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        
        @Override
        public String toString() {
            return "Point{" + "x=" + x + ", y=" + y + '}';
        }
        
    }
    
    public static void main(String[] args) {
        
        Shape2 tri1=new Shape2(Arrays.asList(new Point(2, 3),new Point(4, 4),new Point(7, 7),new Point(2, 3),new Point(4, 4),new Point(7, 7)));
        Shape2 tri2=new Shape2(Arrays.asList(new Point(-3, 3),new Point(2, 4),new Point(10, 1),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri3=new Shape2(Arrays.asList(new Point(7, 7),new Point(-1,4),new Point(6, 6),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri4=new Shape2(Arrays.asList(new Point(4, 4),new Point(7, 7),new Point(-1,4),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri5=new Shape2(Arrays.asList(new Point(10, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri6=new Shape2(Arrays.asList(new Point(2, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri7=new Shape2(Arrays.asList(new Point(-3, 3),new Point(2, 4),new Point(10, 1),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri8=new Shape2(Arrays.asList(new Point(7, 7),new Point(-1,4),new Point(6, 6),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri9=new Shape2(Arrays.asList(new Point(4, 4),new Point(7, 7),new Point(-1,4),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri10=new Shape2(Arrays.asList(new Point(10, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri11=new Shape2(Arrays.asList(new Point(2, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri12=new Shape2(Arrays.asList(new Point(-3, 3),new Point(2, 4),new Point(10, 1),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri13=new Shape2(Arrays.asList(new Point(7, 7),new Point(-1,4),new Point(6, 6),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri14=new Shape2(Arrays.asList(new Point(4, 4),new Point(7, 7),new Point(-1,4),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri15=new Shape2(Arrays.asList(new Point(10, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri16=new Shape2(Arrays.asList(new Point(2, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri17=new Shape2(Arrays.asList(new Point(-3, 3),new Point(2, 4),new Point(10, 1),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri18=new Shape2(Arrays.asList(new Point(7, 7),new Point(-1,4),new Point(6, 6),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri19=new Shape2(Arrays.asList(new Point(4, 4),new Point(7, 7),new Point(-1,4),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        Shape2 tri20=new Shape2(Arrays.asList(new Point(10, 3),new Point(4, 4),new Point(7, 7),new Point(-3, 3),new Point(2, 4),new Point(10, 1)));
        
        
        //System.out.println(""+poly);
        
        ShapeList shapelist=new ShapeList(Arrays.asList(tri1,tri2,tri3,tri4,tri5,tri6,tri7,tri8,tri9,tri10,tri11,tri12,tri13,tri14,tri15,tri16,tri17,tri18,tri19,tri20));
        
        //ShapeList shapelist=new ShapeList(Arrays.asList(tri1));
        //System.out.println(""+shapelist);
        
        long start=System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            int totalx=0;
            int totaly=0;
            int totalpoints=0;
            Shape2 s=shapelist.head;
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
