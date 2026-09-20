package operations;
import java.util.List;
import model.Product;
public interface ProductOperations {
    List<Product> getAll(); Product getById(int id); boolean save(Product p); boolean update(Product p); boolean delete(int id);
}
