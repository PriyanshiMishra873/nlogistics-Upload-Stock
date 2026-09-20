package operations;
import java.util.List;
import model.StockUploadLog;
public interface StockUploadLogOperations {
    List<StockUploadLog> getAll(); StockUploadLog getById(int id); boolean save(StockUploadLog l); boolean update(StockUploadLog l); boolean delete(int id);
}
