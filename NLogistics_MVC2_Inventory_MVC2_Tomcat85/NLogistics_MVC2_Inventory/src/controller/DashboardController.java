package controller;

import db_config.GetConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/DashboardController")
public class DashboardController extends HttpServlet {

private static final long serialVersionUID = 1L;

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    /*
     * LOGIN CHECK
     */
    HttpSession session = req.getSession(false);

    if (session == null || session.getAttribute("loggedIn") == null) {
        res.sendRedirect("login.jsp");
        return;
    }

    /*
     * EXISTING DATABASE CODE
     */
    try (Connection c = GetConnection.getConnection()) {

        req.setAttribute(
                "products",
                count(c, "SELECT COUNT(*) FROM products")
        );

        req.setAttribute(
                "stock",
                sum(c, "SELECT COALESCE(SUM(quantity_on_hand), 0) FROM stock")
        );

        req.setAttribute(
                "sales",
                sum(c, "SELECT COALESCE(SUM(sale_amount), 0) FROM sales_transactions")
        );

        req.setAttribute(
                "ledger",
                count(c, "SELECT COUNT(*) FROM inventory_ledger")
        );

    } catch (Exception e) {
        req.setAttribute("dbError", e.getMessage());
    }

    /*
     * EXISTING JSP
     */
    req.getRequestDispatcher("/index.jsp").forward(req, res);
}

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    doGet(req, res);
}

private int count(Connection c, String query) throws SQLException {

    try (PreparedStatement p = c.prepareStatement(query);
         ResultSet r = p.executeQuery()) {

        if (r.next()) {
            return r.getInt(1);
        }

        return 0;
    }
}

private double sum(Connection c, String query) throws SQLException {

    try (PreparedStatement p = c.prepareStatement(query);
         ResultSet r = p.executeQuery()) {

        if (r.next()) {
            return r.getDouble(1);
        }

        return 0.0;
    }
}

}