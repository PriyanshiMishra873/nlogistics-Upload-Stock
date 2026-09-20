package operations;
import java.util.List;
import model.InventoryLedger;
public interface InventoryLedgerOperations {
    List<InventoryLedger> getAll(); InventoryLedger getById(int id); boolean save(InventoryLedger l); boolean update(InventoryLedger l); boolean delete(int id);
}
