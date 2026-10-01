package com.railway.servlet;

import com.railway.dao.DBConnection;
import com.railway.adsa.SeatArray;
import com.railway.adsa.FareCalculator;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BookTicketServlet extends HttpServlet {

    private long generateUniquePNR(Connection connection)
            throws Exception {

        Random random = new Random();

        while (true) {

            long pnr =
                    1000000000L +
                    random.nextInt(900000000);

            String sql =
                    "SELECT COUNT(*) FROM bookings WHERE pnr = ?";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setLong(1, pnr);

            ResultSet result =
                    statement.executeQuery();

            if (result.next()) {

                if (result.getInt(1) == 0) {
                    result.close();
                    statement.close();
                    return pnr;
                }
            }

            result.close();
            statement.close();
        }
    }

    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        String passengerName =
                request.getParameter("passengerName");

        int age =
                Integer.parseInt(request.getParameter("age"));

        String gender =
                request.getParameter("gender");

        String phone =
                request.getParameter("phone");

        int trainNumber =
                Integer.parseInt(request.getParameter("trainNumber"));

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
             * Find train using Train Number.
             *
             * train_id remains an internal database value.
             */

            String trainSQL =
                    "SELECT train_id, train_number, train_name, " +
                    "total_seats, available_seats, fare " +
                    "FROM trains " +
                    "WHERE train_number = ?";

            PreparedStatement trainStatement =
                    connection.prepareStatement(trainSQL);

            trainStatement.setInt(1, trainNumber);

            ResultSet trainResult =
                    trainStatement.executeQuery();

            if (!trainResult.next()) {

                showError(
                    out,
                    "Train Not Found!",
                    "The entered train number does not exist."
                );

                trainResult.close();
                trainStatement.close();

                return;
            }

            int trainId =
                    trainResult.getInt("train_id");

            String trainName =
                    trainResult.getString("train_name");

            int totalSeats =
                    trainResult.getInt("total_seats");

            int availableSeats =
                    trainResult.getInt("available_seats");

            double fare =
                    trainResult.getDouble("fare");

            fare =
                    FareCalculator.calculateFare(
                        fare,
                        travelClass
                    );

            /*
             * Step 2:
             * Check seat availability.
             */

            if (availableSeats <= 0) {

                showError(
                    out,
                    "No Seats Available!",
                    "Please use the waiting list."
                );

                trainResult.close();
                trainStatement.close();

                return;
            }

            /*
             * Step 3:
             * Create SeatArray.
             */

            SeatArray seatArray =
                    new SeatArray(totalSeats);

            /*
             * Step 4:
             * Load confirmed seats.
             */

            String occupiedSQL =
                    "SELECT seat_number " +
                    "FROM bookings " +
                    "WHERE train_id = ? " +
                    "AND journey_date = ? " +
                    "AND booking_status = 'CONFIRMED' " +
                    "AND seat_number IS NOT NULL";

            PreparedStatement occupiedStatement =
                    connection.prepareStatement(occupiedSQL);

            occupiedStatement.setInt(1, trainId);
            occupiedStatement.setString(2, journeyDate);

            ResultSet occupiedResult =
                    occupiedStatement.executeQuery();

            while (occupiedResult.next()) {

                int occupiedSeat =
                        occupiedResult.getInt("seat_number");

                seatArray.occupySeat(occupiedSeat);
            }

            /*
             * Step 5:
             * Find first available seat.
             */

            int seatNumber =
                    seatArray.bookSeat();

            if (seatNumber == -1) {

                showError(
                    out,
                    "No Seats Available!",
                    "All seats are currently occupied."
                );

                occupiedResult.close();
                occupiedStatement.close();
                trainResult.close();
                trainStatement.close();

                return;
            }

            /*
             * Step 6:
             * Generate unique PNR.
             */

            long pnr =
                    generateUniquePNR(connection);

            /*
             * Step 7:
             * Insert passenger.
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
             * Step 8:
             * Insert booking.
             */

            String bookingSQL =
                    "INSERT INTO bookings " +
                    "(pnr, passenger_id, train_id, " +
                    "journey_date, seat_number, travel_class, " +
                    "fare, booking_status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement bookingStatement =
                    connection.prepareStatement(bookingSQL);

            bookingStatement.setLong(1, pnr);
            bookingStatement.setInt(2, passengerId);
            bookingStatement.setInt(3, trainId);
            bookingStatement.setString(4, journeyDate);
            bookingStatement.setInt(5, seatNumber);
            bookingStatement.setString(6, travelClass);
            bookingStatement.setDouble(7, fare);
            bookingStatement.setString(8, "CONFIRMED");

            bookingStatement.executeUpdate();

            /*
             * Step 9:
             * Reduce available seats.
             */

            String updateSQL =
                    "UPDATE trains " +
                    "SET available_seats = available_seats - 1 " +
                    "WHERE train_id = ?";

            PreparedStatement updateStatement =
                    connection.prepareStatement(updateSQL);

            updateStatement.setInt(1, trainId);

            updateStatement.executeUpdate();

            /*
             * Step 10:
             * Display confirmation.
             */

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<title>Booking Confirmation</title>");

            out.println(
                "<link rel='stylesheet' type='text/css' " +
                "href='/RailwayReservationSystem/css/style.css'>"
            );

            out.println("<style>");

            out.println(".success-box {");
            out.println("background-color: #eef9ee;");
            out.println("border-left: 5px solid #008000;");
            out.println("padding: 15px;");
            out.println("margin-bottom: 20px;");
            out.println("}");

            out.println(".booking-details {");
            out.println("width: 100%;");
            out.println("border-collapse: collapse;");
            out.println("margin-top: 15px;");
            out.println("}");

            out.println(".booking-details td {");
            out.println("padding: 10px;");
            out.println("border-bottom: 1px solid #ddd;");
            out.println("}");

            out.println(".booking-details td:first-child {");
            out.println("font-weight: bold;");
            out.println("width: 35%;");
            out.println("}");

            out.println(".pnr-box {");
            out.println("font-size: 24px;");
            out.println("font-weight: bold;");
            out.println("padding: 15px;");
            out.println("background-color: #eef5fb;");
            out.println("border: 2px solid #1f4e79;");
            out.println("border-radius: 8px;");
            out.println("text-align: center;");
            out.println("margin: 15px 0;");
            out.println("}");

            out.println("</style>");

            out.println("</head>");
            out.println("<body>");

            out.println("<header>");
            out.println("<h1>&#128646; Railway Reservation System</h1>");
            out.println("<p>Booking Confirmation</p>");
            out.println("</header>");

            out.println("<div class='container'>");

            out.println("<div class='section'>");

            out.println("<div class='success-box'>");

            out.println(
                "<h2>Ticket Booked Successfully!</h2>"
            );

            out.println(
                "<p>Your railway ticket has been confirmed.</p>"
            );

            out.println("</div>");

            out.println(
                "<div class='pnr-box'>" +
                "PNR: " +
                pnr +
                "</div>"
            );

            out.println("</div>");

            /*
             * Booking Details
             */

            out.println("<div class='section'>");

            out.println("<h2>Booking Details</h2>");

            out.println("<table class='booking-details'>");

            out.println(
                "<tr><td>PNR</td><td>" +
                pnr +
                "</td></tr>"
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

            out.println(
                "<tr><td>Train Number</td><td>" +
                trainNumber +
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
                "<tr><td>Seat Number</td><td>" +
                seatNumber +
                "</td></tr>"
            );

            out.println(
                "<tr><td>Travel Class</td><td>" +
                travelClass +
                "</td></tr>"
            );

            out.println(
                "<tr><td>Fare</td><td>Rs. " +
                fare +
                "</td></tr>"
            );

            out.println(
                "<tr><td>Status</td>" +
                "<td><b>CONFIRMED</b></td></tr>"
            );

            out.println("</table>");

            out.println("</div>");

            
            /*
             * Navigation
             */

            out.println("<div class='section'>");

            out.println(
                "<a class='back-link' href='index.html'>" +
                "Back to Home" +
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

            /*
             * Close resources.
             */

            passengerKeys.close();
            passengerStatement.close();

            bookingStatement.close();
            updateStatement.close();

            occupiedResult.close();
            occupiedStatement.close();

            trainResult.close();
            trainStatement.close();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                out,
                "Booking Failed!",
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
            "<p>Ticket Booking</p>"
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
            "<a class='back-link' href='index.html'>" +
            "Back to Home" +
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