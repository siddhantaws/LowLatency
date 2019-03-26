package org.mk.training.java8.lambda.intro;

import java.util.*;
import java.util.function.Predicate;

public class FilteringAnything {

    public static void main(String... args) {

        List<Apple> inventory = Arrays.asList(new Apple(80, "green"), new Apple(155, "green"), new Apple(120, "red"));

        Predicate<Apple> p=(Apple a) -> "red".equals(a.getColor());
        Predicate<Apple> p1=(Apple a) -> "red".equals(a.getColor());
        System.out.println("PredicateLambda:"+(p==p1));
        System.out.println("PredicateLambda:"+p);
        System.out.println("PredicateLambda:"+p1);
        System.out.println("PredicateLambda:"+p.getClass());

        List<Apple> redApples4 = filter(inventory, (Apple a) -> "red".equals(a.getColor()));
        System.out.println(redApples4);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

        List<Integer> evennumbers = filter(numbers, (Integer i) -> i % 2 == 0);
        System.out.println(evennumbers);
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

    public static class Apple {

        private int weight = 0;
        private String color = "";

        public Apple(int weight, String color) {
            this.weight = weight;
            this.color = color;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String toString() {
            return "Apple{"
                    + "color='" + color + '\''
                    + ", weight=" + weight
                    + '}';
        }
    }

}
