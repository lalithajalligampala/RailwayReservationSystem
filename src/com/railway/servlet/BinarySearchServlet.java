package com.railway.servlet;

import com.railway.adsa.BinarySearch;
import com.railway.dao.DBConnection;
import com.railway.model.Train;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BinarySearchServlet extends HttpServlet {

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        String trainNumberText =
                request.getParameter("trainNumber");


        /*
         * HTML START
         */

        out.println("<!DOCTYPE html>");
        out.println("<html>");

        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println("<title>Binary Search</title>");

        out.println(
            "<link rel='stylesheet' type='text/css' " +
            "href='/RailwayReservationSystem/css/style.css'>"
        );


        /*
         * Binary Search specific styling
         */

        out.println("<style>");

        out.println(".search-success {");
        out.println("background-color: #eef9ee;");
        out.println("border-left: 5px solid #008000;");
        out.println("padding: 15px;");
        out.println("margin-bottom: 20px;");
        out.println("}");

        out.println(".search-error {");
        out.println("background-color: #fff4f4;");
        out.println("border-left: 5px solid #cc0000;");
        out.println("padding: 15px;");
        out.println("margin-bottom: 20px;");
        out.println("}");

        out.println(".complexity-box {");
        out.println("background-color: #eef5fb;");
        out.println("border-left: 5px solid #1f4e79;");
        out.println("padding: 15px;");
        out.println("margin-top: 15px;");
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
            "<p>Train Search</p>"
        );

        out.println("</header>");


        /*
         * MAIN CONTAINER
         */

        out.println("<div class='container'>");


        /*
         * Page heading
         */


        /*
         * Input validation
         */

        if (trainNumberText == null ||
            trainNumberText.trim().isEmpty()) {

            out.println("<div class='section'>");

            out.println("<div class='search-error'>");

            out.println(
                "<h2>Train Number Required</h2>"
            );

            out.println(
                "<p>Please enter a train number.</p>"
            );

            out.println("</div>");

            out.println(
                "<a class='back-link' " +
                "href='binarySearch.html'>" +
                "Back to Binary Search" +
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

            return;
        }


        try {

            int trainNumber =
                    Integer.parseInt(trainNumberText);


            /*
             * Load trains from database.
             *
             * The records are sorted by train number
             * before applying Binary Search.
             */

            List<Train> trains =
                    new ArrayList<Train>();

            String sql =
                    "SELECT * FROM trains " +
                    "ORDER BY train_number";

            try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    Train train = new Train();

                    train.setTrainId(
                            resultSet.getInt(
                                    "train_id"));

                    train.setTrainNumber(
                            resultSet.getInt(
                                    "train_number"));

                    train.setTrainName(
                            resultSet.getString(
                                    "train_name"));

                    train.setSource(
                            resultSet.getString(
                                    "source"));

                    train.setDestination(
                            resultSet.getString(
                                    "destination"));

                    train.setDepartureTime(
                            resultSet.getString(
                                    "departure_time"));

                    train.setArrivalTime(
                            resultSet.getString(
                                    "arrival_time"));

                    train.setTotalSeats(
                            resultSet.getInt(
                                    "total_seats"));

                    train.setAvailableSeats(
                            resultSet.getInt(
                                    "available_seats"));

                    train.setFare(
                            resultSet.getDouble(
                                    "fare"));

                    trains.add(train);
                }
            }


            /*
             * Binary Search
             */

            Train foundTrain =
                    BinarySearch.searchByTrainNumber(
                            trains,
                            trainNumber);


            /*
             * Train Found
             */

            if (foundTrain != null) {

                out.println("<div class='section'>");

                out.println(
                    "<div class='search-success'>"
                );

                out.println(
                    "<h2>Train Found!</h2>"
                );

                out.println(
                    "<p>Search successfully found " +
                    "the requested train.</p>"
                );

                out.println("</div>");


                /*
                 * Train details table
                 */

                out.println("<h2>Train Details</h2>");

                out.println(
                    "<div class='train-table-container'>"
                );

                out.println(
                    "<table class='train-table'>"
                );

                out.println("<tr>");

                out.println(
                    "<th>Train Number</th>"
                );

                out.println(
                    "<th>Train Name</th>"
                );

                out.println(
                    "<th>Source</th>"
                );

                out.println(
                    "<th>Destination</th>"
                );

                out.println(
                    "<th>Departure</th>"
                );

                out.println(
                    "<th>Arrival</th>"
                );

                out.println(
                    "<th>Available Seats</th>"
                );

                out.println(
                    "<th>Fare</th>"
                );

                out.println("</tr>");

                out.println("<tr>");

                out.println(
                    "<td>" +
                    foundTrain.getTrainNumber() +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    foundTrain.getTrainName() +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    foundTrain.getSource() +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    foundTrain.getDestination() +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    foundTrain.getDepartureTime() +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    foundTrain.getArrivalTime() +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    foundTrain.getAvailableSeats() +
                    "</td>"
                );

                out.println(
                    "<td>Rs. " +
                    foundTrain.getFare() +
                    "</td>"
                );

                out.println("</tr>");

                out.println("</table>");

                out.println("</div>");

                out.println("</div>");

            } else {


                /*
                 * Train Not Found
                 */

                out.println("<div class='section'>");

                out.println(
                    "<div class='search-error'>"
                );

                out.println(
                    "<h2>Train Not Found</h2>"
                );

                out.println(
                    "<p>No train exists with train number " +
                    trainNumber +
                    ".</p>"
                );

                out.println("</div>");

                out.println("</div>");
            }


        } catch (NumberFormatException e) {


            /*
             * Invalid number
             */

            out.println("<div class='section'>");

            out.println(
                "<div class='search-error'>"
            );

            out.println(
                "<h2>Invalid Train Number</h2>"
            );

            out.println(
                "<p>Please enter a valid numeric train number.</p>"
            );

            out.println("</div>");

            out.println("</div>");


        } catch (Exception e) {


            /*
             * General error
             */

            out.println("<div class='section'>");

            out.println(
                "<div class='search-error'>"
            );

            out.println(
                "<h2>Error Occurred</h2>"
            );

            out.println(
                "<p>" +
                e.getMessage() +
                "</p>"
            );

            out.println("</div>");

            out.println("</div>");

            e.printStackTrace();
        }


        /*
         * Navigation
         */

        out.println("<div class='section'>");

        out.println(
            "<a class='back-link' " +
            "href='binarySearch.html'>" +
            "Search Again" +
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
         * CLOSE CONTAINER
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
    }
}