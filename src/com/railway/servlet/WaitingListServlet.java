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

public class WaitingListServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        String passengerName =
                request.getParameter("passengerName");

        int age =
                Integer.parseInt(
                    request.getParameter("age")
                );

        String gender =
                request.getParameter("gender");

        String phone =
                request.getParameter("phone");

        int trainNumber =
                Integer.parseInt(
                    request.getParameter("trainNumber")
                );

        String journeyDate =
                request.getParameter("journeyDate");

        String travelClass =
                request.getParameter("travelClass");

        Connection connection = null;

        try {

            connection = DBConnection.getConnection();

            if (connection == null) {

                showError(
                    out,
                    "Database Connection Failed!",
                    "Unable to connect to the railway reservation database."
                );

                return;
            }

            /*
             * Step 1:
             * Find internal Train ID using Train Number.
             */

            String trainSQL =
                    "SELECT train_id, train_number, train_name " +
                    "FROM trains WHERE train_number = ?";

            PreparedStatement trainStatement =
                    connection.prepareStatement(trainSQL);

            trainStatement.setInt(
                    1,
                    trainNumber
            );

            ResultSet trainResult =
                    trainStatement.executeQuery();

            if (!trainResult.next()) {

                trainResult.close();
                trainStatement.close();

                showError(
                    out,
                    "Train Not Found!",
                    "No train exists with Train Number " +
                    trainNumber + "."
                );

                return;
            }

            int trainId =
                    trainResult.getInt("train_id");

            int actualTrainNumber =
                    trainResult.getInt("train_number");

            String trainName =
                    trainResult.getString("train_name");

            trainResult.close();
            trainStatement.close();


            /*
             * Step 2:
             * Add passenger to passengers table.
             */

            String passengerSQL =
                    "INSERT INTO passengers " +
                    "(passenger_name, age, gender, phone) " +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement passengerStatement =
                    connection.prepareStatement(
                        passengerSQL,
                        PreparedStatement.RETURN_GENERATED_KEYS
                    );

            passengerStatement.setString(
                    1,
                    passengerName
            );

            passengerStatement.setInt(
                    2,
                    age
            );

            passengerStatement.setString(
                    3,
                    gender
            );

            passengerStatement.setString(
                    4,
                    phone
            );

            passengerStatement.executeUpdate();

            ResultSet passengerKeys =
                    passengerStatement.getGeneratedKeys();

            int passengerId = 0;

            if (passengerKeys.next()) {

                passengerId =
                        passengerKeys.getInt(1);
            }


            /*
             * Step 3:
             * Find the next queue position.
             *
             * FIFO:
             * Smaller queue position = earlier request.
             */

            String positionSQL =
                    "SELECT COALESCE(MAX(queue_position), 0) + 1 " +
                    "FROM waiting_list " +
                    "WHERE train_id = ? " +
                    "AND journey_date = ? " +
                    "AND booking_status = 'WAITING'";

            PreparedStatement positionStatement =
                    connection.prepareStatement(
                        positionSQL
                    );

            positionStatement.setInt(
                    1,
                    trainId
            );

            positionStatement.setString(
                    2,
                    journeyDate
            );

            ResultSet positionResult =
                    positionStatement.executeQuery();

            int queuePosition = 1;

            if (positionResult.next()) {

                queuePosition =
                        positionResult.getInt(1);
            }


            /*
             * Step 4:
             * Insert passenger into waiting list.
             */

            String waitingSQL =
                    "INSERT INTO waiting_list " +
                    "(passenger_id, train_id, journey_date, " +
                    "travel_class, queue_position, booking_status) " +
                    "VALUES (?, ?, ?, ?, ?, 'WAITING')";

            PreparedStatement waitingStatement =
                    connection.prepareStatement(
                        waitingSQL
                    );

            waitingStatement.setInt(
                    1,
                    passengerId
            );

            waitingStatement.setInt(
                    2,
                    trainId
            );

            waitingStatement.setString(
                    3,
                    journeyDate
            );

            waitingStatement.setString(
                    4,
                    travelClass
            );

            waitingStatement.setInt(
                    5,
                    queuePosition
            );

            waitingStatement.executeUpdate();


            /*
             * Step 5:
             * Display waiting-list confirmation.
             */

            out.println("<!DOCTYPE html>");

            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<title>Waiting List</title>");

            out.println(
                "<link rel='stylesheet' type='text/css' " +
                "href='/RailwayReservationSystem/css/style.css'>"
            );


            /*
             * Waiting-list specific styling.
             */

            out.println("<style>");

            out.println(".success-box {");
            out.println("background-color: #eef9ee;");
            out.println("border-left: 5px solid #008000;");
            out.println("padding: 15px;");
            out.println("margin-bottom: 20px;");
            out.println("}");

            out.println(".queue-number {");
            out.println("font-size: 28px;");
            out.println("font-weight: bold;");
            out.println("color: #1f4e79;");
            out.println("text-align: center;");
            out.println("padding: 15px;");
            out.println("background-color: #eef5fb;");
            out.println("border: 2px solid #1f4e79;");
            out.println("border-radius: 8px;");
            out.println("margin: 15px 0;");
            out.println("}");

            out.println(".waiting-details {");
            out.println("width: 100%;");
            out.println("border-collapse: collapse;");
            out.println("margin-top: 15px;");
            out.println("}");

            out.println(".waiting-details td {");
            out.println("padding: 10px;");
            out.println("border-bottom: 1px solid #ddd;");
            out.println("}");

            out.println(".waiting-details td:first-child {");
            out.println("font-weight: bold;");
            out.println("width: 35%;");
            out.println("}");

            out.println("</style>");

            out.println("</head>");


            /*
             * BODY
             */

            out.println("<body>");


            /*
             * HEADER
             */

            out.println("<header>");

            out.println(
                "<h1>&#128646; Railway Reservation System</h1>"
            );

            out.println(
                "<p>Waiting List Management</p>"
            );

            out.println("</header>");


            /*
             * MAIN CONTAINER
             */

            out.println("<div class='container'>");


            /*
             * Success section.
             */

            out.println("<div class='section'>");

            out.println("<div class='success-box'>");

            out.println(
                "<h2>Added to Waiting List Successfully!</h2>"
            );

            out.println(
                "<p>Your request has been added to the waiting list.</p>"
            );

            out.println("</div>");


            /*
             * Queue position.
             */

            out.println(
                "<div class='queue-number'>" +
                "Queue Position: " +
                queuePosition +
                "</div>"
            );

            out.println("</div>");


            /*
             * Waiting-list details.
             */

            out.println("<div class='section'>");

            out.println("<h2>Waiting List Details</h2>");

            out.println(
                "<table class='waiting-details'>"
            );

            out.println(
                "<tr><td>Passenger</td><td>" +
                passengerName +
                "</td></tr>"
            );

            out.println(
                "<tr><td>Age</td><td>" +
                age +
                "</td></tr>"
            );

            out.println(
                "<tr><td>Gender</td><td>" +
                gender +
                "</td></tr>"
            );

            out.println(
                "<tr><td>Phone</td><td>" +
                phone +
                "</td></tr>"
            );

            /*
             * Show Train Number to the user.
             */

            out.println(
                "<tr><td>Train Number</td><td>" +
                actualTrainNumber +
                "</td></tr>"
            );

            out.println(
                "<tr><td>Train Name</td><td>" +
                trainName +
                "</td></tr>"
            );

            out.println(
                "<tr><td>Journey Date</td><td>" +
                journeyDate +
                "</td></tr>"
            );

            out.println(
                "<tr><td>Travel Class</td><td>" +
                travelClass +
                "</td></tr>"
            );

            out.println(
                "<tr><td>Queue Position</td><td>" +
                queuePosition +
                "</td></tr>"
            );

            out.println(
                "<tr><td>Status</td>" +
                "<td><b>WAITING</b></td></tr>"
            );

            out.println("</table>");

            out.println("</div>");


            /*
             * Navigation.
             */

            out.println("<div class='section'>");

            out.println(
                "<a class='back-link' href='waiting.html'>" +
                "Add Another Passenger" +
                "</a>"
            );

            out.println("<br><br>");

            out.println(
                "<a class='back-link' href='index.html'>" +
                "Back to Home" +
                "</a>"
            );

            out.println("</div>");


            /*
             * Close container.
             */

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


            /*
             * Close resources.
             */

            passengerKeys.close();
            passengerStatement.close();

            positionResult.close();
            positionStatement.close();

            waitingStatement.close();

        } catch (NumberFormatException e) {

            showError(
                out,
                "Invalid Input",
                "Please enter valid numbers for age and Train Number."
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                out,
                "Waiting List Failed!",
                e.getMessage()
            );

        } finally {

            try {

                if (connection != null) {
                    connection.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


    /*
     * Display styled error page.
     */

    private void showError(
            PrintWriter out,
            String title,
            String message) {

        out.println("<!DOCTYPE html>");

        out.println("<html>");

        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println("<title>" + title + "</title>");

        out.println(
            "<link rel='stylesheet' type='text/css' " +
            "href='/RailwayReservationSystem/css/style.css'>"
        );

        out.println("</head>");

        out.println("<body>");

        out.println("<header>");

        out.println(
            "<h1>Railway Reservation System</h1>"
        );

        out.println(
            "<p>Waiting List Management</p>"
        );

        out.println("</header>");

        out.println("<div class='container'>");

        out.println("<div class='section'>");

        out.println("<h2>" + title + "</h2>");

        out.println(
            "<div class='algorithm'>" +
            "<strong>Message:</strong> " +
            message +
            "</div>"
        );

        out.println(
            "<a class='back-link' href='waiting.html'>" +
            "Back to Waiting List" +
            "</a>"
        );

        out.println("</div>");

        out.println("</div>");

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