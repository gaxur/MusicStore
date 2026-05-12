package main.java.musicstore.gui;

import main.java.musicstore.model.*;

import javax.swing.*;
import java.awt.*;

/**
 * Modal dialog for adding or editing a product in the catalog
 * Displays different fields based on the selected product type
 * (Album, Instrument or Accessory)
 *
 * @author Marcos Galan Carrillo
 */

public class DialogProduct extends JDialog {

    private boolean confirmed = false;
    private Product result;

    private JTextField txtName, txtPrice, txtStock;
    private JComboBox<String> chType;
    private JPanel panelSpecific;

    // Album fields
    private JTextField txtArtist, txtGenre, txtYear;

    // Instrument fields
    private JTextField txtBrand, txtType;
    private JCheckBox isRookie;

    // Accessory fields
    private JTextField txtCompatibility, txtMaterial;

    private final int idProduct;
    private final Shop<Product> shop;


    /**
     * Constructor for adding a new product
     * Generates a new ID from the shop reference
     *
     * @param parent father window
     * @param shop shop reference to get new ID and validate data
     */

    public DialogProduct(JFrame parent, Shop<Product> shop) {
        this(parent, shop, null);
    }


    /**
     * Constructor for editing an existing product
     * Pre-fills fields with the product's current data
     *
     * @param parent    father window
     * @param shop      shop reference to validate data
     * @param existence product for editing (null for adding new)
     */

    public DialogProduct(JFrame parent, Shop<Product> shop, Product existence) {
        super(parent, existence == null ? "Add Product" : "Edit Product", true);
        this.shop = shop;
        this.idProduct = existence == null ? shop.getNewId() : existence.getId();
        this.result = existence;

        initUI();

        if (existence != null) {
            fillData(existence);
            chType.setEnabled(false);
        }

        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }


    /**
     * Initializes the user interface components and layout
     * Sets up event listeners for type selection and buttons
     */

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        // Product type
        gbc.gridx = 0; gbc.gridy = 0;
        panelPrincipal.add(new JLabel("Type:"), gbc);
        chType = new JComboBox<>(new String[]{"Album", "Instrument", "Accessory"});
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(chType, gbc);

        // Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panelPrincipal.add(new JLabel("Name:"), gbc);
        txtName = new JTextField(20);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(txtName, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panelPrincipal.add(new JLabel("Price (€):"), gbc);
        txtPrice = new JTextField(10);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(txtPrice, gbc);

        // Stock
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panelPrincipal.add(new JLabel("Stock:"), gbc);
        txtStock = new JTextField(10);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(txtStock, gbc);

        // Specific details panel
        panelSpecific = new JPanel(new GridBagLayout());
        panelSpecific.setBorder(BorderFactory.createTitledBorder("Specific Details"));
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        panelPrincipal.add(panelSpecific, gbc);

        add(panelPrincipal, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton buttonAccept = new JButton("✔ Accept");
        JButton buttonCancel = new JButton("✖ Cancel");

        buttonAccept.setBackground(new Color(70, 130, 180));
        buttonAccept.setFocusPainted(false);

        buttonAccept.addActionListener(e -> accept());
        buttonCancel.addActionListener(e -> dispose());

        buttonsPanel.add(buttonAccept);
        buttonsPanel.add(buttonCancel);
        add(buttonsPanel, BorderLayout.SOUTH);

        chType.addActionListener(e -> updateSpecificPanel());
        updateSpecificPanel();
    }


    /**
     * Updates the specific details panel based on the selected product type
     */

    private void updateSpecificPanel() {
        panelSpecific.removeAll();
        String type = (String) chType.getSelectedItem();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;

        if ("Album".equals(type)) {
            txtArtist = new JTextField(15);
            txtGenre = new JTextField(15);
            txtYear = new JTextField(6);
            addField(panelSpecific, gbc, 0, "Artist:", txtArtist);
            addField(panelSpecific, gbc, 1, "Genre:", txtGenre);
            addField(panelSpecific, gbc, 2, "Year:", txtYear);

        } else if ("Instrument".equals(type)) {
            txtBrand = new JTextField(15);
            txtType = new JTextField(15);
            isRookie = new JCheckBox("Able for beginners");
            addField(panelSpecific, gbc, 0, "Brand:", txtBrand);
            addField(panelSpecific, gbc, 1, "Type:", txtType);
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            panelSpecific.add(isRookie, gbc);
            gbc.gridwidth = 1;

        } else {
            txtCompatibility = new JTextField(15);
            txtMaterial = new JTextField(15);
            addField(panelSpecific, gbc, 0, "Compatibility:", txtCompatibility);
            addField(panelSpecific, gbc, 1, "Material:", txtMaterial);
        }

        panelSpecific.revalidate();
        panelSpecific.repaint();
        pack();
    }


    /**
     * Helper method to add a label and field to the specific details panel
     *
     * @param panel panel to add fields to
     * @param gbc GridBagConstraints for layout
     * @param row row number to place the field
     * @param tag label text for the field
     * @param field the input component (e.g., JTextField) to add
     */

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String tag, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(tag), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }


    /**
     * Fills the dialog fields with the data from the given product
     *
     * @param p product whose data will be displayed for editing
     */

    private void fillData(Product p) {
        txtName.setText(p.getName());
        txtPrice.setText(String.valueOf(p.getPrice()));
        txtStock.setText(String.valueOf(p.getStock()));

        if (p instanceof Album) {
            Album a = (Album) p;
            chType.setSelectedItem("Album");
            updateSpecificPanel();
            txtArtist.setText(a.getArtist());
            txtGenre.setText(a.getGenre());
            txtYear.setText(String.valueOf(a.getYear()));
        } else if (p instanceof Instrument) {
            Instrument i = (Instrument) p;
            chType.setSelectedItem("Instrument");
            updateSpecificPanel();
            txtBrand.setText(i.getBrand());
            txtType.setText(i.getType());
            isRookie.setSelected(i.isForRookies());
        } else if (p instanceof Accessory) {
            Accessory ac = (Accessory) p;
            chType.setSelectedItem("Accessory");
            updateSpecificPanel();
            txtCompatibility.setText(ac.getCompatibility());
            txtMaterial.setText(ac.getMaterial());
        }
    }


    /**
     * Validates the input fields and creates or updates the product object based on the selected type
     * Uses setters when editing an existing product, creates new object when adding
     * Validates that no product with the same name and type already exists
     */

    private void accept() {
        try {
            String name = txtName.getText().trim();
            double price = Double.parseDouble(txtPrice.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());

            if (name.isEmpty()) throw new IllegalArgumentException("The name cannot be empty");
            if (price < 0) throw new IllegalArgumentException("The price cannot be negative");
            if (stock < 0) throw new IllegalArgumentException("The stock cannot be negative");

            String type = (String) chType.getSelectedItem();

            // Validate that no product with the same name and type exists (excluding the current product if editing)
            boolean duplicateExists = shop.getAll().stream()
                    .anyMatch(p -> p.getName().equalsIgnoreCase(name) &&
                            p.getCategory().equals(type) &&
                            p.getId() != idProduct);

            if (duplicateExists) {
                throw new IllegalArgumentException("A product with the same name and type already exists");
            }

            if ("Album".equals(type)) {
                String artist = txtArtist.getText().trim();
                String genre = txtGenre.getText().trim();
                int year = Integer.parseInt(txtYear.getText().trim());

                if (result == null) {
                    result = new Album(idProduct, name, price, stock, artist, genre, year);
                } else {
                    result.setName(name);
                    result.setPrice(price);
                    result.setStock(stock);
                    ((Album) result).setArtist(artist);
                    ((Album) result).setGenre(genre);
                    ((Album) result).setYear(year);
                }

            } else if ("Instrument".equals(type)) {
                String brand = txtBrand.getText().trim();
                String instrType = txtType.getText().trim();
                boolean isPrinciple = isRookie.isSelected();

                if (result == null) {
                    result = new Instrument(idProduct, name, price, stock, brand, instrType, isPrinciple);
                } else {
                    result.setName(name);
                    result.setPrice(price);
                    result.setStock(stock);
                    ((Instrument) result).setBrand(brand);
                    ((Instrument) result).setType(instrType);
                    ((Instrument) result).setForRookies(isPrinciple);
                }

            } else {
                String compat = txtCompatibility.getText().trim();
                String material = txtMaterial.getText().trim();

                if (result == null) {
                    result = new Accessory(idProduct, name, price, stock, compat, material);
                } else {
                    result.setName(name);
                    result.setPrice(price);
                    result.setStock(stock);
                    ((Accessory) result).setCompatibility(compat);
                    ((Accessory) result).setMaterial(material);
                }
            }

            confirmed = true;
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please, fill it with correct values",
                    "Format error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Shows whether the user confirmed the action (accept) or canceled it
     *
     * @return true if the user accepted the changes, false if they canceled
     */

    public boolean isConfirmed() {
        return confirmed;
    }


    /**
     * Returns the product created or edited in the dialog, if the user confirmed the action
     *
     * @return resultant product if confirmed, null otherwise
     */

    public Product getResult() {
        return result;
    }
}

