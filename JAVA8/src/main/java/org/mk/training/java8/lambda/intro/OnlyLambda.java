package org.mk.training.java8.lambda.intro;

import java.util.*;
import java.util.function.Predicate;
import static org.mk.training.java8.lambda.intro.FilteringAnything.filter;
import org.mk.training.java8.lambda.intro.FilteringApples.Apple;

public class OnlyLambda {

    public static void main(String... args) {

       // List<Apple> inventory = Arrays.asList(new Apple(80, "green"), new Apple(155, "green"), new Apple(120, "red"));

       // List<Apple> redApples4 = filter(inventory, (Apple a) -> "red".equals(a.getColor()));
        
        Predicate<Apple> pred=(Apple a) -> "red".equals(a.getColor());
        System.out.println(""+pred);
    }

    
}
