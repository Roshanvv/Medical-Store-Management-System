import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegisterPage() {
        setTitle("Create Account");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("Register New User");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setBounds(120, 20, 200, 30);
        add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 80, 100, 30);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 80, 180, 30);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 130, 100, 30);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 130, 180, 30);
        add(passwordField);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(80, 190, 110, 30);
        add(registerBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(210, 190, 110, 30);
        add(backBtn);

        registerBtn.addActionListener(e -> registerUser());
        backBtn.addActionListener(e -> {
            dispose();
            new LoginPage().setVisible(true);
        });
    }

    private void registerUser() {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/medstore", 
                "root", 
                "Mysql@9090"
            );

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users(username, password) VALUES (?, ?)");
            ps.setString(1, usernameField.getText());
            ps.setString(2, passwordField.getText());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Account Created!");

            dispose();
            new LoginPage().setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Username already exists");
        }
    }
}
