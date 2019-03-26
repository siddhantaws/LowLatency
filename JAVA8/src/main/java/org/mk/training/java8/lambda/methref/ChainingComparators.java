/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.java8.lambda.methref;

import java.util.Arrays;
import java.util.Comparator;
import static java.util.Comparator.comparing;
import java.util.List;
import org.mk.training.java8.lambda.methref.MethodReferenceDemo.Person;

/**
 *
 * @author mohit
 */
public class ChainingComparators {

    public static void main(String... args) {
        List<Person> people = Arrays.asList(new Person(60, "Feynman"), new Person(45, "Wolff"), new Person(55, "Wolff"), new Person(97, "Erwin"),
                new Person(60, "David"), new Person(78, "David"));

        people.sort(comparing(Person::getName).thenComparing(Person::getAge));
        System.out.println("" + people);

        people.sort(comparing(Person::getName).reversed().thenComparing(Person::getName));
        System.out.println("" + people);

        people.sort(comparing(Person::getName).thenComparing(comparing(Person::getAge).reversed()));
        System.out.println("" + people);

    }
}
