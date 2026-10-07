package com.railway.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/adminLogin")
public class AdminLoginServlet extends HttpServlet {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = System.getenv("RAILWAY_ADMIN_PASSWORD");

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (ADMIN_USERNAME.equals(username)
        && password != null
        && ADMIN_PASSWORD != null
        && ADMIN_PASSWORD.equals(password)) {

            HttpSession session = request.getSession();

            session.setAttribute("adminLoggedIn", true);

            response.sendRedirect("admin.html");

        } else {

            response.setContentType("text/html");

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Admin Login Failed</title>");
            out.println("<link rel='stylesheet' type='text/css' " +
                    "href='/RailwayReservationSystem/css/style.css'>");
            out.println("</head>");

            out.println("<body>");

            out.println("<header>");
            out.println("<h1>Railway Reservation System</h1>");
            out.println("<p>Administrator Login</p>");
            out.println("</header>");

            out.println("<div class='container'>");

            out.println("<div class='section'>");

            out.println("<h2>Login Failed</h2>");

            out.println("<div class='algorithm'>");
            out.println("<b>Invalid username or password.</b>");
            out.println("</div>");

            out.println("<br>");

            out.println("<a class='back-link' href='adminLogin.html'>");
            out.println("Try Again");
            out.println("</a>");

            out.println("</div>");

            out.println("</div>");

            out.println("<footer>");
            out.println("Railway Reservation System | Admin Authentication");
            out.println("</footer>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}