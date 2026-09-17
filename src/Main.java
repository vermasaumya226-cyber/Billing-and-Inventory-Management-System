import dao.ProductDAO;
import model.Product;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=================================");
            System.out.println("   BILLING INVENTORY SYSTEM");
            System.out.println("=================================");
            System.out.println("1. Add New Product");
            System.out.println("2. View All Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Product Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Product Price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter Quantity: ");
                    int quantity = scanner.nextInt();

                    Product newProduct = new Product(name, price, quantity);
                    if (productDAO.addProduct(newProduct)) {
                        System.out.println("✅ Product added successfully!");
                    } else {
                        System.out.println("❌ Failed to add product.");
                    }
                    break;

                case 2:
                    List<Product> products = productDAO.getAllProducts();
                    System.out.println("\n--- Product List ---");
                    if (products.isEmpty()) {
                        System.out.println("No products found in database.");
                    } else {
                        for (Product p : products) {
                            System.out.println("ID: " + p.getId() +
                                    " | Name: " + p.getName() +
                                    " | Price: ₹" + p.getPrice() +
                                    " | Quantity: " + p.getQuantity());
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter Product ID to Update: ");
                    int updateId = scanner.nextInt();
                    System.out.print("Enter New Price: ");
                    double newPrice = scanner.nextDouble();
                    System.out.print("Enter New Quantity: ");
                    int newQty = scanner.nextInt();

                    if (productDAO.updateProduct(updateId, newPrice, newQty)) {
                        System.out.println("✅ Product updated successfully!");
                    } else {
                        System.out.println("❌ Update failed or ID not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Product ID to Delete: ");
                    int deleteId = scanner.nextInt();

                    if (productDAO.deleteProduct(deleteId)) {
                        System.out.println("✅ Product deleted successfully!");
                    } else {
                        System.out.println("❌ Delete failed or ID not found.");
                    }
                    break;

                case 5:
                    System.out.println("Thank you for using Billing Inventory System. Goodbye!");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice! Please select between 1 and 5.");
            }
        }
    }
}