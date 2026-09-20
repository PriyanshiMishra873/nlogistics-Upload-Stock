package implementors;

import db_config.GetConnection;
import model.SalesTransaction;
import operations.SalesTransactionOperations;
import java.sql.*;
import java.util.*;

public class SalesTransactionImplementor implements SalesTransactionOperations {
	private SalesTransaction map(ResultSet r) throws SQLException {
		SalesTransaction x = new SalesTransaction();
		x.setTransactionId(r.getInt("transaction_id"));
		x.setProductId(r.getInt("product_id"));
		x.setCustomerId(r.getInt("customer_id"));
		int sh = r.getInt("shipment_id");
		x.setShipmentId(r.wasNull() ? null : Integer.valueOf(sh));
		x.setProductName(r.getString("product_name"));
		x.setCustomerName(r.getString("customer_name"));
		x.setQuantitySold(r.getDouble("quantity_sold"));
		x.setSalePriceSnapshot(r.getDouble("sale_price_snapshot"));
		x.setSaleAmount(r.getDouble("sale_amount"));
		x.setSaleDate(r.getString("sale_date"));
		return x;
	}

	public List<SalesTransaction> getAll() {
		List<SalesTransaction> x = new ArrayList<SalesTransaction>();
		String q = "SELECT s.*,p.product_name,c.customer_name FROM sales_transactions s JOIN products p ON s.product_id=p.product_id JOIN customers c ON s.customer_id=c.customer_id ORDER BY s.transaction_id DESC";
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

	public SalesTransaction getById(int id) {
		String q = "SELECT s.*,p.product_name,c.customer_name FROM sales_transactions s JOIN products p ON s.product_id=p.product_id JOIN customers c ON s.customer_id=c.customer_id WHERE s.transaction_id=?";
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

	public boolean save(SalesTransaction x) {
		String q = "INSERT INTO sales_transactions(product_id,customer_id,shipment_id,quantity_sold,sale_price_snapshot,sale_date) VALUES(?,?,?,?,?,?)";
		try (Connection c = GetConnection.getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
			ps.setInt(1, x.getProductId());
			ps.setInt(2, x.getCustomerId());
			if (x.getShipmentId() == null)
				ps.setNull(3, Types.INTEGER);
			else
				ps.setInt(3, x.getShipmentId());
			ps.setDouble(4, x.getQuantitySold());
			ps.setDouble(5, x.getSalePriceSnapshot());
			ps.setDate(6, java.sql.Date.valueOf(x.getSaleDate()));
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(SalesTransaction x) {
		String q = "UPDATE sales_transactions SET product_id=?,customer_id=?,shipment_id=?,quantity_sold=?,sale_price_snapshot=?,sale_date=? WHERE transaction_id=?";
		try (Connection c = GetConnection.getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
			ps.setInt(1, x.getProductId());
			ps.setInt(2, x.getCustomerId());
			if (x.getShipmentId() == null)
				ps.setNull(3, Types.INTEGER);
			else
				ps.setInt(3, x.getShipmentId());
			ps.setDouble(4, x.getQuantitySold());
			ps.setDouble(5, x.getSalePriceSnapshot());
			ps.setDate(6, java.sql.Date.valueOf(x.getSaleDate()));
			ps.setInt(7, x.getTransactionId());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean delete(int id) {
		try (Connection c = GetConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE FROM sales_transactions WHERE transaction_id=?")) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
