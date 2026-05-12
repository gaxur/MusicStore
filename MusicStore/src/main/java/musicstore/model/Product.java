package main.java.musicstore.model;

/**
 * Abstract class representing a generic product in the music store
 * It is the root of the inheritance hierarchy of the system
 *
 * @author Marcos Galan Carrillo
 */

public abstract class Product {

    // Unique identifier for the product
    private final int id;

    // Product name
    private String name;

    // Product price in euros
    private double price;

    // Available stock quantity
    private int stock;


    /**
     * Product constructor
     *
     * @param id     unique identifier for the product
     * @param name   name of the product
     * @param price  price (euros)
     * @param stock  available stock
     */

    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }


    /**
     * Returns the unique identifier of the product
     * @return product ID
     */

    public int getId() { return id; }


    /**
     * Returns the name of the product
     * @return product name
     */

    public String getName() { return name; }


    /**
     * Sets the name of the product
     * @param name new name of the product
     */

    public void setName(String name) { this.name = name; }


    /**
     * Returns the price of the product
     * @return price in euros
     */

    public double getPrice() { return price; }


    /**
     * Sets the price of the product
     * @param price new price in euros
     */

    public void setPrice(double price) { this.price = price; }


    /**
     * Returns the available stock quantity of the product
     * @return stock quantity
     */

    public int getStock() { return stock; }


    /**
     * Sets the available stock quantity of the product
     * @param stock new stock quantity
     */

    public void setStock(int stock) { this.stock = stock; }


    /**
     * Abstract method to get the category of the product
     *
     * @return string of the product category
     */

    public abstract String getCategory();


    /**
     * Abstract method to get a detailed description of the product
     *
     * @return detailed description string of the product
     */

    public abstract String getDetailedDescription();


    /**
     * Serializes the product data into a CSV format string
     *
     * @return string in CSV format representing the product data
     */

    public abstract String toCSV();


    /**
     * String representation of the product, showing its name, price and stock
     *
     * @return string representation of the product
     */

    @Override
    public String toString() {
        return String.format("[%d] %s - %.2f€ (stock: %d)", id, name, price, stock);
    }
}

