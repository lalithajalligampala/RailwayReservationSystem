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

public class ViewBookingsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {

            Connection connection =
                    DBConnection.getConnection();

            String sql =
                    "SELECT b.pnr, " +
                    "p.passenger_name, " +
                    "t.train_number, " +
                    "t.train_name, " +
                    "b.journey_date, " +
                    "b.seat_number, " +
                    "b.travel_class, " +
                    "b.fare, " +
                    "b.booking_status " +
                    "FROM bookings b " +
                    "JOIN passengers p " +
                    "ON b.passenger_id = p.passenger_id " +
                    "JOIN trains t " +
                    "ON b.train_id = t.train_id " +
                    "ORDER BY b.booking_id DESC";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>All Bookings</title>");

            out.println(
                "<link rel='stylesheet' type='text/css' " +
                "href='/RailwayReservationSystem/css/style.css'>"
            );

            out.println("</head>");

            out.println("<body>");

            /* HEADER */

            out.println("<header>");
            out.println("<h1>Railway Reservation System</h1>");
            out.println("<p>All Booking Records</p>");
            out.println("</header>");

            /* MAIN CONTAINER */

            out.println("<div class='container'>");

            out.println("<div class='section'>");

            out.println("<h2>All Bookings</h2>");

            out.println("<p>");
            out.println(
                "The following table displays all passenger " +
                "booking records in the system."
            );
            out.println("</p>");

            /* TABLE CONTAINER */

            out.println("<div class='train-table-container'>");

            out.println("<table class='train-table'>");

            out.println("<tr>");

            out.println("<th>PNR</th>");
            out.println("<th>Passenger</th>");
            out.println("<th>Train Number</th>");
            out.println("<th>Train Name</th>");
            out.println("<th>Journey Date</th>");
            out.println("<th>Seat</th>");
            out.println("<th>Class</th>");
            out.println("<th>Fare</th>");
            out.println("<th>Status</th>");

            out.println("</tr>");

            while (resultSet.next()) {

                out.println("<tr>");

                out.println("<td>"
                        + resultSet.getLong("pnr")
                        + "</td>");

                out.println("<td>"
                        + resultSet.getString("passenger_name")
                        + "</td>");

                out.println("<td>"
                        + resultSet.getInt("train_number")
                        + "</td>");

                out.println("<td>"
                        + resultSet.getString("train_name")
                        + "</td>");

                out.println("<td>"
                        + resultSet.getDate("journey_date")
                        + "</td>");

                out.println("<td>"
                        + resultSet.getInt("seat_number")
                        + "</td>");

                out.println("<td>"
                        + resultSet.getString("travel_class")
                        + "</td>");

                out.println("<td>Rs. "
                        + resultSet.getDouble("fare")
                        + "</td>");

                out.println("<td>"
                        + resultSet.getString("booking_status")
                        + "</td>");

                out.println("</tr>");
            }

            out.println("</table>");

            out.println("</div>");

            out.println("</div>");

            /* INFORMATION */

            out.println("<div class='section'>");

            out.println("<h2>Booking Management</h2>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Database Operation:</strong> " +
                "Booking records are retrieved using SQL JOIN " +
                "operations between bookings, passengers and trains."
            );

            out.println("</div>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Sorting:</strong> " +
                "Bookings are displayed in descending order " +
                "of booking ID."
            );

            out.println("</div>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Admin Function:</strong> " +
                "View all passenger booking records."
            );

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
                "<a class='back-link' href='dashboard'>" +
                "Go to Dashboard" +
                "</a>"
            );

            out.println("</div>");

            /* FOOTER */

            out.println("<footer>");

            out.println(
                "Railway Reservation System | " +
                "ADSA Course Based Project"
            );

            out.println("</footer>");

            out.println("</body>");

            out.println("</html>");

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Error</title>");

            out.println(
                "<link rel='stylesheet' type='text/css' " +
                "href='/RailwayReservationSystem/css/style.css'>"
            );

            out.println("</head>");

            out.println("<body>");

            out.println("<header>");
            out.println("<h1>Railway Reservation System</h1>");
            out.println("<p>Error</p>");
            out.println("</header>");

            out.println("<div class='container'>");

            out.println("<div class='section'>");

            out.println("<h2>Error Loading Bookings</h2>");

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