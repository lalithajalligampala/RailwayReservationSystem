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

public class DashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        int totalTrains = 0;
        int totalBookings = 0;
        int confirmedBookings = 0;
        int cancelledBookings = 0;
        int waitingList = 0;
        int availableSeats = 0;
        int totalPassengers = 0;
        double totalRevenue = 0;

        try {

            Connection connection =
                    DBConnection.getConnection();

            // Total trains
            String trainSQL =
                    "SELECT COUNT(*) FROM trains";

            PreparedStatement trainStatement =
                    connection.prepareStatement(trainSQL);

            ResultSet trainResult =
                    trainStatement.executeQuery();

            if (trainResult.next()) {
                totalTrains = trainResult.getInt(1);
            }

            // Total bookings
            String bookingSQL =
                    "SELECT COUNT(*) FROM bookings";

            PreparedStatement bookingStatement =
                    connection.prepareStatement(bookingSQL);

            ResultSet bookingResult =
                    bookingStatement.executeQuery();

            if (bookingResult.next()) {
                totalBookings = bookingResult.getInt(1);
            }

            // Confirmed bookings
            String confirmedSQL =
                    "SELECT COUNT(*) FROM bookings " +
                    "WHERE booking_status = 'CONFIRMED'";

            PreparedStatement confirmedStatement =
                    connection.prepareStatement(confirmedSQL);

            ResultSet confirmedResult =
                    confirmedStatement.executeQuery();

            if (confirmedResult.next()) {
                confirmedBookings =
                        confirmedResult.getInt(1);
            }

            // Cancelled bookings
            String cancelledSQL =
                    "SELECT COUNT(*) FROM bookings " +
                    "WHERE booking_status = 'CANCELLED'";

            PreparedStatement cancelledStatement =
                    connection.prepareStatement(cancelledSQL);

            ResultSet cancelledResult =
                    cancelledStatement.executeQuery();

            if (cancelledResult.next()) {
                cancelledBookings =
                        cancelledResult.getInt(1);
            }

            // Waiting list
            String waitingSQL =
                    "SELECT COUNT(*) FROM waiting_list " +
                    "WHERE booking_status = 'WAITING'";

            PreparedStatement waitingStatement =
                    connection.prepareStatement(waitingSQL);

            ResultSet waitingResult =
                    waitingStatement.executeQuery();

            if (waitingResult.next()) {
                waitingList =
                        waitingResult.getInt(1);
            }

            // Available seats
            String seatsSQL =
                    "SELECT COALESCE(SUM(available_seats), 0) " +
                    "FROM trains";

            PreparedStatement seatsStatement =
                    connection.prepareStatement(seatsSQL);

            ResultSet seatsResult =
                    seatsStatement.executeQuery();

            if (seatsResult.next()) {
                availableSeats =
                        seatsResult.getInt(1);
            }

            // Total passengers
            String passengerSQL =
                    "SELECT COUNT(*) FROM passengers";

            PreparedStatement passengerStatement =
                    connection.prepareStatement(passengerSQL);

            ResultSet passengerResult =
                    passengerStatement.executeQuery();

            if (passengerResult.next()) {
                totalPassengers =
                        passengerResult.getInt(1);
            }

            // Total revenue from confirmed bookings
            String revenueSQL =
                    "SELECT COALESCE(SUM(fare), 0) " +
                    "FROM bookings " +
                    "WHERE booking_status = 'CONFIRMED'";

            PreparedStatement revenueStatement =
                    connection.prepareStatement(revenueSQL);

            ResultSet revenueResult =
                    revenueStatement.executeQuery();

            if (revenueResult.next()) {
                totalRevenue =
                        revenueResult.getDouble(1);
            }

            /*
             * DASHBOARD PAGE
             */

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<title>Railway Dashboard</title>");

            out.println(
                "<link rel='stylesheet' type='text/css' " +
                "href='/RailwayReservationSystem/css/style.css'>"
            );

            out.println("</head>");

            out.println("<body>");

            /*
             * HEADER
             */

            out.println("<header>");

            out.println(
                "<h1>&#128646; Railway Reservation System</h1>"
            );

            out.println(
                "<p>System Dashboard</p>"
            );

            out.println("</header>");

            /*
             * MAIN CONTAINER
             */

            out.println("<div class='container'>");

            out.println("<div class='section'>");

            out.println(
                "<h2>Railway Reservation Dashboard</h2>"
            );

            /*
             * DASHBOARD TABLE
             */

            out.println("<div class='train-table-container'>");

            out.println("<table class='train-table'>");

            out.println("<tr>");

            out.println("<th>Category</th>");

            out.println("<th>Count / Value</th>");

            out.println("</tr>");

            out.println("<tr>");

            out.println("<td>Total Trains</td>");

            out.println(
                "<td>" + totalTrains + "</td>"
            );

            out.println("</tr>");

            out.println("<tr>");

            out.println("<td>Total Passengers</td>");

            out.println(
                "<td>" + totalPassengers + "</td>"
            );

            out.println("</tr>");

            out.println("<tr>");

            out.println("<td>Total Bookings</td>");

            out.println(
                "<td>" + totalBookings + "</td>"
            );

            out.println("</tr>");

            out.println("<tr>");

            out.println("<td>Confirmed Bookings</td>");

            out.println(
                "<td>" + confirmedBookings + "</td>"
            );

            out.println("</tr>");

            out.println("<tr>");

            out.println("<td>Cancelled Bookings</td>");

            out.println(
                "<td>" + cancelledBookings + "</td>"
            );

            out.println("</tr>");

            out.println("<tr>");

            out.println("<td>Waiting List</td>");

            out.println(
                "<td>" + waitingList + "</td>"
            );

            out.println("</tr>");

            out.println("<tr>");

            out.println("<td>Available Seats</td>");

            out.println(
                "<td>" + availableSeats + "</td>"
            );

            out.println("</tr>");

            out.println("<tr>");

            out.println("<td>Total Revenue</td>");

            out.println(
                "<td>Rs. " + totalRevenue + "</td>"
            );

            out.println("</tr>");

            out.println("</table>");

            out.println("</div>");

            out.println("</div>");

            /*
             * LINKS
             */

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

            /*
             * FOOTER
             */

            out.println("<footer>");

            out.println(
                "Railway Reservation System | " +
                "Online Train Booking and Reservation"
            );

            out.println("</footer>");

            out.println("</body>");

            out.println("</html>");

            connection.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<!DOCTYPE html>");

            out.println("<html>");

            out.println("<head>");

            out.println("<title>Dashboard Error</title>");

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
                "<p>Dashboard Error</p>"
            );

            out.println("</header>");

            out.println("<div class='container'>");

            out.println("<div class='section'>");

            out.println("<h2>Dashboard Error</h2>");

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