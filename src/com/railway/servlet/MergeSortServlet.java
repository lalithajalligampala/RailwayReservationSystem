package com.railway.servlet;

import com.railway.adsa.MergeSort;
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

public class MergeSortServlet extends HttpServlet {

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        List<Train> trains = new ArrayList<Train>();

        try {

            String sql =
                    "SELECT * FROM trains";

            try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    Train train = new Train();

                    train.setTrainId(
                            resultSet.getInt("train_id"));

                    train.setTrainNumber(
                            resultSet.getInt("train_number"));

                    train.setTrainName(
                            resultSet.getString("train_name"));

                    train.setSource(
                            resultSet.getString("source"));

                    train.setDestination(
                            resultSet.getString("destination"));

                    train.setDepartureTime(
                            resultSet.getString("departure_time"));

                    train.setArrivalTime(
                            resultSet.getString("arrival_time"));

                    train.setTotalSeats(
                            resultSet.getInt("total_seats"));

                    train.setAvailableSeats(
                            resultSet.getInt("available_seats"));

                    train.setFare(
                            resultSet.getDouble("fare"));

                    trains.add(train);
                }
            }


            /*
             * Apply Merge Sort
             */

            MergeSort.sortByDepartureTime(trains);


            /*
             * HTML START
             */

            out.println("<!DOCTYPE html>");

            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println(
                "<title>Merge Sort - Railway Reservation</title>"
            );

            out.println(
                "<link rel='stylesheet' type='text/css' " +
                "href='/RailwayReservationSystem/css/style.css'>"
            );


            /*
             * Merge Sort specific styling
             */

            out.println("<style>");

            out.println(".sort-success {");
            out.println("background-color: #eef9ee;");
            out.println("border-left: 5px solid #008000;");
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
                "<h1>Railway Reservation System</h1>"
            );

            out.println(
                "<p>Train Sorting using Merge Sort</p>"
            );

            out.println("</header>");


            /*
             * MAIN CONTAINER
             */

            out.println("<div class='container'>");


            /*
             * Page information
             */

            out.println("<div class='section'>");

            out.println(
                "<h2>ADSA - Merge Sort</h2>"
            );

            out.println(
                "<p>" +
                "Trains sorted by departure time " +
                "using Merge Sort." +
                "</p>"
            );

            out.println("</div>");


            /*
             * Success information
             */

            out.println("<div class='section'>");

            out.println("<div class='sort-success'>");

            out.println(
                "<h2>Sorting Completed</h2>"
            );

            out.println(
                "<p>" +
                "All train records have been sorted " +
                "according to departure time." +
                "</p>"
            );

            out.println("</div>");

            out.println("</div>");


            /*
             * Sorted train table
             */

            out.println("<div class='section'>");

            out.println(
                "<h2>Trains Sorted by Departure Time</h2>"
            );

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


            /*
             * Display sorted trains
             */

            for (Train train : trains) {

                out.println("<tr>");

                out.println(
                    "<td>" +
                    train.getTrainNumber() +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    train.getTrainName() +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    train.getSource() +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    train.getDestination() +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    train.getDepartureTime() +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    train.getArrivalTime() +
                    "</td>"
                );

                out.println(
                    "<td>" +
                    train.getAvailableSeats() +
                    "</td>"
                );

                out.println(
                    "<td>Rs. " +
                    train.getFare() +
                    "</td>"
                );

                out.println("</tr>");
            }

            out.println("</table>");

            out.println("</div>");

            out.println("</div>");


            /*
             * ADSA Information
             */

            out.println("<div class='section'>");

            out.println(
                "<h2>ADSA Algorithm</h2>"
            );

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Algorithm:</strong> " +
                "Merge Sort"
            );

            out.println("</div>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Sorting Criteria:</strong> " +
                "Departure Time"
            );

            out.println("</div>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Time Complexity:</strong> " +
                "O(n log n)"
            );

            out.println("</div>");

            out.println(
                "<div class='complexity-box'>"
            );

            out.println(
                "<b>How it works:</b><br>"
            );

            out.println(
                "Merge Sort divides the train records into " +
                "smaller groups, sorts those groups, and then " +
                "merges them to produce the final sorted order."
            );

            out.println("</div>");

            out.println("</div>");


            /*
             * Navigation
             */

            out.println("<div class='section'>");

            out.println(
                "<a class='back-link' " +
                "href='mergeSort.html'>" +
                "Run Merge Sort Again" +
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
                "ADSA Course Based Project"
            );

            out.println("</footer>");

            out.println("</body>");

            out.println("</html>");


        } catch (Exception e) {

            /*
             * Styled error page
             */

            out.println("<!DOCTYPE html>");

            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<title>Merge Sort Error</title>");

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
                "<p>Merge Sort</p>"
            );

            out.println("</header>");

            out.println("<div class='container'>");

            out.println("<div class='section'>");

            out.println(
                "<h2>Error Occurred</h2>"
            );

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Message:</strong> " +
                e.getMessage()
            );

            out.println("</div>");

            out.println(
                "<a class='back-link' " +
                "href='mergeSort.html'>" +
                "Back to Merge Sort" +
                "</a>"
            );

            out.println("</div>");

            out.println("</div>");

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
}