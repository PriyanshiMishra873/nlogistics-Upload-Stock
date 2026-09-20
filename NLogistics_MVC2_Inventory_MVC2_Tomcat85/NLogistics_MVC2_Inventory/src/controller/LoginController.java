package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {

private static final long serialVersionUID = 1L;

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    String username = req.getParameter("username");

    /*
     * Browser has already checked the credentials
     * against localStorage.
     *
     * Now create the server-side session.
     */
    if (username != null && !username.trim().isEmpty()) {

        HttpSession session = req.getSession(true);

        session.setAttribute("loggedIn", true);
        session.setAttribute("username", username);

        res.sendRedirect("DashboardController");

    } else {

        res.sendRedirect("login.jsp");

    }
}

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    res.sendRedirect("login.jsp");

}

}