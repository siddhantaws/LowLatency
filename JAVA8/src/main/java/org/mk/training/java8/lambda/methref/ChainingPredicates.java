/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.java8.lambda.methref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.function.Predicate;
import org.mk.training.java8.lambda.methref.MethodReferenceDemo.Person;

/**
 *
 * @author mohit
 */
public class ChainingPredicates {

    public static void main(String... args) {
        List<Person> people = Arrays.asList(new Person(60, "Feynman"), new Person(45, "Wolff"), new Person(55, "Wolff"), new Person(97, "Erwin"),
                new Person(60, "David"), new Person(78, "David"));

        Predicate<Person> Wolffs = p -> (p.getName().equals("Wolff"));
        Predicate<Person> notWolff = Wolffs.negate();
        Predicate<Person> notWolffandover60 = notWolff.and(p -> (p.getAge() > 60));

        System.out.println("" + filter(people, notWolffandover60));

    }

    public static <T> List<T> filter(List<T> inventory, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T type : inventory) {
            if (p.test(type)) {
                result.add(type);
            }
        }
        return result;
    }
}
