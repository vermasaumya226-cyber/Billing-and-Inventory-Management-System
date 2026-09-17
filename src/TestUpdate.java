import dao.ProductDAO;

public class TestUpdate {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();

        // Product ID 1 (Laptop) ka price = 60000.0 aur quantity = 15 update kar rahe hain
        boolean isUpdated = productDAO.updateProduct(1, 60000.0, 15);

        if (isUpdated) {
            System.out.println("Product updated successfully!");
        } else {
            System.out.println("Failed to update product or Product ID not found.");
        }
    }
}
