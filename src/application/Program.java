package application;

import entities.Product;
import handlers.ListHandler;

import java.util.Arrays;
import java.util.List;

public class Program
{
    public static void main(String[] args)
    {
        List<Product> productsList = Arrays.asList
        (
            new Product("Tv", 1200.00),
            new Product("Radio", 20.60),
            new Product("Mouse", 40.50),
            new Product("Keyboard", 80.90),
            new Product("Microphone", 200.00),
            new Product("Notebook", 800.00)
        );
        ListHandler<Product> products = new ListHandler<>(productsList);

        var anonProducts =
                products.
                startingAfter(p -> p.getName().equals("Radio")).
                endingWhere(p -> p.getPrice().equals(200.00)).
                where(p -> p.getPrice() > 30).
                orderDescendingBy(p -> p.getPrice()).
                selectNew
                (
                    p -> "Product name = " + p.getName(),
                    p -> String.format("Price = %.2f", p.getPrice() * 1.1)
                );

        System.out.println(products.containsAny(p -> p.getName().equals("Book")));

        System.out.println(products.first());

        anonProducts.forEach(System.out::println);
        System.out.println(products.max(Product::getPrice));
        System.out.println(products.maxString(Product::getName));

        double min = products.select(Product::getPrice).min();
        System.out.println(min);
        System.out.println(products.min(Product::getPrice));
        System.out.println(products.minString(Product::getName));
        System.out.println(products.select(Product::getName).minString());

        System.out.println();
        products.forEach(System.out::println);
    }
}
