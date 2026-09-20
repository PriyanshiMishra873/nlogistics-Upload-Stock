package operations;
import java.util.List;
import model.Stock;
public interface StockOperations {
    List<Stock> getAll(); Stock getById(int id); boolean save(Stock s); boolean update(Stock s); boolean delete(int id);
}
