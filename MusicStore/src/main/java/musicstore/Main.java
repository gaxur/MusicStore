package main.java.musicstore;

import main.java.musicstore.gui.MainWindow;
import main.java.musicstore.model.*;
import main.java.musicstore.storage.FilesManager;

import javax.swing.*;
import java.io.IOException;

/**
 * Main class of the MusicStore application
 * Initializes the shop with sample data, attempts to load the saved catalog, and launches the GUI
 *
 * @author Marcos Galan Carrillo
 */

public class Main {

    /**
     * Main method that initializes the application, loads data, and starts the GUI
     *
     * @param args command-line arguments (not used)
     */

    public static void main(String[] args) {
        // Set modern Look and Feel (Nimbus - similar to Material Design)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Fallback to system look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }

        // Create the shop instance
        Shop<Product> shop = new Shop<>("MusicStore");

        // Try to load the catalog from file, if it exists. If not, load sample data
        boolean loadedFromFile = false;
        try {
            FilesManager.load(shop);
            if (shop.getTotalProducts() > 0) {
                loadedFromFile = true;
            }
        } catch (IOException e) {
            System.out.println("Not found saved catalog file. Loading example data ...");
        }

        // Example data (only if the file was not loaded successfully)
        if (!loadedFromFile) {
            loadExampleData(shop);
        }

        // Launch the GUI on the Event Dispatch Thread (EDT), Swing's main thread
        final Shop<Product> shopFinal = shop;
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow(shopFinal);
            window.setVisible(true);
        });
    }


    /**
     * Load sample data into the shop
     *
     * @param shop shop where the sample products will be added
     */

    private static void loadExampleData(Shop<Product> shop) {
        // Albums
        shop.addProduct(new Album(shop.getNewId(),
                "Abbey Road", 19.99, 15, "The Beatles", "Rock", 1969));
        shop.addProduct(new Album(shop.getNewId(),
                "Thriller", 17.99, 20, "Michael Jackson", "Pop", 1982));
        shop.addProduct(new Album(shop.getNewId(),
                "Dark Side of the Moon", 22.50, 8, "Pink Floyd", "Rock", 1973));
        shop.addProduct(new Album(shop.getNewId(),
                "Back in Black", 16.99, 12, "AC/DC", "Hard Rock", 1980));

        // Instruments
        shop.addProduct(new Instrument(shop.getNewId(),
                "Guitar Fender Stratocaster", 899.00, 3, "Fender", "Electric guitar", false));
        shop.addProduct(new Instrument(shop.getNewId(),
                "Guitar Yamaha Pacifica", 349.00, 5, "Yamaha", "Electric guitar", true));
        shop.addProduct(new Instrument(shop.getNewId(),
                "Piano Roland FP-30X", 749.00, 2, "Roland", "Digital piano", true));
        shop.addProduct(new Instrument(shop.getNewId(),
                "Drums Pearl Export", 1299.00, 1, "Pearl", "Acoustic drums", false));

        // Accessories
        shop.addProduct(new Accessory(shop.getNewId(),
                "Strings Ernie Ball .010", 8.99, 50, "Electric guitar", "Nickel-plated steel"));
        shop.addProduct(new Accessory(shop.getNewId(),
                "Drumsticks Vic Firth 5A", 11.50, 30, "Drums", "Maple tree wood"));
        shop.addProduct(new Accessory(shop.getNewId(),
                "Padded guitar case", 29.99, 10, "Guitar", "Nylon/foam"));
        shop.addProduct(new Accessory(shop.getNewId(),
                "Korg Chromatic Tuner", 19.99, 25, "Universal", "Plastic ABS"));
    }
}