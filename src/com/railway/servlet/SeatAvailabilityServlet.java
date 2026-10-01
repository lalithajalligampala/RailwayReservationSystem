package com.railway.servlet;

import com.railway.adsa.SeatArray;
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

public class SeatAvailabilityServlet extends HttpServlet {

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        String trainNumberText =
                request.getParameter("trainNumber");

        out.println("<!DOCTYPE html>");
        out.println("<html>");

        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Seat Availability</title>");

        out.println(
            "<link rel='stylesheet' type='text/css' " +
            "href='/RailwayReservationSystem/css/style.css'>"
        );

        out.println("<style>");

        out.println(".train-image {");
        out.println("width: 100%;");
        out.println("max-height: 300px;");
        out.println("object-fit: cover;");
        out.println("border-radius: 10px;");
        out.println("margin-bottom: 20px;");
        out.println("}");

        out.println(".seat-summary {");
        out.println("background-color: #eef5fb;");
        out.println("border-left: 5px solid #1f4e79;");
        out.println("padding: 15px;");
        out.println("margin: 10px 0;");
        out.println("}");

        out.println(".available-box {");
        out.println("background-color: #eef9ee;");
        out.println("border-left: 5px solid #008000;");
        out.println("padding: 15px;");
        out.println("margin: 10px 0;");
        out.println("}");

        out.println(".seat-layout {");
        out.println("margin-top: 15px;");
        out.println("padding: 10px;");
        out.println("background-color: #fafafa;");
        out.println("border: 1px solid #ddd;");
        out.println("border-radius: 8px;");
        out.println("}");

        out.println(".seat {");
        out.println("display: inline-block;");
        out.println("width: 50px;");
        out.println("padding: 10px 5px;");
        out.println("margin: 5px;");
        out.println("text-align: center;");
        out.println("border-radius: 6px;");
        out.println("font-weight: bold;");
        out.println("}");

        out.println(".booked-seat {");
        out.println("background-color: #f4cccc;");
        out.println("border: 2px solid #cc0000;");
        out.println("}");

        out.println(".available-seat {");
        out.println("background-color: #d9ead3;");
        out.println("border: 2px solid #008000;");
        out.println("}");

        out.println(".legend {");
        out.println("margin-top: 15px;");
        out.println("padding: 10px;");
        out.println("}");

        out.println("</style>");

        out.println("</head>");

        out.println("<body>");

        out.println("<header>");

        out.println("<h1>&#128646; Railway Reservation System</h1>");

        out.println(
            "<p>Seat Availability</p>"
        );

        out.println("</header>");

        out.println("<div class='container'>");

        /*
         * Train picture
         */

        

        /*
         * Input validation
         */

        if (trainNumberText == null ||
            trainNumberText.trim().isEmpty()) {

            out.println("<div class='section'>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Message:</strong> " +
                "Please provide a train number."
            );

            out.println("</div>");

            out.println(
                "<a class='back-link' " +
                "href='seatAvailability.html'>" +
                "Back to Seat Availability" +
                "</a>"
            );

            out.println("</div>");

            out.println("</div>");

            out.println("<footer>");

            out.println(
                "Railway Reservation System"
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
             * Find train using Train Number
             */

            String sql =
                    "SELECT train_id, train_number, train_name, total_seats " +
                    "FROM trains WHERE train_number = ?";

            try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
            ) {

                statement.setInt(1, trainNumber);

                try (
                    ResultSet resultSet =
                            statement.executeQuery()
                ) {

                    if (!resultSet.next()) {

                        out.println("<div class='section'>");

                        out.println(
                            "<div class='algorithm'>"
                        );

                        out.println(
                            "<strong>Train Not Found:</strong> " +
                            "No train exists with Train Number " +
                            trainNumber +
                            "."
                        );

                        out.println("</div>");

                        out.println(
                            "<a class='back-link' " +
                            "href='seatAvailability.html'>" +
                            "Try Again" +
                            "</a>"
                        );

                        out.println("</div>");

                        out.println("</div>");

                        out.println("<footer>");

                        out.println(
                            "Railway Reservation System"
                        );

                        out.println("</footer>");

                        out.println("</body>");
                        out.println("</html>");

                        return;
                    }

                    /*
                     * Internal Train ID
                     */

                    int trainId =
                            resultSet.getInt("train_id");

                    int actualTrainNumber =
                            resultSet.getInt("train_number");

                    String trainName =
                            resultSet.getString("train_name");

                    int totalSeats =
                            resultSet.getInt("total_seats");

                    /*
                     * Create SeatArray
                     */

                    SeatArray seatArray =
                            new SeatArray(totalSeats);

                    /*
                     * Find already booked seats
                     */

                    String bookingSql =
                            "SELECT seat_number FROM bookings " +
                            "WHERE train_id = ? " +
                            "AND booking_status = 'CONFIRMED' " +
                            "AND seat_number IS NOT NULL";

                    try (
                        PreparedStatement bookingStatement =
                                connection.prepareStatement(
                                        bookingSql)
                    ) {

                        bookingStatement.setInt(
                                1,
                                trainId
                        );

                        try (
                            ResultSet bookingResult =
                                    bookingStatement.executeQuery()
                        ) {

                            while (bookingResult.next()) {

                                int seatNumber =
                                        bookingResult.getInt(
                                                "seat_number");

                                seatArray.occupySeat(
                                        seatNumber
                                );
                            }
                        }
                    }

                    /*
                     * Train information
                     */

                    out.println("<div class='section'>");

                    out.println(
                        "<h2>" +
                        actualTrainNumber +
                        " - " +
                        trainName +
                        "</h2>"
                    );

                    out.println(
                        "<div class='seat-summary'>"
                    );

                    out.println(
                        "<b>Total Seats:</b> " +
                        seatArray.getTotalSeats()
                    );

                    out.println("</div>");

                    out.println(
                        "<div class='available-box'>"
                    );

                    out.println(
                        "<b>Available Seats:</b> " +
                        seatArray.getAvailableSeatCount()
                    );

                    out.println("</div>");

                    out.println("</div>");

                    /*
                     * Available seat numbers
                     */

                    out.println("<div class='section'>");

                    out.println(
                        "<h2>Available Seat Numbers</h2>"
                    );

                    out.println(
                        "<div class='algorithm'>"
                    );

                    out.println(
                        seatArray.getAvailableSeats()
                    );

                    out.println("</div>");

                    out.println("</div>");

                    /*
                     * Seat Layout
                     */

                    out.println("<div class='section'>");

                    out.println("<h2>Seat Layout</h2>");

                    out.println(
                        "<div class='seat-layout'>"
                    );

                    for (int i = 1;
                         i <= seatArray.getTotalSeats();
                         i++) {

                        if (seatArray.isOccupied(i)) {

                            out.println(
                                "<span class='seat booked-seat'>" +
                                i +
                                " B</span>"
                            );

                        } else {

                            out.println(
                                "<span class='seat available-seat'>" +
                                i +
                                " A</span>"
                            );
                        }

                        if (i % 10 == 0) {

                            out.println("<br>");
                        }
                    }

                    out.println("</div>");

                    /*
                     * Seat legend
                     */

                    out.println(
                        "<div class='legend'>"
                    );

                    out.println(
                        "<b>B = Booked</b>"
                    );

                    out.println("<br>");

                    out.println(
                        "<b>A = Available</b>"
                    );

                    out.println("</div>");

                    out.println("</div>");
                }
            }

        } catch (NumberFormatException e) {

            out.println("<div class='section'>");

            out.println(
                "<div class='algorithm'>"
            );

            out.println(
                "<strong>Invalid Train Number:</strong> " +
                "Please enter a valid numeric Train Number."
            );

            out.println("</div>");

            out.println("</div>");

        } catch (Exception e) {

            out.println("<div class='section'>");

            out.println(
                "<h2>Error Occurred</h2>"
            );

            out.println(
                "<div class='algorithm'>"
            );

            out.println(
                "<strong>Message:</strong> " +
                e.getMessage()
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
            "href='seatAvailability.html'>" +
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

        /*
         * Close container
         */

        out.println("</div>");

        /*
         * Footer
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