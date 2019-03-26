/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.springbasics.collection;

/**
 *
 * @author mohit
 */
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {
   public static void main(String[] args) {
      ApplicationContext context = 
             new ClassPathXmlApplicationContext("file:beans.xml");

      JavaCollection jc=(JavaCollection)context.getBean("javaCollection");
      
      jc.getAddressList();
      jc.getAddressSet();
      jc.getAddressMap();
      jc.getAddressProp();
   }
}