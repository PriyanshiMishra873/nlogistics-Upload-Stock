package operations;
import java.util.List;
import model.SalesTransaction;
public interface SalesTransactionOperations {
    List<SalesTransaction> getAll(); SalesTransaction getById(int id); boolean save(SalesTransaction s); boolean update(SalesTransaction s); boolean delete(int id);
}
