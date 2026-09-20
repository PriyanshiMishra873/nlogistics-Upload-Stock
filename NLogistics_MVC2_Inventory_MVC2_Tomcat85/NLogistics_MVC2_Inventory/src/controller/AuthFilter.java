package controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

@Override
public void init(FilterConfig filterConfig)
        throws ServletException {
}

@Override
public void doFilter(ServletRequest request,
                      ServletResponse response,
                      FilterChain chain)
        throws IOException, ServletException {

    HttpServletRequest req =
            (HttpServletRequest) request;

    HttpServletResponse res =
            (HttpServletResponse) response;


    String uri = req.getRequestURI();

    String contextPath =
            req.getContextPath();

    String page =
            uri.substring(contextPath.length());


    /*
     * Public pages
     */
    boolean publicPage =
            page.equals("/login.jsp") ||
            page.equals("/register.jsp") ||
            page.equals("/LoginController") ||
            page.equals("/LogoutController");


    /*
     * Allow public pages
     */
    if (publicPage) {

        chain.doFilter(request, response);

        return;
    }


    /*
     * Check Java session
     */
    HttpSession session =
            req.getSession(false);


    boolean loggedIn =
            session != null &&
            session.getAttribute("loggedIn") != null &&
            Boolean.TRUE.equals(
                session.getAttribute("loggedIn")
            );


    /*
     * No session
     */
    if (!loggedIn) {

        res.sendRedirect(
            contextPath + "/login.jsp"
        );

        return;
    }


    /*
     * Session exists
     */
    chain.doFilter(request, response);
}


@Override
public void destroy() {
}

}