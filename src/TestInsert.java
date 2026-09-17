import dao.ProductDAO;
import model.Product;

public class TestInsert {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();

        // Sample product: Name = "Laptop", Price = 55000.0, Quantity = 10
        Product newProduct = new Product("Laptop", 55000.0, 10);

        boolean isAdded = productDAO.addProduct(newProduct);

        if (isAdded) {
            System.out.println("Product added successfully to database!");
        } else {
            System.out.println("Failed to add product.");
        }
    }
}
