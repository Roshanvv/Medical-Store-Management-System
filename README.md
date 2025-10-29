

# Medical Store Management System

A Java Swing-based desktop application for managing medical store inventory with MySQL database integration.

## Features

- 🔒 User Authentication System
  - Login functionality
  - New user registration
  - Secure password handling

- 📊 Inventory Management
  - View all products in tabular format
  - Real-time search functionality
  - Add new products
  - Edit existing product details
  - Delete products
  - Refresh data view

## Technical Details

- **Language:** Java
- **GUI Framework:** Java Swing
- **Database:** MySQL
- **JDBC Driver:** MySQL Connector/J

## Database Setup

1. Create a MySQL database named `medstore`
2. Create the following tables:

```sql
CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    unit_price DOUBLE,
    quantity INT
);
```

## Configuration

Database connection settings in the application:
- Host: localhost
- Port: 3306
- Database: medstore
- Username: root
- Password: Mysql@9090

## Running the Application

1. Ensure MySQL server is running
2. Make sure MySQL Connector/J is in your classpath
3. Compile the Java files:
```bash
javac *.java
```
4. Run the application:
```bash
java LoginPage
```

## System Requirements

- Java Runtime Environment (JRE) 8 or higher
- MySQL Server 5.7 or higher
- Minimum 2GB RAM
- Screen resolution: 1024x768 or higher

## Features in Detail

- **Search:** Real-time product search by name
- **Add Product:** Add new products with name, category, price, and quantity
- **Edit Product:** Modify price and quantity of existing products
- **Delete Product:** Remove products from inventory
- **Data Refresh:** Instant view updates after modifications

## Files Structure

- LoginPage.java: Handles user authentication
- RegisterPage.java: Manages new user registration
- MedicalStoreGUI.java: Main inventory management interface
- DBConnection.java: Database connection configuration

## Security Notes

- Database passwords should be changed before deployment
- Consider implementing password encryption
- Implement proper input validation for all fields
