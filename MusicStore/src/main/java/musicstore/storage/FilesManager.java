package main.java.musicstore.storage;

import main.java.musicstore.model.*;

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

            if ("ALBUM".equals(type)) {
                return new Album(id, name, price, stock,
                        parts[5], parts[6], Integer.parseInt(parts[7]));
            } else if ("INSTRUMENT".equals(type)) {
                return new Instrument(id, name, price, stock,
                        parts[5], parts[6], Boolean.parseBoolean(parts[7]));
            } else if ("ACCESSORY".equals(type)) {
                return new Accessory(id, name, price, stock,
                        parts[5], parts[6]);
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error parsing the line: " + line);
            return null;
        }
    }


    /**
     * Export a report of the current products in the shop to a text file
     * The report will include a summary of the inventory and a list of all products with details
     *
     * @param shop shop with the products to export
     * @param filePath path of the file to write the report
     * @throws IOException whether an error occurs during writing
     */

    public static void exportReport(Shop<Product> shop, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("=======================================");
            writer.newLine();
            writer.write("       MUSICSTORE INVENTORY REPORT      ");
            writer.newLine();
            writer.write("=======================================");
            writer.newLine();
            writer.newLine();

            writer.write("SUMMARY:");
            writer.newLine();
            writer.write("Total Products: " + shop.getTotalProducts());
            writer.newLine();
            writer.write("Total Inventory Value: " + String.format("%.2f€", shop.getInventoryValue()));
            writer.newLine();
            writer.newLine();

            writer.write("PRODUCTS:");
            writer.newLine();
            writer.write("---------------------------------------");
            writer.newLine();

            for (Product p : shop.getAll()) {
                writer.write(p.getDetailedDescription());
                writer.newLine();
            }

            writer.write("---------------------------------------");
            writer.newLine();
            writer.write("End of Report");
            writer.newLine();
        }
    }
}

