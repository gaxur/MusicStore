package main.java.musicstore.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic class representing the music store's catalog
 * Uses Java Generics to work with any type of {@link Product}
 *
 * @param <T> product type, must extend {@link Product}
 * @author Marcos Galan Carrillo
 */

public class Shop<T extends Product> {

    // Shop name
    private final String name;

    // Catalog of products in the store
    private final List<T> catalog;

    // Counter to generate unique IDs for new products
    private int counterId;


    /**
     * Shop constructor
     *
     * @param name shop name
     */

    public Shop(String name) {
        this.name = name;
        this.catalog = new ArrayList<>();
        this.counterId = 1;
    }


    /**
     * Returns the name of the shop
     *
     * @return shop name
     */

    public String getName() { return name; }


    /**
     * Adds a product to the catalog
     *
     * @param product adding product
     */

    public void addProduct(T product) {
        catalog.add(product);
        if (product.getId() >= counterId) {
            counterId = product.getId() + 1;
        }
    }


    /**
     * Deletes a product from the catalog by its ID
     *
     * @param id product identifier to delete
     */

    public void deleteProduct(int id) {
        catalog.removeIf(p -> p.getId() == id);
    }


    /**
     * Searches for products whose name contains the given text (case-insensitive)
     *
     * @param txt txt to search in product names
     * @return products list whose name contains the text
     */

    public List<T> searchByName(String txt) {
        return catalog.stream()
                .filter(p -> p.getName().toLowerCase().contains(txt.toLowerCase()))
                .collect(Collectors.toList());
    }


    /**
     * Filters products by category
     *
     * @param category filter category
     * @return products list that belongs to the specified category
     */

    public List<T> filterByCategory(String category) {
        return catalog.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }


    /**
     * Returns a complete list of products in the catalog
     *
     * @return complete products list
     */

    public List<T> getAll() {
        return new ArrayList<>(catalog);
    }


    /**
     * Returns the total number of products in the catalog
     *
     * @return products count in the catalog
     */

    public int getTotalProducts() {
        return catalog.size();
    }


    /**
     * Returns a new unique ID for a product, incrementing the internal counter
     *
     * @return new unique product ID
     */

    public int getNewId() {
        return counterId++;
    }


    /**
     * Returns the total value of the inventory, calculated as the sum of (price * stock) for all products
     *
     * @return total value of the inventory in euros
     */

    public double getInventoryValue() {
        return catalog.stream()
                .mapToDouble(p -> p.getPrice() * p.getStock())
                .sum();
    }
}