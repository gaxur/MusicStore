package main.java.musicstore.gui;

import main.java.musicstore.model.*;
import main.java.musicstore.storage.FilesManager;
import main.java.musicstore.util.ProductTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main screen of the MusicStore application
 * This class provides a user-friendly interface to manage the product catalog of the music store
 *
 * <p>Main features:</p>
 * <ul>
 *   <li>Products visualization</li>
 *   <li>Add, edit and delete products</li>
 *   <li>Filtering by category and search by name</li>
 *   <li>Save and load data from a file</li>
 *   <li>View full product details</li>
 * </ul>
 *
 * @author Marcos Galán Carrillo
 */

public class MainWindow extends JFrame {

    private final Shop<Product> shop;
    private ProductTableModel tableModel;
    private JTable table;
    private JLabel lblState;
    private JTextField txtSearch;
    private JComboBox<String> chFilter;


    /**
     * Main window constructor
     * Initializes the UI and loads the product data from the provided shop
     *
     * @param shop shop instance containing the product catalog to manage
     */

    public MainWindow(Shop<Product> shop) {
        this.shop = shop;
        initUI();
        updateTable();
        updateState();
    }


    /**
     * Initialize the user interface components and layout of the main window
     * This method sets up the main panels, buttons, table and menu of the application
     */

    private void initUI() {
        setTitle("MusicStore - Catalog Management");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(850, 550));
        setSize(950, 620);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        // Principal panel with BorderLayout to organize the main sections of the UI
        JPanel contentPane = new JPanel(new BorderLayout(5, 5));
        contentPane.setBorder(new EmptyBorder(10, 10, 5, 10));
        setContentPane(contentPane);

        // Superior panel with title, search and filter controls
        contentPane.add(createSuperiorPanel(), BorderLayout.NORTH);

        // Central table to display the list of products
        contentPane.add(createTablePanel(), BorderLayout.CENTER);

        // Right buttons panel to perform actions on the products
        contentPane.add(createButtonsPanel(), BorderLayout.EAST);

        // State bar at the bottom to show messages and inventory summary
        contentPane.add(createStateBar(), BorderLayout.SOUTH);

        // Menu
        setJMenuBar(createBarMenu());
    }


    /**
     * Create the superior panel containing the shop title, search field, category filter and action buttons
     *
     * @return a JPanel with the configured components for the top section of the main window
     */

    private JPanel createSuperiorPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(new Color(30, 30, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel title = new JLabel(shop.getName());
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        panel.add(title);

        panel.add(Box.createHorizontalStrut(30));

        panel.add(createLabel("Search:"));
        txtSearch = new JTextField(15);
        txtSearch.addActionListener(e -> filtering());
        panel.add(txtSearch);

        JButton searchButton = createIconButton("Search", new Color(70, 130, 180));
        searchButton.addActionListener(e -> filtering());
        panel.add(searchButton);

        panel.add(createLabel("Filter:"));
        chFilter = new JComboBox<>(new String[]{"All", "Album", "Instrument", "Accessory"});
        chFilter.addActionListener(e -> filtering());
        panel.add(chFilter);

        JButton cleanButton = createIconButton("✖ Clean", new Color(100, 100, 100));
        cleanButton.addActionListener(e -> {
            txtSearch.setText("");
            chFilter.setSelectedIndex(0);
            updateTable();
        });
        panel.add(cleanButton);

        return panel;
    }


    /**
     * Helper method to create a JLabel with a consistent style for the superior panel
     *
     * @param txt the text to display in the label
     * @return a JLabel with the specified text and styled for the superior panel
     */

    private JLabel createLabel(String txt) {
        JLabel lbl = new JLabel(txt);
        lbl.setForeground(Color.LIGHT_GRAY);
        return lbl;
    }


    /**
     * Helper method to create a JButton with a consistent style for the superior panel
     *
     * @param txt the text to display on the button
     * @param bg the background color of the button
     * @return a JButton with the specified text, background color and styled for the superior panel
     */

    private JButton createIconButton(String txt, Color bg) {
        JButton btn = new JButton(txt);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return btn;
    }


    /**
     * Create the central panel containing the JTable to display the list of products
     * The table is configured with a custom model, cell renderer and mouse listener for double-click details
     *
     * @return a JScrollPane containing the configured JTable for product visualization
     */

    private JScrollPane createTablePanel() {
        tableModel = new ProductTableModel(shop.getAll());
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Configure header with fixed colors
        table.getTableHeader().setBackground(new Color(50, 50, 80));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        // Custom renderer for header to ensure fixed colors
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(new Color(50, 50, 80));
                setForeground(Color.WHITE);
                setFont(new Font("SansSerif", Font.BOLD, 12));
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                return this;
            }
        });

        // Custom cell renderer to center all product data in the table
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                if (sel) {
                    setBackground(new Color(70, 130, 180));
                    setForeground(Color.WHITE);
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 245, 255));
                    setForeground(Color.DARK_GRAY);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                setOpaque(true);
                return this;
            }
        };

        // Apply the centered renderer to all columns
        for (int i = 0; i < 5; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) viewDetails();
            }
        });

        // Width columns
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(220);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);

        // Wrap the table in a scroll pane with a custom border
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        return scroll;
    }


    /**
     * Create the right panel containing the action buttons to manage the products
     * Each button is styled with a consistent design and has an associated action listener
     *
     * @return a JPanel with the configured buttons for product management
     */

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 10, 0, 0));

        JButton btnAggregate = createActionButton("Add",   new Color(49, 149, 85));
        JButton btnEdit     = createActionButton("Edit",    new Color(65, 115, 156));
        JButton btnDelete   = createActionButton("Delete",  new Color(180, 60, 60));
        JButton btnDetails  = createActionButton("Details",  new Color(100, 100, 150));
        JButton btnSave     = createActionButton("Save",   new Color(80, 80, 80));
        JButton btnLoad     = createActionButton("Load",    new Color(80, 80, 80));
        JButton btnReport   = createActionButton("Report",   new Color(80, 80, 80));

        btnAggregate.addActionListener(e  -> addProduct());
        btnEdit.addActionListener(e   -> editProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnDetails.addActionListener(e -> viewDetails());
        btnSave.addActionListener(e  -> save());
        btnLoad.addActionListener(e   -> load());
        btnReport.addActionListener(e  -> exportReport());

        for (JButton btn : new JButton[]{btnAggregate, btnEdit, btnDelete,
                btnDetails, btnSave, btnLoad, btnReport}) {
            panel.add(btn);
            panel.add(Box.createVerticalStrut(6));
        }

        return panel;
    }


    /**
     * Helper method to create a JButton with a consistent style for the action buttons panel
     *
     * @param txt the text to display on the button
     * @param bg the background color of the button
     * @return a JButton with the specified text, background color and styled for the action buttons panel
     */
    private JButton createActionButton(String txt, Color bg) {
        JButton btn = new JButton(txt);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMaximumSize(new Dimension(130, 36));
        btn.setPreferredSize(new Dimension(130, 36));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return btn;
    }


    /**
     * Create the state bar at the bottom of the main window to show messages and inventory summary
     *
     * @return a JPanel with a JLabel to display the current state of the application
     */
    private JPanel createStateBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(230, 230, 240));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        lblState = new JLabel("Ready");
        lblState.setFont(new Font("SansSerif", Font.PLAIN, 11));
        panel.add(lblState);
        return panel;
    }


    /**
     * Create the menu bar with "File" and "Product" menus, each containing relevant actions
     *
     * @return a JMenuBar with the configured menus and menu items for the application
     */
    private JMenuBar createBarMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem itemSave = new JMenuItem("Save");
        JMenuItem itemLoad  = new JMenuItem("Load");
        JMenuItem itemExit   = new JMenuItem("Exit");

        itemSave.addActionListener(e -> save());
        itemLoad.addActionListener(e  -> load());
        itemExit.addActionListener(e   -> exit());

        fileMenu.add(itemSave);
        fileMenu.add(itemLoad);
        fileMenu.addSeparator();
        fileMenu.add(itemExit);

        JMenu productMenu = new JMenu("Product");
        JMenuItem itemAdd  = new JMenuItem("Add");
        JMenuItem itemEdit   = new JMenuItem("Edit");
        JMenuItem itemDelete = new JMenuItem("Delete");

        itemAdd.addActionListener(e  -> addProduct());
        itemEdit.addActionListener(e   -> editProduct());
        itemDelete.addActionListener(e -> deleteProduct());

        productMenu.add(itemAdd);
        productMenu.add(itemEdit);
        productMenu.add(itemDelete);

        menuBar.add(fileMenu);
        menuBar.add(productMenu);
        return menuBar;
    }


    /**
     * Show a dialog to add a new product to the shop
     */

    private void addProduct() {
        DialogProduct dialog = new DialogProduct(this, shop);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            shop.addProduct(dialog.getResult());
            updateTable();
            updateState();
            setState("Product correctly added");
        }
    }


    /**
     * Edit the selected product by showing a dialog with the current details
     * If no product is selected, show a warning message
     */

    private void editProduct() {
        int fila = table.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Select a product to edit",
                    "Without selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Product selected = tableModel.getProductInRow(fila);
        DialogProduct dialog = new DialogProduct(this, shop, selected);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            // The product was updated in place using setters
            // No need for delete + add, just refresh the table and UI
            updateTable();
            setState("Product successfully edited");
        }
    }


    /**
     * Delete the selected product after confirming with the user
     * If no product is selected, show a warning message
     */

    private void deleteProduct() {
        int fila = table.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Select a product to delete",
                    "Without selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Product selected = tableModel.getProductInRow(fila);
        int resp = JOptionPane.showConfirmDialog(this,
                "Delete the product: " + selected.getName() + "?",
                "Confirm removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp == JOptionPane.YES_OPTION) {
            shop.deleteProduct(selected.getId());
            updateTable();
            updateState();
            setState("Product deleted");
        }
    }


    /**
     * Show a dialog with the full details of the selected product
     * If no product is selected, show a warning message
     */

    private void viewDetails() {
        int fila = table.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Select a product to view details",
                    "Without selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Product p = tableModel.getProductInRow(fila);
        JOptionPane.showMessageDialog(this, p.getDetailedDescription(),
                "Details: " + p.getName(), JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Apply the current search text and category filter to the product list
     * Then update the table with the filtered results
     */

    private void filtering() {
        String txt = txtSearch.getText().trim();
        String category = (String) chFilter.getSelectedItem();
        List<Product> result;

        if ("All".equals(category)) {
            result = txt.isEmpty() ? shop.getAll() : shop.searchByName(txt);
        } else {
            result = shop.filterByCategory(category);
            if (!txt.isEmpty()) {
                result = result.stream()
                        .filter(p -> p.getName().toLowerCase().contains(txt.toLowerCase()))
                        .collect(Collectors.toList());

            }
        }
        tableModel.setProducts(result);
        setState("Showing " + result.size() + " product(s)");
    }


    /**
     * Save the current product catalog to a file in CSV format
     */

    private void save() {
        JFileChooser fc = new JFileChooser(".");
        fc.setSelectedFile(new java.io.File("catalog.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                FilesManager.save(shop, fc.getSelectedFile().getAbsolutePath());
                setState("Catalog save in: " + fc.getSelectedFile().getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Load the product catalog from a file, replacing the current content
     */

    private void load() {
        JFileChooser fc = new JFileChooser(".");
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                // Replace the content: delete everything and load from the file
                for (Product p : shop.getAll()) {
                    shop.deleteProduct(p.getId());
                }
                FilesManager.load(shop, fc.getSelectedFile().getAbsolutePath());
                updateTable();
                updateState();
                setState("Catalog loaded from: " + fc.getSelectedFile().getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Export a report of the current products in the shop to a text file
     * The report will include a summary of the inventory and a list of all products with details
     */

    private void exportReport() {
        JFileChooser fc = new JFileChooser(".");
        fc.setSelectedFile(new java.io.File("report.txt"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                FilesManager.exportReport(shop, fc.getSelectedFile().getAbsolutePath());
                setState("Exported report: " + fc.getSelectedFile().getName());
                JOptionPane.showMessageDialog(this, "Report exported successfully",
                        "Successfulness", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Handle the exit action with a confirmation dialog
     * If the user chooses to save, it will call the save() method before exiting
     */

    private void exit() {
        int resp = JOptionPane.showConfirmDialog(this,
                "Do yo want to save before exiting?", "Exit",
                JOptionPane.YES_NO_CANCEL_OPTION);
        if (resp == JOptionPane.YES_OPTION) save();
        if (resp != JOptionPane.CANCEL_OPTION) System.exit(0);
    }


    /**
     * Refresh the product table with the current list of products from the shop
     */

    private void updateTable() {
        tableModel.setProducts(shop.getAll());
    }


    /**
     * Update the status bar with the current total products and inventory value
     */

    private void updateState() {
        lblState.setText(String.format("Total: %d products | Inventory value: %.2f€",
                shop.getTotalProducts(), shop.getInventoryValue()));
    }


    /**
     * Set a temporary message in the status bar
     * Then revert to the default state after a few seconds
     *
     * @param message the message to display temporarily in the status bar
     */

    private void setState(String message) {
        lblState.setText(message);
        // Come back to the normal state after 4 secs
        Timer t = new Timer(4000, e -> updateState());
        t.setRepeats(false);
        t.start();
    }
}

