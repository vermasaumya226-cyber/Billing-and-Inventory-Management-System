# Billing-and-Inventory-Management-System

# Electronics Billing & Inventory System

A full-stack desktop application built with **Java (Swing GUI)**, **JDBC**, and **MySQL Database** for retail electronics store inventory management and customer billing.

##  Features

- **Product Inventory Management:** Add, update, view, and delete stock items via GUI.
- **Smart Stock Increment:** Automatically updates quantity when re-adding existing products.
- **Customer Billing Engine:** Calculates flat **10% Auto-Discount** and **18% GST** automatically.
- **Receipt Generation:** Generates clean, itemized receipt text views for fast customer checkout.
- **Order History & Deletion:** View all past customer orders and selectively delete records from MySQL.

##  Tech Stack & Tools

- **Language:** Java (JDK 8 or higher)
- **GUI Framework:** Java Swing (AWT/Swing)
- **Database:** MySQL Server
- **Database Connectivity:** JDBC (Java Database Connectivity)
- **IDE:** IntelliJ IDEA / Eclipse

##  Project Structure
BillingInventorySystem/
│
├── src/
│   ├── database/
│   │   └── DBConnection.java
│   ├── model/
│   │   ├── Product.java
│   │   └── Order.java
│   ├── dao/
│   │   ├── ProductDAO.java
│   │   └── OrderDAO.java
│   ├── BillingUI.java
│   └── Main.java
│
├── README.md
└── statement.md

##  Setup and Installation

### 1. Database Setup
Open MySQL Workbench and run:
```sql
CREATE DATABASE billing_inventory_db;
USE billing_inventory_db;

CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL
);

CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(15) NOT NULL,
    total_amount DOUBLE NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

2. DBConnection Configuration

Update your database password in src/database/DBConnection.java:

```sql
private static final String URL = "jdbc:mysql://localhost:3306/billing_inventory_db";
private static final String USER = "root";
private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";
```

3. Run Application

Run BillingUI.java directly in IntelliJ IDEA or your preferred IDE.

### **Final Checklist Before Submission:**

```sql

1. **GitHub Repository:** Push your Java code along with `README.md` and `statement.md` to GitHub[cite: 1].
2. **Project Report PDF:** PDF report me screenshots attach karke PDF export kar lijiye[cite: 1].
3. **Portal Submission:** GitHub Repo link aur Project Report PDF dono portal par upload kar dein[cite: 1]!
```
