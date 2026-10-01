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

public class AddTrainServlet extends HttpServlet {

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

        out.println("<title>Add Train</title>");

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
            "<h1>&#128646; Railway Reservation System</h1>"
        );

        out.println(
            "<p>Add New Train</p>"
        );

        out.println("</header>");


        /*
         * MAIN CONTAINER
         */

        out.println("<div class='container'>");

        out.println("<div class='section'>");

        out.println(
            "<h2>Train Registration</h2>"
        );


        try {

            /*
             * Read form values
             */

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
             * INSERT QUERY
             */

            String sql =
                    "INSERT INTO trains "
                    + "(train_number, train_name, source, destination, "
                    + "departure_time, arrival_time, total_seats, "
                    + "available_seats, fare) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";


            Connection connection =
                    DBConnection.getConnection();


            PreparedStatement statement =
                    connection.prepareStatement(sql);


            statement.setInt(1, trainNumber);

            statement.setString(2, trainName);

            statement.setString(3, source);

            statement.setString(4, destination);

            statement.setString(5, departureTime);

            statement.setString(6, arrivalTime);

            statement.setInt(7, totalSeats);


            /*
             * Initially all seats are available
             */

            statement.setInt(8, totalSeats);

            statement.setDouble(9, fare);


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
                    "<h2>Train Added Successfully!</h2>"
                );

                out.println(
                    "<p>" +
                    "The new train has been added " +
                    "to the railway system." +
                    "</p>"
                );

                out.println("</div>");


                /*
                 * TRAIN DETAILS
                 */

                out.println(
                    "<div class='train-details'>"
                );

                out.println(
                    "<h3>Train Details</h3>"
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



            } else {

                /*
                 * FAILED INSERT
                 */

                out.println(
                    "<div class='error-box'>"
                );

                out.println(
                    "<h2>Failed to Add Train</h2>"
                );

                out.println(
                    "<p>" +
                    "The train could not be added." +
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
                "<h2>Error Adding Train</h2>"
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
            "Online Train Booking and Reservation"
        );

        out.println("</footer>");


        out.println("</body>");

        out.println("</html>");
    }
}