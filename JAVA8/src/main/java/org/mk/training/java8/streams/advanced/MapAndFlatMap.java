package org.mk.training.java8.streams.advanced;

import java.util.*;
import java.util.stream.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class MapAndFlatMap {

    public static void main(String... args) {

        List<String> hw = Arrays.asList("Hello", "World");

        hw.stream()
                .map(word -> word.split(""))
                .distinct()
                .forEach(System.out::println);
        System.out.println("---");
        hw.stream()
                .map(word -> word.split(""))
                .map(Arrays::stream)
                .distinct()
                .forEach(System.out::println);
        System.out.println("---");
        hw.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .forEach(System.out::println);

    }

}
