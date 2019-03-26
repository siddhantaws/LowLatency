package org.mk.training.java8.completablefuture;

import java.util.List;
import java.util.function.Supplier;

public class BestPriceFinderMainWithDiscount {

    private static BestPriceFinderWithDiscount bestPriceFinder = new BestPriceFinderWithDiscount();

    public static void main(String[] args) {
        //execute("sequential", () -> bestPriceFinder.findPricesSequential("myPhone27S"));
        //execute("parallel", () -> bestPriceFinder.findPricesParallel("myPhone27S"));
        execute("composed CompletableFuture", () -> bestPriceFinder.findPricesFuture("myPhone27S"));
        bestPriceFinder.printPricesStream("myPhone27S");
    }

    private static void execute(String msg, Supplier<List<String>> s) {
        long start = System.nanoTime();
        System.out.println(s.get());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println(msg + " done in " + duration + " msecs");
    }

}
