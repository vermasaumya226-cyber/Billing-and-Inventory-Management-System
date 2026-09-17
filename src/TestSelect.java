import dao.ProductDAO;
import model.Product;
import java.util.List;

public class TestSelect {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();

        System.out.println("--- Products in Database ---");
        for (Product p : products) {
            System.out.println("ID: " + p.getId() +
                    " | Name: " + p.getName() +
                    " | Price: " + p.getPrice() +
                    " | Quantity: " + p.getQuantity());
        }
    }
}
