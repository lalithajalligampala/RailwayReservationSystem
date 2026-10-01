package com.railway.servlet;

import com.railway.adsa.WaitingQueue;
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

public class WaitingQueueServlet extends HttpServlet {

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        String trainNumberText =
                request.getParameter("trainNumber");

        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Waiting Queue</title>");

        out.println(
            "<link rel='stylesheet' type='text/css' " +
            "href='/RailwayReservationSystem/css/style.css'>"
        );

        out.println("<style>");

        out.println(
            ".queue-info {" +
            "background:#f4f8fb;" +
            "border-left:5px solid #2c3e50;" +
            "padding:15px;" +
            "margin-top:20px;" +
            "border-radius:5px;" +
            "}"
        );

        out.println(
            ".first-queue {" +
            "font-weight:bold;" +
            "}"
        );

        out.println(
            ".queue-size {" +
            "font-size:18px;" +
            "font-weight:bold;" +
            "margin-bottom:10px;" +
            "}"
        );

        out.println(
            ".empty-queue {" +
            "background:#f4f8fb;" +
            "padding:20px;" +
            "border-radius:5px;" +
            "margin-top:20px;" +
            "}"
        );

        out.println(
            ".queue-table-container {" +
            "overflow-x:auto;" +
            "margin-top:20px;" +
            "}"
        );

        out.println(
            ".queue-table {" +
            "width:100%;" +
            "border-collapse:collapse;" +
            "}"
        );

        out.println(
            ".queue-table th, .queue-table td {" +
            "padding:12px;" +
            "border:1px solid #ddd;" +
            "text-align:center;" +
            "}"
        );

        out.println(
            ".queue-table th {" +
            "font-weight:bold;" +
            "}"
        );

        out.println(
            ".queue-status {" +
            "font-weight:bold;" +
            "}"
        );

        out.println(
            ".algorithm-box {" +
            "background:#f4f8fb;" +
            "border-left:5px solid #2c3e50;" +
            "padding:15px;" +
            "margin-top:20px;" +
            "border-radius:5px;" +
            "}"
        );

        out.println("</style>");
        out.println("</head>");

        out.println("<body>");

        out.println("<header>");
        out.println("<h1>&#128646; Railway Reservation System</h1>");
        out.println("<p>Waiting List Management</p>");
        out.println("</header>");

        out.println("<div class='container'>");

        out.println("<div class='section'>");

        out.println("<h2>Waiting List Queue</h2>");

        if (trainNumberText == null ||
            trainNumberText.trim().isEmpty()) {

            out.println(
                "<div class='empty-queue'>"
            );

            out.println(
                "<p>Please provide a Train Number.</p>"
            );

            out.println("</div>");

            out.println(
                "<br><a class='back-link' " +
                "href='waitingQueue.html'>Back</a>"
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

            return;
        }

        try {

            int trainNumber =
                    Integer.parseInt(trainNumberText);

            WaitingQueue queue =
                    new WaitingQueue();

            /*
             * Step 1:
             * Find internal Train ID using Train Number.
             */

            String trainSQL =
                    "SELECT train_id, train_number, train_name " +
                    "FROM trains " +
                    "WHERE train_number = ?";

            int trainId = 0;
            String trainName = "";

            try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement trainStatement =
                        connection.prepareStatement(trainSQL)
            ) {

                trainStatement.setInt(1, trainNumber);

                try (ResultSet trainResult =
                        trainStatement.executeQuery()) {

                    if (!trainResult.next()) {

                        out.println(
                            "<div class='empty-queue'>"
                        );

                        out.println(
                            "<p>Train Number " +
                            trainNumber +
                            " does not exist.</p>"
                        );

                        out.println("</div>");

                        out.println(
                            "<br><a class='back-link' " +
                            "href='waitingQueue.html'>" +
                            "Try Another Train</a>"
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

                        return;
                    }

                    trainId =
                            trainResult.getInt("train_id");

                    trainName =
                            trainResult.getString("train_name");
                }
            }

            /*
             * Step 2:
             * Find waiting passengers using
             * internal Train ID.
             */

            String sql =
                    "SELECT w.waiting_id, "
                    + "w.passenger_id, "
                    + "p.passenger_name, "
                    + "w.queue_position "
                    + "FROM waiting_list w "
                    + "JOIN passengers p "
                    + "ON w.passenger_id = p.passenger_id "
                    + "WHERE w.train_id = ? "
                    + "AND w.booking_status = 'WAITING' "
                    + "ORDER BY w.queue_position";

            try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
            ) {

                statement.setInt(1, trainId);

                try (ResultSet resultSet =
                        statement.executeQuery()) {

                    out.println(
                        "<div class='queue-info'>"
                    );

                    out.println(
                        "<h3>Train Information</h3>"
                    );

                    out.println(
                        "<p><b>Train Number:</b> " +
                        trainNumber +
                        "</p>"
                    );

                    out.println(
                        "<p><b>Train Name:</b> " +
                        trainName +
                        "</p>"
                    );

                    out.println("</div>");

                    out.println("<h3>Waiting Queue</h3>");

                    out.println(
                        "<div class='queue-table-container'>"
                    );

                    out.println(
                        "<table class='queue-table'>"
                    );

                    out.println("<tr>");

                    out.println(
                        "<th>Queue Position</th>"
                    );

                    out.println(
                        "<th>Passenger ID</th>"
                    );

                    out.println(
                        "<th>Passenger Name</th>"
                    );

                    out.println(
                        "<th>Queue Status</th>"
                    );

                    out.println("</tr>");

                    boolean hasPassengers = false;

                    while (resultSet.next()) {

                        hasPassengers = true;

                        int passengerId =
                                resultSet.getInt(
                                    "passenger_id"
                                );

                        String passengerName =
                                resultSet.getString(
                                    "passenger_name"
                                );

                        int position =
                                resultSet.getInt(
                                    "queue_position"
                                );

                        /*
                         * Add passenger to FIFO queue.
                         */

                        queue.enqueue(passengerId);

                        out.println("<tr>");

                        out.println(
                            "<td>" +
                            position +
                            "</td>"
                        );

                        out.println(
                            "<td>" +
                            passengerId +
                            "</td>"
                        );

                        out.println(
                            "<td>" +
                            passengerName +
                            "</td>"
                        );

                        if (position == 1) {

                            out.println(
                                "<td class='first-queue'>" +
                                "FIRST IN QUEUE" +
                                "</td>"
                            );

                        } else {

                            out.println(
                                "<td class='queue-status'>" +
                                "WAITING" +
                                "</td>"
                            );
                        }

                        out.println("</tr>");
                    }

                    out.println("</table>");
                    out.println("</div>");

                    if (!hasPassengers) {

                        out.println(
                            "<div class='empty-queue'>"
                        );

                        out.println(
                            "<p>" +
                            "No passengers are currently " +
                            "waiting for this train." +
                            "</p>"
                        );

                        out.println("</div>");

                    } else {

                        out.println(
                            "<div class='queue-info'>"
                        );

                        out.println(
                            "<h3>Queue Information</h3>"
                        );

                        out.println(
                            "<p class='queue-size'>" +
                            "Queue Size: " +
                            queue.size() +
                            "</p>"
                        );

                        out.println(
                            "<p>" +
                            "<b>First Passenger ID:</b> " +
                            queue.peek() +
                            "</p>"
                        );


                        out.println("</div>");
                    }
                }
            }

            out.println("</div>");

        } catch (NumberFormatException e) {

            out.println(
                "<div class='empty-queue'>"
            );

            out.println(
                "<p>" +
                "Please enter a valid numeric Train Number." +
                "</p>"
            );

            out.println("</div>");

        } catch (Exception e) {

            out.println(
                "<div class='empty-queue'>"
            );

            out.println(
                "<h3>Error occurred</h3>"
            );

            out.println(
                "<p>" +
                e.getMessage() +
                "</p>"
            );

            out.println("</div>");
        }

        out.println("<br>");

        out.println(
            "<a class='back-link' " +
            "href='waitingQueue.html'>" +
            "Check Another Train" +
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