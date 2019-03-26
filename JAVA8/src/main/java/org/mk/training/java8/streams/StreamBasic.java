package org.mk.training.java8.streams;

import java.util.*;
import java.util.stream.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class StreamBasic {

    public static void main(String... args) {
        // Java 7
        getLowCaloricDishesNamesInJava7(Dish.menu).forEach(System.out::println);

        System.out.println("---");

        // Java 8
        getLowCaloricDishesNamesInJava8(Dish.menu).forEach(System.out::println);

        System.out.println("---");
        Dish.menu.stream()
                .filter(d -> d.getCalories() > 400)
                .map(Dish::getName)
                .limit(3)
                .collect(toList())
                .forEach(System.out::println);
        System.out.println("---");
        List<String> names = Dish.menu.stream()
                .filter(d -> {
                    System.out.println("Filtering:" + d.getName());
                    return d.getCalories() > 400;
                })
                .map(d -> {
                    System.out.println("Mapping:" + d.getName());
                    return d.getName();
                })
                .limit(3)
                .collect(toList());
        System.out.println("names:" + names);
    }

    public static List<String> getLowCaloricDishesNamesInJava7(List<Dish> dishes) {
        List<Dish> lowCaloricDishes = new ArrayList<>();
        for (Dish d : dishes) {
            if (d.getCalories() > 400) {
                lowCaloricDishes.add(d);
            }
        }
        List<String> lowCaloricDishesName = new ArrayList<>();
        Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
            public int compare(Dish d1, Dish d2) {
                return Integer.compare(d1.getCalories(), d2.getCalories());
            }
        });
        for (Dish d : lowCaloricDishes) {
            lowCaloricDishesName.add(d.getName());
        }
        return lowCaloricDishesName;
    }

    public static List<String> getLowCaloricDishesNamesInJava8(List<Dish> dishes) {
        return dishes.stream()
                .filter(d -> d.getCalories() > 400)
                .sorted(comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(toList());
    }
}
