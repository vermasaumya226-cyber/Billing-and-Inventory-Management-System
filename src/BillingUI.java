import dao.OrderDAO;
import dao.ProductDAO;
import model.Order;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BillingUI extends JFrame {

    private JTextField txtCustomerName, txtCustomerPhone, txtQuantity, txtDiscount;
    private JComboBox<String> comboProducts;
    private JTable billTable;
    private DefaultTableModel billTableModel;
    private JTextArea txtReceipt;
    private JLabel lblTotal, lblGST, lblGrandTotal;

    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private List<Product> availableProducts;
    private double totalAmount = 0.0;

    public BillingUI() {
        productDAO = new ProductDAO();
        orderDAO = new OrderDAO();

        setTitle("Billing Inventory System - Customer Billing with Discount");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(630);

        // LEFT PANEL
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));

        // Customer Details Panel
        JPanel custPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        custPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));
        custPanel.add(new JLabel("Customer Name:"));
        txtCustomerName = new JTextField();
        custPanel.add(txtCustomerName);

        custPanel.add(new JLabel("Phone Number:"));
        txtCustomerPhone = new JTextField();
        custPanel.add(txtCustomerPhone);

        // Product Selection Panel with Discount Field
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        itemPanel.setBorder(BorderFactory.createTitledBorder("Add Product to Bill"));

        comboProducts = new JComboBox<>();
        txtQuantity = new JTextField(3);
        txtDiscount = new JTextField("0", 3); // Default 0% discount
        JButton btnAddToCart = new JButton("Add Item");
        JButton btnAddNewProduct = new JButton("+ New Product");
        JButton btnManageProducts = new JButton("Manage Products");

        itemPanel.add(new JLabel("Select:"));
        itemPanel.add(comboProducts);
        itemPanel.add(new JLabel("Qty:"));
        itemPanel.add(txtQuantity);
        itemPanel.add(new JLabel("Disc (%):"));
        itemPanel.add(txtDiscount);
        itemPanel.add(btnAddToCart);
        itemPanel.add(btnAddNewProduct);
        itemPanel.add(btnManageProducts);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(custPanel, BorderLayout.NORTH);
        topContainer.add(itemPanel, BorderLayout.SOUTH);

        leftPanel.add(topContainer, BorderLayout.NORTH);

        // Bill Table with Discount Column
        billTableModel = new DefaultTableModel(new String[]{"Item Name", "Price (₹)", "Qty", "Disc (%)", "Final Total (₹)"}, 0);
        billTable = new JTable(billTableModel);
        leftPanel.add(new JScrollPane(billTable), BorderLayout.CENTER);

        // Bottom Totals & Action Buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel summaryPanel = new JPanel(new GridLayout(3, 1));
        lblTotal = new JLabel("Sub Total: ₹0.00");
        lblGST = new JLabel("GST (18%): ₹0.00");
        lblGrandTotal = new JLabel("Grand Total: ₹0.00");
        lblGrandTotal.setFont(new Font("Arial", Font.BOLD, 14));

        summaryPanel.add(lblTotal);
        summaryPanel.add(lblGST);
        summaryPanel.add(lblGrandTotal);

        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton btnGenerateBill = new JButton("Generate & Save Bill");
        JButton btnViewOrders = new JButton("View Past Orders");
        JButton btnClear = new JButton("Clear / New Bill");

        actionPanel.add(btnGenerateBill);
        actionPanel.add(btnViewOrders);
        actionPanel.add(btnClear);

        bottomPanel.add(summaryPanel, BorderLayout.WEST);
        bottomPanel.add(actionPanel, BorderLayout.EAST);

        leftPanel.add(bottomPanel, BorderLayout.SOUTH);

        // RIGHT PANEL
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Generated Receipt"));
        txtReceipt = new JTextArea();
        txtReceipt.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtReceipt.setEditable(false);
        rightPanel.add(new JScrollPane(txtReceipt), BorderLayout.CENTER);

        mainSplitPane.setLeftComponent(leftPanel);
        mainSplitPane.setRightComponent(rightPanel);

        add(mainSplitPane, BorderLayout.CENTER);

        loadProductDropdown();

        // Listeners
        btnAddToCart.addActionListener(e -> addItemToCart());
        btnAddNewProduct.addActionListener(e -> openAddProductDialog());
        btnManageProducts.addActionListener(e -> openManageProductsDialog());
        btnGenerateBill.addActionListener(e -> generateAndSaveReceipt());
        btnViewOrders.addActionListener(e -> showOrdersHistory());
        btnClear.addActionListener(e -> resetBill());
    }

    private void loadProductDropdown() {
        comboProducts.removeAllItems();
        availableProducts = productDAO.getAllProducts();
        for (Product p : availableProducts) {
            comboProducts.addItem(p.getName() + " (₹" + p.getPrice() + " | Stock: " + p.getQuantity() + ")");
        }
    }

    private void openAddProductDialog() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField qtyField = new JTextField();

        Object[] message = {
                "Product Name:", nameField,
                "Price (₹):", priceField,
                "Quantity (Stock):", qtyField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Product to Database", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int qty = Integer.parseInt(qtyField.getText().trim());

                if (!name.isEmpty() && productDAO.addProduct(new Product(name, price, qty))) {
                    JOptionPane.showMessageDialog(this, "Product Saved / Updated in Database!");
                    loadProductDropdown();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save product!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Inputs!");
            }
        }
    }

    private void openManageProductsDialog() {
        JDialog dialog = new JDialog(this, "Manage Inventory Products", true);
        dialog.setSize(550, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Product Name", "Price (₹)", "Stock"}, 0);
        JTable table = new JTable(model);

        List<Product> products = productDAO.getAllProducts();
        for (Product p : products) {
            model.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getQuantity()});
        }

        JButton btnDelete = new JButton("Delete Selected Product");
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int productId = (int) model.getValueAt(selectedRow, 0);
                String prodName = model.getValueAt(selectedRow, 1).toString();

                int confirm = JOptionPane.showConfirmDialog(dialog, "Are you sure you want to delete '" + prodName + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (productDAO.deleteProduct(productId)) {
                        JOptionPane.showMessageDialog(dialog, "Product deleted successfully!");
                        model.removeRow(selectedRow);
                        loadProductDropdown();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to delete product!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a product from the table first!");
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnDelete);

        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Updated Item Add logic with Percentage Discount Calculation
    private void addItemToCart() {
        int selectedIdx = comboProducts.getSelectedIndex();
        if (selectedIdx == -1) {
            JOptionPane.showMessageDialog(this, "No product selected!");
            return;
        }

        try {
            int qty = Integer.parseInt(txtQuantity.getText().trim());
            double discountPercent = Double.parseDouble(txtDiscount.getText().trim());
            Product selectedProd = availableProducts.get(selectedIdx);

            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!");
                return;
            }

            if (discountPercent < 0 || discountPercent > 100) {
                JOptionPane.showMessageDialog(this, "Discount percentage must be between 0 and 100!");
                return;
            }

            if (qty > selectedProd.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Stock insufficient! Available: " + selectedProd.getQuantity());
                return;
            }

            // Calculation Formula
            double baseTotal = selectedProd.getPrice() * qty;
            double discountAmount = baseTotal * (discountPercent / 100.0);
            double finalItemTotal = baseTotal - discountAmount;

            billTableModel.addRow(new Object[]{
                    selectedProd.getName(),
                    selectedProd.getPrice(),
                    qty,
                    discountPercent + "%",
                    String.format("%.2f", finalItemTotal)
            });

            totalAmount += finalItemTotal;
            updateSummary();
            txtQuantity.setText("");
            txtDiscount.setText("0"); // Reset to 0 after adding

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for Qty and Discount!");
        }
    }

    private void updateSummary() {
        double gst = totalAmount * 0.18;
        double grandTotal = totalAmount + gst;

        lblTotal.setText(String.format("Sub Total: ₹%.2f", totalAmount));
        lblGST.setText(String.format("GST (18%%): ₹%.2f", gst));
        lblGrandTotal.setText(String.format("Grand Total: ₹%.2f", grandTotal));
    }

    private void generateAndSaveReceipt() {
        String custName = txtCustomerName.getText().trim();
        String custPhone = txtCustomerPhone.getText().trim();

        if (custName.isEmpty() || custPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Customer Name and Phone Number!");
            return;
        }

        if (billTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please add at least one item to the bill!");
            return;
        }

        double gst = totalAmount * 0.18;
        double grandTotal = totalAmount + gst;

        Order order = new Order(custName, custPhone, grandTotal);
        boolean saved = orderDAO.saveOrder(order);

        if (saved) {
            JOptionPane.showMessageDialog(this, "Bill generated & saved to database successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Warning: Failed to save bill in database!");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=============================================\n");
        sb.append("         INVENTORY BILLING RECEIPT           \n");
        sb.append("=============================================\n");
        sb.append("Customer Name  : ").append(custName).append("\n");
        sb.append("Phone Number   : ").append(custPhone).append("\n");
        sb.append("---------------------------------------------\n");
        sb.append(String.format("%-12s %-6s %-4s %-6s %-8s\n", "Item", "Price", "Qty", "Disc", "Total"));
        sb.append("---------------------------------------------\n");

        for (int i = 0; i < billTableModel.getRowCount(); i++) {
            String name = billTableModel.getValueAt(i, 0).toString();
            String price = billTableModel.getValueAt(i, 1).toString();
            String qty = billTableModel.getValueAt(i, 2).toString();
            String disc = billTableModel.getValueAt(i, 3).toString();
            String itemTot = billTableModel.getValueAt(i, 4).toString();

            sb.append(String.format("%-12s %-6s %-4s %-6s %-8s\n", name, price, qty, disc, itemTot));
        }

        sb.append("---------------------------------------------\n");
        sb.append(String.format("Sub Total   : ₹%.2f\n", totalAmount));
        sb.append(String.format("GST (18%%)   : ₹%.2f\n", gst));
        sb.append(String.format("Grand Total : ₹%.2f\n", grandTotal));
        sb.append("=============================================\n");
        sb.append("        Thank You For Shopping!              \n");

        txtReceipt.setText(sb.toString());
    }

    private void showOrdersHistory() {
        JDialog dialog = new JDialog(this, "Customer Order History", true);
        dialog.setSize(600, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        DefaultTableModel model = new DefaultTableModel(new String[]{"Order ID", "Customer Name", "Phone", "Total Amount (₹)"}, 0);
        JTable ordersTable = new JTable(model);

        List<Order> orders = orderDAO.getAllOrders();
        for (Order o : orders) {
            model.addRow(new Object[]{o.getOrderId(), o.getCustomerName(), o.getCustomerPhone(), "₹" + o.getTotalAmount()});
        }

        JButton btnDeleteOrder = new JButton("Delete Selected Order");
        btnDeleteOrder.addActionListener(e -> {
            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow != -1) {
                int orderId = (int) model.getValueAt(selectedRow, 0);
                String custName = model.getValueAt(selectedRow, 1).toString();

                int confirm = JOptionPane.showConfirmDialog(dialog, "Are you sure you want to delete order for '" + custName + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (orderDAO.deleteOrder(orderId)) {
                        JOptionPane.showMessageDialog(dialog, "Order deleted successfully!");
                        model.removeRow(selectedRow); // Live table refresh
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to delete order!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select an order from the table first!");
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnDeleteOrder);

        dialog.add(new JScrollPane(ordersTable), BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void resetBill() {
        txtCustomerName.setText("");
        txtCustomerPhone.setText("");
        txtQuantity.setText("");
        txtDiscount.setText("0");
        txtReceipt.setText("");
        billTableModel.setRowCount(0);
        totalAmount = 0.0;
        updateSummary();
        loadProductDropdown();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BillingUI().setVisible(true));
    }
}