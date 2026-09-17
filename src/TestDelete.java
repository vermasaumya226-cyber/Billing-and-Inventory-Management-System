import dao.ProductDAO;

public class TestDelete {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();

        // Testing Delete operation for Product ID = 1
        boolean isDeleted = productDAO.deleteProduct(1);

        if (isDeleted) {
            System.out.println("Product deleted successfully from database!");
        } else {
            System.out.println("Failed to delete product or Product ID not found.");
        }
    }
}
