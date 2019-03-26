/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mk.training.java8.lambda.methref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static java.util.Comparator.comparing;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author mohit
 */
public class MethodReferenceDemo {

    public static void main(String[] args) {
        List<Person> people = Arrays.asList(new Person(60, "Feynman"), new Person(45, "Wolff"), new Person(55, "Wolff"), new Person(97, "Erwin"),
                new Person(60, "David"), new Person(78, "David"));
        Comparator<Person> comp = new PersonAgeComparator();
        PersonAgeComparator2 comp2=new PersonAgeComparator2();

        Collections.sort(people, (p1, p2) -> Integer.compare(p1.getAge(), p2.getAge()));
        System.out.println(people);
        Collections.sort(people, comparing(Person::getAge).reversed());
        System.out.println(people);
        //type2 method reference
        Collections.sort(people, Person::compareNames);
        System.out.println(people);
        //type2 method reference
        Collections.sort(people, Person::compareAges);
        System.out.println(people);
        //type3 method reference
        
        Collections.sort(people, comp::compare);
        System.out.println(people);
        
        Collections.sort(people, comp2::compare);
        System.out.println(people);
        //Collections.sort(people, PersonAgeComparator::compare);
        Collections.sort(people, Person::compareAgesOf);
        System.out.println(people);
        
        Supplier<Person> personsupp = Person::new;
        System.out.println("Person Default" + personsupp.get());

        BiFunction<Integer, String, Person> personsupp2 = Person::new;
        System.out.println("Person Default" + personsupp2.apply(28, "Tesla"));

        List<String> names = Arrays.asList("Feynman", "Wolff", "Wolff", "Erwin", "David");
        List<Person> people2 = map(names, Person::new);
        System.out.println(people2);

    }

    public static List<Person> map(List<String> names, Function<String, Person> f) {
        List<Person> people = new ArrayList<>();
        for (String name : names) {
            people.add(f.apply(name));
        }
        return people;
    }

    public static class Person {

        public Person(int age, String name) {
            this.age = age;
            this.name = name;
        }

        public Person() {
        }

        public Person(String name) {
            this.name = name;
        }

        private int age;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int compareAges(Person p2) {
            return Integer.compare(this.getAge(), p2.getAge());
        }
        
        public static int compareAgesOf(Person p1,Person p2) {
            return Integer.compare(p1.getAge(), p2.getAge());
        }
        
        public int compareNames(Person o) {
            return this.getName().compareTo(o.getName());
        }

        @Override
        public String toString() {
            return "Person{" + "age=" + age + ", name=" + name + '}';
        }
    }

    static class PersonAgeComparator implements Comparator<Person> {

        @Override
        public int compare(Person p1, Person p2) {
            return Integer.compare(p1.getAge(), p2.getAge());
        }

    }
    
    static class PersonAgeComparator2 {

        //@Override
        public int compare(Person p1, Person p2) {
            return Integer.compare(p1.getAge(), p2.getAge());
        }

    }
}
