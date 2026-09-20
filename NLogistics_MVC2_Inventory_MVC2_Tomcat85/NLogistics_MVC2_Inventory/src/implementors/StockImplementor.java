package implementors;

import db_config.GetConnection;
import model.Stock;
import operations.StockOperations;
import java.sql.*;
import java.util.*;

public class StockImplementor implements StockOperations {
	private Stock map(ResultSet r) throws SQLException {
		Stock s = new Stock();
		s.setStockId(r.getInt("stock_id"));
		s.setCompanyId(r.getInt("company_id"));
		s.setProductId(r.getInt("product_id"));
		s.setCompanyName(r.getString("company_name"));
		s.setProductName(r.getString("product_name"));
		s.setWarehouseLocation(r.getString("warehouse_location"));
		s.setQuantityOnHand(r.getDouble("quantity_on_hand"));
		s.setBatchNo(r.getString("batch_no"));
		s.setExpiryDate(r.getString("expiry_date"));
		s.setLastUpdated(r.getString("last_updated"));
		return s;
	}

	public List<Stock> getAll() {
		List<Stock> x = new ArrayList<Stock>();
		String q = "SELECT s.*,c.company_name,p.product_name FROM stock s JOIN companies c ON s.company_id=c.company_id JOIN products p ON s.product_id=p.product_id ORDER BY s.stock_id DESC";
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

	public Stock getById(int id) {
		String q = "SELECT s.*,c.company_name,p.product_name FROM stock s JOIN companies c ON s.company_id=c.company_id JOIN products p ON s.product_id=p.product_id WHERE s.stock_id=?";
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

	public boolean save(Stock s) {
		String q = "INSERT INTO stock(company_id,product_id,warehouse_location,quantity_on_hand,batch_no,expiry_date) VALUES(?,?,?,?,?,?)";
		try (Connection c = GetConnection.getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
			ps.setInt(1, s.getCompanyId());
			ps.setInt(2, s.getProductId());
			ps.setString(3, s.getWarehouseLocation());
			ps.setDouble(4, s.getQuantityOnHand());
			ps.setString(5, s.getBatchNo());
			if (s.getExpiryDate() == null || s.getExpiryDate().trim().isEmpty())
				ps.setNull(6, Types.DATE);
			else
				ps.setDate(6, java.sql.Date.valueOf(s.getExpiryDate()));
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(Stock s) {
		String q = "UPDATE stock SET company_id=?,product_id=?,warehouse_location=?,quantity_on_hand=?,batch_no=?,expiry_date=? WHERE stock_id=?";
		try (Connection c = GetConnection.getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
			ps.setInt(1, s.getCompanyId());
			ps.setInt(2, s.getProductId());
			ps.setString(3, s.getWarehouseLocation());
			ps.setDouble(4, s.getQuantityOnHand());
			ps.setString(5, s.getBatchNo());
			if (s.getExpiryDate() == null || s.getExpiryDate().trim().isEmpty())
				ps.setNull(6, Types.DATE);
			else
				ps.setDate(6, java.sql.Date.valueOf(s.getExpiryDate()));
			ps.setInt(7, s.getStockId());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean delete(int id) {
		try (Connection c = GetConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE FROM stock WHERE stock_id=?")) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
