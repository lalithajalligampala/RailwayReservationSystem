package com.railway.servlet;

import com.railway.dao.DBConnection;
import com.railway.adsa.SeatArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TrainDetailsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        String trainNumberText =
                request.getParameter("trainNumber");

        String trainIdParameter =
                request.getParameter("trainId");

        /*
         * If trainNumber is provided, find the train
         * using train_number.
         *
         * Otherwise, keep the existing trainId functionality.
         */

        if ((trainNumberText == null ||
             trainNumberText.trim().equals("")) &&
            (trainIdParameter == null ||
             trainIdParameter.trim().equals(""))) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Train Details</title>");

            out.println(
                "<link rel='stylesheet' type='text/css' " +
                "href='/RailwayReservationSystem/css/style.css'>"
            );

            out.println("</head>");

            out.println("<body>");

            out.println("<header>");
            out.println("<h1>&#128646; Railway Reservation System</h1>");
            out.println("<p>Train Details</p>");
            out.println("</header>");

            out.println("<div class='container'>");
            out.println("<div class='section'>");

            out.println("<h2>Train Number is Missing</h2>");

            out.println(
                "<p>Please enter a train number.</p>"
            );

            out.println(
                "<a class='back-link' " +
                "href='trainDetails.html'>" +
                "Back to Train Details" +
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

        Connection connection = null;

        try {

            connection = DBConnection.getConnection();

            String sql;
            PreparedStatement statement;

            /*
             * NEW:
             * If train number is provided, search using
             * train_number.
             */

            if (trainNumberText != null &&
                !trainNumberText.trim().equals("")) {

                int trainNumber =
                        Integer.parseInt(trainNumberText);

                sql =
                    "SELECT * FROM trains " +
                    "WHERE train_number = ?";

                statement =
                        connection.prepareStatement(sql);

                statement.setInt(1, trainNumber);

            } else {

                /*
                 * EXISTING:
                 * Search using train_id.
                 */

                int trainId =
                        Integer.parseInt(trainIdParameter);

                sql =
                    "SELECT * FROM trains " +
                    "WHERE train_id = ?";

                statement =
                        connection.prepareStatement(sql);

                statement.setInt(1, trainId);
            }

            ResultSet resultSet =
                    statement.executeQuery();

            if (!resultSet.next()) {

                out.println("<!DOCTYPE html>");
                out.println("<html>");

                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<title>Train Not Found</title>");

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
                out.println("<p>Train Details</p>");
                out.println("</header>");

                out.println("<div class='container'>");
                out.println("<div class='section'>");

                out.println("<h2>Train Not Found</h2>");

                out.println(
                    "<p>The requested train does not exist.</p>"
                );

                out.println(
                    "<a class='back-link' " +
                    "href='trainDetails.html'>" +
                    "Back to Train Details" +
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

                connection.close();

                return;
            }

            int trainId =
                    resultSet.getInt("train_id");

            int trainNumber =
                    resultSet.getInt("train_number");

            String trainName =
                    resultSet.getString("train_name");

            String source =
                    resultSet.getString("source");

            String destination =
                    resultSet.getString("destination");

            String departureTime =
                    resultSet.getString("departure_time");

            String arrivalTime =
                    resultSet.getString("arrival_time");

            int totalSeats =
                    resultSet.getInt("total_seats");

            int availableSeats =
                    resultSet.getInt("available_seats");

            double fare =
                    resultSet.getDouble("fare");

            /*
             * Calculate booked seats.
             */

            int bookedSeats =
                    totalSeats - availableSeats;

            /*
             * PAGE
             */

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<title>Train Details</title>");

            out.println(
                "<link rel='stylesheet' type='text/css' " +
                "href='/RailwayReservationSystem/css/style.css'>"
            );

            /*
             * Small extra styling for seat layout.
             */

            out.println("<style>");

            out.println(".seat-layout {");
            out.println("display: flex;");
            out.println("flex-wrap: wrap;");
            out.println("gap: 8px;");
            out.println("margin-top: 15px;");
            out.println("}");

            out.println(".seat {");
            out.println("display: inline-block;");
            out.println("width: 50px;");
            out.println("padding: 10px 5px;");
            out.println("text-align: center;");
            out.println("border-radius: 6px;");
            out.println("font-weight: bold;");
            out.println("border: 2px solid;");
            out.println("}");

            out.println(".seat-available {");
            out.println("background-color: #ccffcc;");
            out.println("border-color: #008000;");
            out.println("}");

            out.println(".seat-booked {");
            out.println("background-color: #ffcccc;");
            out.println("border-color: #cc0000;");
            out.println("}");

            out.println(".seat-legend {");
            out.println("margin-top: 15px;");
            out.println("font-weight: bold;");
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
                "<p>Train Details and Seat Availability</p>"
            );

            out.println("</header>");

            /*
             * MAIN CONTAINER
             */

            out.println("<div class='container'>");

            /*
             * TRAIN INFORMATION
             */

            out.println("<div class='section'>");

            out.println("<h2>"
                    + trainName
                    + "</h2>");

            out.println(
                "<div class='algorithm'>" +
                "<strong>Train Number:</strong> " +
                trainNumber +
                "</div>"
            );

            out.println(
                "<div class='algorithm'>" +
                "<strong>Route:</strong> " +
                source +
                " -> " +
                destination +
                "</div>"
            );

            out.println(
                "<div class='algorithm'>" +
                "<strong>Departure:</strong> " +
                departureTime +
                "</div>"
            );

            out.println(
                "<div class='algorithm'>" +
                "<strong>Arrival:</strong> " +
                arrivalTime +
                "</div>"
            );

            out.println("</div>");

            /*
             * SEAT AVAILABILITY
             */

            out.println("<div class='section'>");

            out.println("<h2>Seat Availability</h2>");

            out.println(
                "<p><b>Total Seats:</b> " +
                totalSeats +
                "</p>"
            );

            out.println(
                "<p><b>Available Seats:</b> " +
                availableSeats +
                "</p>"
            );

            out.println(
                "<p><b>Booked Seats:</b> " +
                bookedSeats +
                "</p>"
            );

            out.println(
                "<p><b>Availability:</b> " +
                availableSeats +
                " / " +
                totalSeats +
                "</p>"
            );

            /*
             * SEAT ARRAY
             */

            SeatArray seatArray =
                    new SeatArray(totalSeats);

            /*
             * Load confirmed booked seats.
             */

            String seatSql =
                    "SELECT seat_number FROM bookings " +
                    "WHERE train_id = ? " +
                    "AND booking_status = 'CONFIRMED' " +
                    "AND seat_number IS NOT NULL";

            PreparedStatement seatStatement =
                    connection.prepareStatement(seatSql);

            seatStatement.setInt(1, trainId);

            ResultSet seatResult =
                    seatStatement.executeQuery();

            while (seatResult.next()) {

                int bookedSeat =
                        seatResult.getInt("seat_number");

                seatArray.occupySeat(bookedSeat);
            }

            /*
             * SEAT LAYOUT
             */

            out.println("<h2>Seat Layout</h2>");

            out.println("<div class='seat-layout'>");

            for (int i = 1;
                 i <= seatArray.getTotalSeats();
                 i++) {

                String status =
                        seatArray.getSeatStatus(i);

                if ("BOOKED".equals(status)) {

                    out.println(
                        "<span class='seat seat-booked'>" +
                        i +
                        " B</span>"
                    );

                } else {

                    out.println(
                        "<span class='seat seat-available'>" +
                        i +
                        " A</span>"
                    );
                }
            }

            out.println("</div>");

            out.println(
                "<p class='seat-legend'>" +
                "Legend: A = Available, B = Booked" +
                "</p>"
            );

            out.println("</div>");

            /*
             * FARE INFORMATION
             */

            out.println("<div class='section'>");

            out.println("<h2>Fare Information</h2>");

            out.println(
                "<p><b>Base Fare:</b> Rs. " +
                fare +
                "</p>"
            );

            out.println("<h2>Travel Classes</h2>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Sleeper:</strong> Rs. " +
                fare
            );

            out.println("</div>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>3A:</strong> Rs. " +
                (fare * 1.50)
            );

            out.println("</div>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>2A:</strong> Rs. " +
                (fare * 2.00)
            );

            out.println("</div>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>1A:</strong> Rs. " +
                (fare * 2.50)
            );

            out.println("</div>");

            out.println("</div>");

            /*
             * BOOKING LINK
             */

            out.println("<div class='section'>");

            out.println(
                "<a class='table-button' " +
                "href='book.html?trainId=" +
                trainId +
                "'>" +
                "Book This Train" +
                "</a>"
            );

            out.println(
                "<a class='back-link' " +
                "href='trainDetails.html'>" +
                "Back to Train Details" +
                "</a>"
            );

            out.println("</div>");

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

            seatResult.close();
            seatStatement.close();
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<!DOCTYPE html>");

            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<title>Error</title>");

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

            out.println("<p>Train Details</p>");

            out.println("</header>");

            out.println("<div class='container'>");

            out.println("<div class='section'>");

            out.println("<h2>Error Loading Train Details</h2>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Error:</strong> " +
                e.getMessage()
            );

            out.println("</div>");

            out.println(
                "<a class='back-link' " +
                "href='trainDetails.html'>" +
                "Back to Train Details" +
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
}