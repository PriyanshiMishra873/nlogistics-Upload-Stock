package controller;

import db_config.GetConnection;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/DashboardController")
public class DashboardController extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try (Connection c = GetConnection.getConnection()) {
			req.setAttribute("products", count(c, "SELECT COUNT(*) FROM products"));
			req.setAttribute("stock", sum(c, "SELECT COALESCE(SUM(quantity_on_hand),0) FROM stock"));
			req.setAttribute("sales", sum(c, "SELECT COALESCE(SUM(sale_amount),0) FROM sales_transactions"));
			req.setAttribute("ledger", count(c, "SELECT COUNT(*) FROM inventory_ledger"));
		} catch (Exception e) {
			req.setAttribute("dbError", e.getMessage());
		}
		req.getRequestDispatcher("/index.jsp").forward(req, res);
	}

	private int count(Connection c, String q) throws SQLException {
		try (PreparedStatement p = c.prepareStatement(q); ResultSet r = p.executeQuery()) {
			r.next();
			return r.getInt(1);
		}
	}

	private double sum(Connection c, String q) throws SQLException {
		try (PreparedStatement p = c.prepareStatement(q); ResultSet r = p.executeQuery()) {
			r.next();
			return r.getDouble(1);
		}
	}
}
