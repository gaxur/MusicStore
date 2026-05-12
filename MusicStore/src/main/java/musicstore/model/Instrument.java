package main.java.musicstore.model;

/**
 * Represents a musical instrument in the store
 * Extends {@link Product} by adding brand, type of instrument,
 * and whether it is suitable for beginners
 *
 * @author Marcos Galan Carrillo
 */

public class Instrument extends Product {

    // Instrument brand
    private String brand;

    // Instrument type (e.g., guitar, piano, etc.)
    private String type;

    // Indicates if the instrument is suitable for beginners
    private boolean forRookies;


    /**
     * Instrument constructor
     *
     * @param id              unique identifier
     * @param name            instrument name
     * @param price           price (euros)
     * @param stock           available stock
     * @param brand           instrument brand
     * @param type            instrument type
     * @param forRookies true whether the instrument is suitable for beginners
     */

    public Instrument(int id, String name, double price, int stock,
                      String brand, String type, boolean forRookies) {
        super(id, name, price, stock);
        this.brand = brand;
        this.type = type;
        this.forRookies = forRookies;
    }


    /**
     * Returns the brand of the instrument
     * @return instrument brand
     */

    public String getBrand() { return brand; }


    /**
     * Sets the brand of the instrument
     * @param brand instrument new brand
     */

    public void setBrand(String brand) { this.brand = brand; }


    /**
     * Returns the type of the instrument
     * @return instrument type
     */

    public String getType() { return type; }


    /**
     * Sets the type of the instrument
     * @param type instrument new type
     */

    public void setType(String type) { this.type = type; }


    /**
     * Indicates if the instrument is suitable for beginners
     * @return true whether the instrument is suitable for beginners
     */

    public boolean isForRookies() { return forRookies; }


    /**
     * Sets whether the instrument is suitable for beginners
     * @param forRookies new value indicating if the instrument is suitable for beginners
     */

    public void setForRookies(boolean forRookies) {
        this.forRookies = forRookies;
    }


    /**
     * {@inheritDoc}
     * @return "Instrument"
     */

    @Override
    public String getCategory() { return "Instrument"; }


    /**
     * {@inheritDoc}
     */

    @Override
    public String getDetailedDescription() {
        return String.format("Instrument: %s | Brand: %s | Type: %s | Rookies: %s | Price: %.2f€ | Stock: %d",
                getName(), brand, type, forRookies ? "Yes" : "No", getPrice(), getStock());
    }


    /**
     * {@inheritDoc}
     * Format: INSTRUMENT;id;name;price;stock;brand;type;forRookies
     */

    @Override
    public String toCSV() {
        return String.format("INSTRUMENT;%d;%s;%.2f;%d;%s;%s;%b",
                getId(), getName(), getPrice(), getStock(), brand, type, forRookies);
    }
}

