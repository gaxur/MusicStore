package musicstore.model;

/**
 * Represents a musical accessory in the store (strings, drumsticks, cases, etc.)
 * Extends {@link Product} adding specific attributes like compatibility and material
 *
 * @author Marcos Galán Carrillo
 */
public class Accessory extends Product {

    // Instruments compatible with the accessory (e.g., "guitars, drums")
    private String compatibility;

    // Material of the accessory (e.g., "wood, plastic")
    private String material;


    /**
     * Accessory constructor
     *
     * @param id             identifier of the accessory
     * @param name           accessory name
     * @param price          accessory price (euros)
     * @param stock          available stock
     * @param compatibility  compatible instruments
     * @param material       accessory material
     */
    public Accessory(int id, String name, double price, int stock,
                     String compatibility, String material) {
        super(id, name, price, stock);
        this.compatibility = compatibility;
        this.material = material;
    }


    /**
     * Returns the compatibility of the accessory
     * @return compatible instruments
     */
    public String getCompatibility() { return compatibility; }


    /**
     * Sets the compatibility of the accessory
     * @param compatibility new compatibility
     */
    public void setCompatibility(String compatibility) {
        this.compatibility = compatibility;
    }


    /**
     * Returns the material of the accessory
     * @return material
     */
    public String getMaterial() { return material; }


    /**
     * Sets the material of the accessory
     * @param material new material
     */
    public void setMaterial(String material) { this.material = material; }


    /**
     * {@inheritDoc}
     * @return "Accessory"
     */
    @Override
    public String getCategory() { return "Accessory"; }


    /**
     * {@inheritDoc}
     * @return detailed description of the accessory with all attributes
     */
    @Override
    public String getDetailedDescription() {
        return String.format("Accessory: %s | Compatibility: %s | Material: %s | Price: %.2f€ | Stock: %d",
                getName(), compatibility, material, getPrice(), getStock());
    }


    /**
     * {@inheritDoc}
     * Format: ACCESSORY;id;name;price;stock;compatibility;material
     */
    @Override
    public String toCSV() {
        return String.format("ACCESSORY;%d;%s;%.2f;%d;%s;%s",
                getId(), getName(), getPrice(), getStock(), compatibility, material);
    }
}
