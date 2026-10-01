package com.railway.servlet;

import com.railway.dao.DBConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewTrainServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {

            Connection connection =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM trains ORDER BY train_number";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<title>All Trains</title>");

            out.println(
                "<link rel='stylesheet' type='text/css' " +
                "href='/RailwayReservationSystem/css/style.css'>"
            );

            out.println("</head>");

            out.println("<body>");

            /* HEADER */

            out.println("<header>");

            out.println(
                "<h1>&#128646; Railway Reservation System</h1>"
            );

            out.println(
                "<p>All Railway Trains</p>"
            );

            out.println("</header>");

            /* MAIN CONTAINER */

            out.println("<div class='container'>");

            out.println("<div class='section'>");

            out.println("<h2>All Trains</h2>");

            out.println(
                "<p>Train records are displayed in " +
                "ascending order of train number."
            );

            /* TABLE */

            out.println("<div class='train-table-container'>");

            out.println("<table class='train-table'>");

            out.println("<tr>");

            out.println("<th>Train Number</th>");
            out.println("<th>Train Name</th>");
            out.println("<th>Source</th>");
            out.println("<th>Destination</th>");
            out.println("<th>Departure</th>");
            out.println("<th>Arrival</th>");
            out.println("<th>Total Seats</th>");
            out.println("<th>Available Seats</th>");
            out.println("<th>Fare</th>");

            out.println("</tr>");

            while (resultSet.next()) {

                out.println("<tr>");

                out.println(
                    "<td>" +
                    resultSet.getInt("train_number") +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    resultSet.getString("train_name") +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    resultSet.getString("source") +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    resultSet.getString("destination") +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    resultSet.getString("departure_time") +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    resultSet.getString("arrival_time") +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    resultSet.getInt("total_seats") +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    resultSet.getInt("available_seats") +
                    "</td>"
                );

                out.println(
                    "<td>Rs. " +
                    resultSet.getDouble("fare") +
                    "</td>"
                );

                out.println("</tr>");
            }

            out.println("</table>");

            out.println("</div>");

            out.println("</div>");

            /* LINKS */

            out.println(
                "<a class='back-link' href='admin.html'>" +
                "Back to Admin Panel" +
                "</a>"
            );

            out.println("<br>");

            out.println(
                "<a class='back-link' href='index.html'>" +
                "Back to Home" +
                "</a>"
            );

            out.println("</div>");

            /* FOOTER */

            out.println("<footer>");

            out.println(
                "Railway Reservation System | " +
                "Online Train Booking and Reservation"
            );

            out.println("</footer>");

            out.println("</body>");

            out.println("</html>");

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<!DOCTYPE html>");

            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<title>Error Loading Trains</title>");

            out.println(
                "<link rel='stylesheet' type='text/css' " +
                "href='/RailwayReservationSystem/css/style.css'>"
            );

            out.println("</head>");

            out.println("<body>");

            out.println("<header>");

            out.println(
                "<h1>&#128646; Railway Reservation System</h1>"
            );

            out.println(
                "<p>Train Management</p>"
            );

            out.println("</header>");

            out.println("<div class='container'>");

            out.println("<div class='section'>");

            out.println("<h2>Error Loading Trains</h2>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Error:</strong> " +
                e.getMessage()
            );

            out.println("</div>");

            out.println(
                "<a class='back-link' href='admin.html'>" +
                "Back to Admin Panel" +
                "</a>"
            );

            out.println("</div>");

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");
        }
    }
}