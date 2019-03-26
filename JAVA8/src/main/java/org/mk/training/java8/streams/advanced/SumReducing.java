package org.mk.training.java8.streams.advanced;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.mk.training.java8.streams.Dish;

public class SumReducing {

    public static void main(String... args) {

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        Integer sum = numbers.stream().reduce(0, (a, b) -> a + b);
        System.out.println("" + sum);

        sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println("" + sum);

        Optional<Integer> osum = numbers.stream().reduce(Integer::sum);
        if (osum.isPresent()) {
            System.out.println("" + osum.get());
        }

        int numdishes = Dish.menu.stream()
                .map(d -> 1)
                .reduce(0, Integer::sum);

        System.out.println("numdishes:" + numdishes);
    }

}
