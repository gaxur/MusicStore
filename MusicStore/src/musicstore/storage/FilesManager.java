package musicstore.storage;

import musicstore.model.*;

import java.io.*;

/**
 * Class in charge of data persistence for the store
 * Manages reading and writing the catalog to text files
 * using the classes {@link BufferedReader}, {@link BufferedWriter},
 * {@link FileReader} and {@link FileWriter} from Java system
 *
 * @author Marcos Galan Carrillo
 */
public class FilesManager {

    // Default path for the catalog file
    private static final String DEFAULT_FILE = "catalog.csv";


    /**
     * Save all the products in the shop to a CSV file with the specified path
     *
     * @param shop   shop with the products to save
     * @param pathFile final path of the file to write
     * @throws IOException whether an error occurs during writing
     */
    public static void save(Shop<Product> shop, String pathFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathFile))) {
            writer.write("# MusicStore - Catalog");
            writer.newLine();
            for (Product p : shop.getAll()) {
                writer.write(p.toCSV());
                writer.newLine();
            }
        }
    }


    /**
     * Load all the products from a CSV file with the specified path and add them to the shop
     *
     * @param shop shop where the products will be loaded
     * @param filePath path of the file to read
     * @throws IOException whether an error occurs during reading
     */
    public static void load(Shop<Product> shop, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("#") || linea.trim().isEmpty()) continue;
                Product p = parserLine(linea);
                if (p != null) shop.addProduct(p);
            }
        }
    }


    /**
     * Load all the products from the default CSV file and add them to the shop
     *
     * @param shop shop where the products will be loaded
     * @throws IOException whether an error occurs during reading
     */
    public static void load(Shop<Product> shop) throws IOException {
        load(shop, DEFAULT_FILE);
    }

    /**
     * Analyzes a CSV line and creates a Product object based on its content
     *
     * @param line CSV line to analyze
     * @return object Product created, or null whether the format is incorrect
     */
    private static Product parserLine(String line) {
        try {
            String[] parts = line.split(";");
            String type = parts[0];
            int id = Integer.parseInt(parts[1]);
            String name = parts[2];
            // Replace comma with dot to handle decimal separator correctly
            double price = Double.parseDouble(parts[3].replace(",", "."));
            int stock = Integer.parseInt(parts[4]);

            return switch (type) {
                case "ALBUM" -> new Album(id, name, price, stock,
                        parts[5], parts[6], Integer.parseInt(parts[7]));
                case "INSTRUMENT" -> new Instrument(id, name, price, stock,
                        parts[5], parts[6], Boolean.parseBoolean(parts[7]));
                case "ACCESSORY" -> new Accessory(id, name, price, stock,
                        parts[5], parts[6]);
                default -> null;
            };
        } catch (Exception e) {
            System.err.println("Error parsing the line: " + line);
            return null;
        }
    }

    /**
     * Export a report of the shop's inventory to a text file with the specified path
     *
     * @param shop shop to export the report from
     * @param filePath file path where the report will be saved
     * @throws IOException whether an error occurs during writing
     */
    public static void exportReport(Shop<Product> shop, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            writer.println("========================================");
            writer.println("  INVENTORY REPORT - " + shop.getName());
            writer.println("========================================");
            writer.println("Total products: " + shop.getTotalProducts());
            writer.printf("Total inventory value: %.2f€%n", shop.getInventoryValue());
            writer.println("----------------------------------------");
            for (Product p : shop.getAll()) {
                writer.println(p.getDetailedDescription());
            }
            writer.println("========================================");
        }
    }
}
