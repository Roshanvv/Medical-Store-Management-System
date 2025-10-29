import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Sign In");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("Medical Store Sign In");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setBounds(100, 20, 200, 30);
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

        JButton signInBtn = new JButton("Sign In");
        signInBtn.setBounds(60, 190, 120, 30);
        add(signInBtn);

        JButton createAccBtn = new JButton("Create Account");
        createAccBtn.setBounds(200, 190, 140, 30);
        add(createAccBtn);

        signInBtn.addActionListener(e -> authenticateUser());
        createAccBtn.addActionListener(e -> {
            dispose();
            new RegisterPage().setVisible(true);
        });
    }

    private void authenticateUser() {
        String username = usernameField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/medstore", 
                "root", 
                "Mysql@9090"
            );

            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dispose();
                new MedicalStoreGUI().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new LoginPage().setVisible(true);
    }
}
