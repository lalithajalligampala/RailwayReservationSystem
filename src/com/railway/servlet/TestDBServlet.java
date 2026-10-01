package com.railway.servlet;

import com.railway.dao.DBConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestDBServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        Connection connection = DBConnection.getConnection();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Database Test</title>");
        out.println("</head>");

        out.println("<body>");

        if (connection != null) {

            out.println("<h1>Database Connected Successfully!</h1>");

            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            out.println("<h1>Database Connection Failed!</h1>");
        }

        out.println("</body>");
        out.println("</html>");
    }
}