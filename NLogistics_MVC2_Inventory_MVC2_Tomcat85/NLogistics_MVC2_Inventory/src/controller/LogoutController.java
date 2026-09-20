package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogoutController")
public class LogoutController extends HttpServlet {

private static final long serialVersionUID = 1L;

@Override
protected void doGet(HttpServletRequest req,
                      HttpServletResponse res)
        throws ServletException, IOException {

    HttpSession session =
            req.getSession(false);

    if (session != null) {
        session.invalidate();
    }

    /*
     * Prevent browser from caching
     * protected pages.
     */
    res.setHeader("Cache-Control",
                  "no-cache, no-store, must-revalidate");

    res.setHeader("Pragma", "no-cache");

    res.setDateHeader("Expires", 0);

    res.sendRedirect("login.jsp");
}

@Override
protected void doPost(HttpServletRequest req,
                       HttpServletResponse res)
        throws ServletException, IOException {

    doGet(req, res);
}

}