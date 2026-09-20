package implementors;

import db_config.GetConnection;
import model.InventoryLedger;
import operations.InventoryLedgerOperations;
import java.sql.*;
import java.util.*;

public class InventoryLedgerImplementor implements InventoryLedgerOperations {
	private InventoryLedger map(ResultSet r) throws SQLException {
		InventoryLedger x = new InventoryLedger();
		x.setLedgerId(r.getInt("ledger_id"));
		x.setProductId(r.getInt("product_id"));
		x.setProductName(r.getString("product_name"));
		x.setTransactionType(r.getString("transaction_type"));
		x.setQuantity(r.getDouble("quantity"));
		x.setUnitCostAtTxn(r.getDouble("unit_cost_at_txn"));
		x.setReferenceType(r.getString("reference_type"));
		x.setReferenceId(r.getString("reference_id"));
		x.setTxnDate(r.getString("txn_date"));
		return x;
	}

	public List<InventoryLedger> getAll() {
		List<InventoryLedger> x = new ArrayList<InventoryLedger>();
		String q = "SELECT l.*,p.product_name FROM inventory_ledger l JOIN products p ON l.product_id=p.product_id ORDER BY l.ledger_id DESC";
		try (Connection c = GetConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(q);
				ResultSet r = ps.executeQuery()) {
			while (r.next())
				x.add(map(r));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return x;
	}

	public InventoryLedger getById(int id) {
		String q = "SELECT l.*,p.product_name FROM inventory_ledger l JOIN products p ON l.product_id=p.product_id WHERE l.ledger_id=?";
		try (Connection c = GetConnection.getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
			ps.setInt(1, id);
			ResultSet r = ps.executeQuery();
			if (r.next())
				return map(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean save(InventoryLedger x) {
		String q = "INSERT INTO inventory_ledger(product_id,transaction_type,quantity,unit_cost_at_txn,reference_type,reference_id,txn_date) VALUES(?,?,?,?,?,?,?)";
		try (Connection c = GetConnection.getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
			ps.setInt(1, x.getProductId());
			ps.setString(2, x.getTransactionType());
			ps.setDouble(3, x.getQuantity());
			ps.setDouble(4, x.getUnitCostAtTxn());
			ps.setString(5, x.getReferenceType());
			ps.setString(6, x.getReferenceId());
			if (x.getTxnDate() == null || x.getTxnDate().isEmpty())
				ps.setDate(7, new java.sql.Date(System.currentTimeMillis()));
			else
				ps.setDate(7, java.sql.Date.valueOf(x.getTxnDate()));
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(InventoryLedger x) {
		String q = "UPDATE inventory_ledger SET product_id=?,transaction_type=?,quantity=?,unit_cost_at_txn=?,reference_type=?,reference_id=?,txn_date=? WHERE ledger_id=?";
		try (Connection c = GetConnection.getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
			ps.setInt(1, x.getProductId());
			ps.setString(2, x.getTransactionType());
			ps.setDouble(3, x.getQuantity());
			ps.setDouble(4, x.getUnitCostAtTxn());
			ps.setString(5, x.getReferenceType());
			ps.setString(6, x.getReferenceId());
			ps.setDate(7, java.sql.Date.valueOf(x.getTxnDate()));
			ps.setInt(8, x.getLedgerId());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean delete(int id) {
		try (Connection c = GetConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE FROM inventory_ledger WHERE ledger_id=?")) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
