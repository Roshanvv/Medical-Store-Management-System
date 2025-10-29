import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MedicalStoreGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private Connection conn;
    private JTextField searchField;

    public MedicalStoreGUI() {
        setTitle("💊 Medical Store Inventory Dashboard");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/medstore?serverTimezone=UTC",
                    "root",
                    "Mysql@9090"
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed: " + e.getMessage());
            System.exit(0);
        }

        model = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Price", "Quantity"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.setSelectionBackground(Color.decode("#D0E8FF"));
        loadTable();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("🛒 Inventory Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(18);
        JButton searchBtn = new JButton("Search");
        JButton resetBtn = new JButton("Reset");

        topPanel.add(titleLabel);
        topPanel.add(Box.createHorizontalStrut(25));
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(resetBtn);

        searchBtn.addActionListener(e -> searchProduct());
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            loadTable();
        });
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                searchProduct();
            }
        });

        JPanel bottomPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        JButton logoutBtn = new JButton("Logout");

        bottomPanel.add(addBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(refreshBtn);
        bottomPanel.add(logoutBtn);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addProduct());
        editBtn.addActionListener(e -> updateProduct());
        deleteBtn.addActionListener(e -> deleteProduct());
        refreshBtn.addActionListener(e -> loadTable());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage().setVisible(true);
        });
    }

    private void loadTable() {
        try {
            model.setRowCount(0);
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM products");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("name"),
                        rs.getString("category"), rs.getDouble("unit_price"),
                        rs.getInt("quantity")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void searchProduct() {
        String keyword = searchField.getText().trim();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM products WHERE name LIKE ?"
            );
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("unit_price"),
                        rs.getInt("quantity")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void addProduct() {
        JTextField name = new JTextField();
        JTextField cat = new JTextField();
        JTextField price = new JTextField();
        JTextField qty = new JTextField();
        Object[] inputs = {"Name:", name, "Category:", cat, "Price:", price, "Quantity:", qty};

        if (JOptionPane.showConfirmDialog(this, inputs,
                "Add Product", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO products(name, category, unit_price, quantity) VALUES (?, ?, ?, ?)"
                );
                ps.setString(1, name.getText());
                ps.setString(2, cat.getText());
                ps.setDouble(3, Double.parseDouble(price.getText()));
                ps.setInt(4, Integer.parseInt(qty.getText()));
                ps.executeUpdate();
                loadTable();
                JOptionPane.showMessageDialog(this, "Added Successfully");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: Check inputs");
            }
        }
    }

    private void updateProduct() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a product to edit");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        JTextField newPrice = new JTextField(model.getValueAt(row, 3).toString());
        JTextField newQty = new JTextField(model.getValueAt(row, 4).toString());
        Object[] inputs = {"New Price:", newPrice, "New Quantity:", newQty};

        if (JOptionPane.showConfirmDialog(this, inputs,
                "Update Product", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE products SET unit_price=?, quantity=? WHERE id=?"
                );
                ps.setDouble(1, Double.parseDouble(newPrice.getText()));
                ps.setInt(2, Integer.parseInt(newQty.getText()));
                ps.setInt(3, id);
                ps.executeUpdate();
                loadTable();
                JOptionPane.showMessageDialog(this, "Updated Successfully");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a product first");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this,
                "Are you sure to delete?", "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM products WHERE id=?"
                );
                ps.setInt(1, id);
                ps.executeUpdate();
                loadTable();
                JOptionPane.showMessageDialog(this, "Deleted Successfully");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }
}
