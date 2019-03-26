package org.mk.training.java8.completablefuture;


import java.util.Random;
import static org.mk.training.java8.completablefuture.Util.delay;
import static org.mk.training.java8.completablefuture.Util.format;

public class ShopWithDiscount {

    private final String name;
    private final Random random;

    public ShopWithDiscount(String name) {
        this.name = name;
        random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
    }

    public String getPrice(String product) {
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
        return name + ":" + price + ":" + code;
    }

    public double calculatePrice(String product) {
        delay();
        return format(random.nextDouble() * product.charAt(0) + product.charAt(1));
    }

    public String getName() {
        return name;
    }
}
