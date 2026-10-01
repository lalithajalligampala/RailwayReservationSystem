package com.railway.servlet;

import com.railway.dao.DBConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateTrainServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        /*
         * HTML START
         */

        out.println("<!DOCTYPE html>");
        out.println("<html>");

        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println("<title>Update Train</title>");

        out.println(
            "<link rel='stylesheet' type='text/css' " +
            "href='/RailwayReservationSystem/css/style.css'>"
        );

        /*
         * Page-specific styling
         */

        out.println("<style>");

        out.println(".success-box {");
        out.println("background-color: #eef9ee;");
        out.println("border-left: 5px solid #008000;");
        out.println("padding: 15px;");
        out.println("margin: 15px 0;");
        out.println("}");

        out.println(".error-box {");
        out.println("background-color: #fff0f0;");
        out.println("border-left: 5px solid #cc0000;");
        out.println("padding: 15px;");
        out.println("margin: 15px 0;");
        out.println("}");

        out.println(".train-details {");
        out.println("background-color: #f8f8f8;");
        out.println("border: 1px solid #ddd;");
        out.println("border-radius: 8px;");
        out.println("padding: 15px;");
        out.println("margin-top: 15px;");
        out.println("}");

        out.println(".detail-row {");
        out.println("padding: 8px;");
        out.println("border-bottom: 1px solid #ddd;");
        out.println("}");

        out.println(".detail-row:last-child {");
        out.println("border-bottom: none;");
        out.println("}");

        out.println("</style>");

        out.println("</head>");

        out.println("<body>");

        /*
         * HEADER
         */

        out.println("<header>");

        out.println(
            "<h1>Railway Reservation System</h1>"
        );

        out.println(
            "<p>Update Train Details</p>"
        );

        out.println("</header>");

        /*
         * MAIN CONTAINER
         */

        out.println("<div class='container'>");

        out.println("<div class='section'>");

        out.println(
            "<h2>Train Update</h2>"
        );

        try {

            int trainNumber =
                    Integer.parseInt(
                        request.getParameter("trainNumber")
                    );

            String trainName =
                    request.getParameter("trainName");

            String source =
                    request.getParameter("source");

            String destination =
                    request.getParameter("destination");

            String departureTime =
                    request.getParameter("departureTime");

            String arrivalTime =
                    request.getParameter("arrivalTime");

            int totalSeats =
                    Integer.parseInt(
                        request.getParameter("totalSeats")
                    );

            double fare =
                    Double.parseDouble(
                        request.getParameter("fare")
                    );

            /*
             * UPDATE QUERY
             */

            String sql =
                    "UPDATE trains SET "
                    + "train_name = ?, "
                    + "source = ?, "
                    + "destination = ?, "
                    + "departure_time = ?, "
                    + "arrival_time = ?, "
                    + "total_seats = ?, "
                    + "available_seats = ?, "
                    + "fare = ? "
                    + "WHERE train_number = ?";

            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, trainName);

            statement.setString(2, source);

            statement.setString(3, destination);

            statement.setString(4, departureTime);

            statement.setString(5, arrivalTime);

            statement.setInt(6, totalSeats);

            /*
             * Set available seats equal
             * to the new total seats.
             */

            statement.setInt(7, totalSeats);

            statement.setDouble(8, fare);

            statement.setInt(9, trainNumber);

            int rows =
                    statement.executeUpdate();


            /*
             * SUCCESS
             */

            if (rows > 0) {

                out.println(
                    "<div class='success-box'>"
                );

                out.println(
                    "<h2>Train Updated Successfully!</h2>"
                );

                out.println(
                    "<p>" +
                    "The train details have been " +
                    "updated successfully." +
                    "</p>"
                );

                out.println("</div>");


                /*
                 * UPDATED TRAIN DETAILS
                 */

                out.println(
                    "<div class='train-details'>"
                );

                out.println(
                    "<h3>Updated Train Details</h3>"
                );

                out.println(
                    "<div class='detail-row'>" +
                    "<b>Train Number:</b> " +
                    trainNumber +
                    "</div>"
                );

                out.println(
                    "<div class='detail-row'>" +
                    "<b>Train Name:</b> " +
                    trainName +
                    "</div>"
                );

                out.println(
                    "<div class='detail-row'>" +
                    "<b>Source:</b> " +
                    source +
                    "</div>"
                );

                out.println(
                    "<div class='detail-row'>" +
                    "<b>Destination:</b> " +
                    destination +
                    "</div>"
                );

                out.println(
                    "<div class='detail-row'>" +
                    "<b>Departure Time:</b> " +
                    departureTime +
                    "</div>"
                );

                out.println(
                    "<div class='detail-row'>" +
                    "<b>Arrival Time:</b> " +
                    arrivalTime +
                    "</div>"
                );

                out.println(
                    "<div class='detail-row'>" +
                    "<b>Total Seats:</b> " +
                    totalSeats +
                    "</div>"
                );

                out.println(
                    "<div class='detail-row'>" +
                    "<b>Available Seats:</b> " +
                    totalSeats +
                    "</div>"
                );

                out.println(
                    "<div class='detail-row'>" +
                    "<b>Fare:</b> Rs. " +
                    fare +
                    "</div>"
                );

                out.println("</div>");


                /*
                 * NOTE ABOUT AVAILABLE SEATS
                 */

                out.println(
                    "<div class='algorithm'>"
                );

                out.println(
                    "<strong>Note:</strong> " +
                    "Available seats are reset to the " +
                    "new total seat count when the train " +
                    "is updated."
                );

                out.println("</div>");


            } else {

                /*
                 * TRAIN NOT FOUND
                 */

                out.println(
                    "<div class='error-box'>"
                );

                out.println(
                    "<h2>Train Not Found</h2>"
                );

                out.println(
                    "<p>" +
                    "No train exists with Train Number " +
                    trainNumber +
                    "." +
                    "</p>"
                );

                out.println("</div>");
            }


            /*
             * CLOSE DATABASE RESOURCES
             */

            statement.close();

            connection.close();


        } catch (NumberFormatException e) {

            out.println(
                "<div class='error-box'>"
            );

            out.println(
                "<h2>Invalid Input</h2>"
            );

            out.println(
                "<p>" +
                "Please enter valid numeric values " +
                "for Train Number, Total Seats and Fare." +
                "</p>"
            );

            out.println("</div>");


        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                "<div class='error-box'>"
            );

            out.println(
                "<h2>Error Updating Train</h2>"
            );

            out.println(
                "<p>" +
                e.getMessage() +
                "</p>"
            );

            out.println("</div>");
        }

        out.println("</div>");


        /*
         * NAVIGATION
         */

        out.println("<div class='section'>");

        out.println(
            "<a class='back-link' " +
            "href='admin.html'>" +
            "Back to Admin Panel" +
            "</a>"
        );

        out.println("<br><br>");

        out.println(
            "<a class='back-link' " +
            "href='viewTrains'>" +
            "View All Trains" +
            "</a>"
        );

        out.println("<br><br>");

        out.println(
            "<a class='back-link' " +
            "href='index.html'>" +
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
            "ADSA Course Based Project"
        );

        out.println("</footer>");

        out.println("</body>");

        out.println("</html>");
    }
}