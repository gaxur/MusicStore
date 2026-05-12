package main.java.musicstore.util;

import main.java.musicstore.model.Product;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

/**
 * Personalized table model for showing products in a {@link javax.swing.JTable}.
 * Extends {@link AbstractTableModel} for integrating with Swing's table components
 *
 * @author Marcos Galan Carrillo
 */

public class ProductTableModel extends AbstractTableModel {

    // Name of the columns in the table
    private static final String[] COLUMNS = {"ID", "Name", "Category", "Price (€)", "Stock"};

    // Products to show in the table
    private List<Product> products;


    /**
     * Constructor that initializes the table model with a list of products
     *
     * @param products products initial list
     */

    public ProductTableModel(List<Product> products) {
        this.products = new ArrayList<>(products);
    }


    /**
     * Updates the products shown in the table and notifies the table to refresh its display.
     *
     * @param products new products list to show
     */

    public void setProducts(List<Product> products) {
        this.products = new ArrayList<>(products);
        fireTableDataChanged(); // Notify the table that the data has changed so it can refresh
    }


    /**
     * Returns the product at the specified row index in the table
     *
     * @param row row index
     * @return product at the specified row
     */

    public Product getProductInRow(int row) {
        return products.get(row);
    }


    /** {@inheritDoc} */
    @Override
    public int getRowCount() { return products.size(); }

    /** {@inheritDoc} */
    @Override
    public int getColumnCount() { return COLUMNS.length; }

    /** {@inheritDoc} */
    @Override
    public String getColumnName(int col) { return COLUMNS[col]; }

    /** {@inheritDoc} */
    @Override
    public Object getValueAt(int fila, int col) {
        Product p = products.get(fila);
        switch (col) {
            case 0:
                return p.getId();
            case 1:
                return p.getName();
            case 2:
                return p.getCategory();
            case 3:
                return String.format("%.2f", p.getPrice());
            case 4:
                return p.getStock();
            default:
                return "";
        }
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getColumnClass(int col) {
        if (col == 0 || col == 4) return Integer.class;
        return String.class;
    }
}

