package com.railway.servlet;

import com.railway.dao.DBConnection;

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

public class CancelTicketServlet extends HttpServlet {

    /*
     * Generate a unique 10-digit PNR
     */
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

                int count = result.getInt(1);

                if (count == 0) {
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

        String pnrText =
                request.getParameter("pnr");


        /*
         * Start HTML page.
         */

        out.println("<!DOCTYPE html>");

        out.println("<html>");

        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println("<title>Cancel Ticket</title>");

        out.println(
            "<link rel='stylesheet' type='text/css' " +
            "href='/RailwayReservationSystem/css/style.css'>"
        );

        /*
         * Extra styling for cancellation page.
         */

        out.println("<style>");

        out.println(".success-box {");
        out.println("background-color: #eef9ee;");
        out.println("border-left: 5px solid #008000;");
        out.println("padding: 15px;");
        out.println("margin-bottom: 20px;");
        out.println("}");

        out.println(".warning-box {");
        out.println("background-color: #fff8e1;");
        out.println("border-left: 5px solid #d99a00;");
        out.println("padding: 15px;");
        out.println("margin-bottom: 20px;");
        out.println("}");

        out.println(".cancel-box {");
        out.println("background-color: #fff0f0;");
        out.println("border-left: 5px solid #cc0000;");
        out.println("padding: 15px;");
        out.println("margin-bottom: 20px;");
        out.println("}");

        out.println(".pnr-box {");
        out.println("font-size: 22px;");
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


        /*
         * HEADER
         */

        out.println("<header>");

        out.println(
            "<h1>&#128646; Railway Reservation System</h1>"
        );

        out.println(
            "<p>Ticket Cancellation</p>"
        );

        out.println("</header>");


        /*
         * MAIN CONTAINER
         */

        out.println("<div class='container'>");


        /*
         * Check PNR input.
         */

        if (pnrText == null ||
            pnrText.trim().isEmpty()) {

            out.println("<div class='section'>");

            out.println("<h2>PNR is Missing</h2>");

            out.println(
                "<div class='warning-box'>" +
                "<strong>Please enter a PNR number.</strong>" +
                "</div>"
            );

            out.println(
                "<a class='back-link' href='cancel.html'>" +
                "Back to Cancellation" +
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

            long pnr =
                    Long.parseLong(pnrText);

            connection =
                    DBConnection.getConnection();


            /*
             * Database connection check.
             */

            if (connection == null) {

                out.println("<div class='section'>");

                out.println("<h2>Database Connection Failed</h2>");

                out.println(
                    "<div class='cancel-box'>" +
                    "Unable to connect to the database." +
                    "</div>"
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


            /*
             * Start transaction.
             */

            connection.setAutoCommit(false);


            /*
             * Step 1:
             * Find the booking.
             */

            String findSQL =
                    "SELECT booking_id, train_id, " +
                    "journey_date, seat_number, " +
                    "travel_class, fare, booking_status " +
                    "FROM bookings " +
                    "WHERE pnr = ?";

            PreparedStatement findStatement =
                    connection.prepareStatement(findSQL);

            findStatement.setLong(1, pnr);

            ResultSet result =
                    findStatement.executeQuery();


            /*
             * Booking not found.
             */

            if (!result.next()) {

                connection.rollback();

                out.println("<div class='section'>");

                out.println("<h2>Booking Not Found</h2>");

                out.println(
                    "<div class='warning-box'>" +
                    "<strong>PNR:</strong> " +
                    pnr +
                    "<br><br>" +
                    "No booking exists for this PNR." +
                    "</div>"
                );

                out.println(
                    "<a class='back-link' href='cancel.html'>" +
                    "Try Again" +
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


            int bookingId =
                    result.getInt("booking_id");

            int trainId =
                    result.getInt("train_id");

            String journeyDate =
                    result.getString("journey_date");

            int seatNumber =
                    result.getInt("seat_number");

            String travelClass =
                    result.getString("travel_class");

            double fare =
                    result.getDouble("fare");

            String status =
                    result.getString("booking_status");


            /*
             * Step 2:
             * Check whether already cancelled.
             */

            if ("CANCELLED".equalsIgnoreCase(status)) {

                connection.rollback();

                out.println("<div class='section'>");

                out.println("<h2>Ticket Already Cancelled</h2>");

                out.println(
                    "<div class='warning-box'>" +
                    "<strong>PNR:</strong> " +
                    pnr +
                    "<br><br>" +
                    "This ticket has already been cancelled." +
                    "</div>"
                );

                out.println(
                    "<a class='back-link' href='cancel.html'>" +
                    "Back to Cancellation" +
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


            /*
             * Step 3:
             * Cancel current booking.
             */

            String updateBookingSQL =
                    "UPDATE bookings " +
                    "SET booking_status = 'CANCELLED' " +
                    "WHERE booking_id = ?";

            PreparedStatement updateBooking =
                    connection.prepareStatement(
                        updateBookingSQL
                    );

            updateBooking.setInt(
                    1,
                    bookingId
            );

            updateBooking.executeUpdate();


            /*
             * Step 4:
             * Find first passenger in waiting queue.
             *
             * FIFO:
             * Smallest queue_position comes first.
             */

            String waitingSQL =
                    "SELECT waiting_id, passenger_id, " +
                    "travel_class, queue_position " +
                    "FROM waiting_list " +
                    "WHERE train_id = ? " +
                    "AND journey_date = ? " +
                    "AND booking_status = 'WAITING' " +
                    "ORDER BY queue_position ASC " +
                    "LIMIT 1";

            PreparedStatement waitingStatement =
                    connection.prepareStatement(waitingSQL);

            waitingStatement.setInt(
                    1,
                    trainId
            );

            waitingStatement.setString(
                    2,
                    journeyDate
            );

            ResultSet waitingResult =
                    waitingStatement.executeQuery();


            boolean passengerPromoted = false;

            long newPNR = 0;

            String promotedPassengerName = "";

            int promotedPassengerId = 0;

            String promotedTravelClass = "";


            /*
             * Step 5:
             * Promote waiting passenger if available.
             */

            if (waitingResult.next()) {

                int waitingId =
                        waitingResult.getInt(
                            "waiting_id"
                        );

                promotedPassengerId =
                        waitingResult.getInt(
                            "passenger_id"
                        );

                promotedTravelClass =
                        waitingResult.getString(
                            "travel_class"
                        );

                int queuePosition =
                        waitingResult.getInt(
                            "queue_position"
                        );


                /*
                 * Get passenger name.
                 */

                String passengerSQL =
                        "SELECT passenger_name " +
                        "FROM passengers " +
                        "WHERE passenger_id = ?";

                PreparedStatement passengerStatement =
                        connection.prepareStatement(
                            passengerSQL
                        );

                passengerStatement.setInt(
                        1,
                        promotedPassengerId
                );

                ResultSet passengerResult =
                        passengerStatement.executeQuery();

                if (passengerResult.next()) {

                    promotedPassengerName =
                            passengerResult.getString(
                                "passenger_name"
                            );
                }


                /*
                 * Generate new PNR.
                 */

                newPNR =
                        generateUniquePNR(
                            connection
                        );


                /*
                 * Create confirmed booking.
                 */

                String newBookingSQL =
                        "INSERT INTO bookings " +
                        "(pnr, passenger_id, train_id, " +
                        "journey_date, seat_number, " +
                        "travel_class, fare, " +
                        "booking_status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement newBookingStatement =
                        connection.prepareStatement(
                            newBookingSQL
                        );

                newBookingStatement.setLong(
                        1,
                        newPNR
                );

                newBookingStatement.setInt(
                        2,
                        promotedPassengerId
                );

                newBookingStatement.setInt(
                        3,
                        trainId
                );

                newBookingStatement.setString(
                        4,
                        journeyDate
                );


                /*
                 * Give cancelled seat
                 * to waiting passenger.
                 */

                newBookingStatement.setInt(
                        5,
                        seatNumber
                );

                newBookingStatement.setString(
                        6,
                        promotedTravelClass
                );

                newBookingStatement.setDouble(
                        7,
                        fare
                );

                newBookingStatement.setString(
                        8,
                        "CONFIRMED"
                );

                newBookingStatement.executeUpdate();


                /*
                 * Update waiting-list status.
                 */

                String promoteSQL =
                        "UPDATE waiting_list " +
                        "SET booking_status = 'CONFIRMED' " +
                        "WHERE waiting_id = ?";

                PreparedStatement promoteStatement =
                        connection.prepareStatement(
                            promoteSQL
                        );

                promoteStatement.setInt(
                        1,
                        waitingId
                );

                promoteStatement.executeUpdate();

                passengerPromoted = true;


            } else {

                /*
                 * No waiting passenger.
                 * Return seat to available seats.
                 */

                String updateTrainSQL =
                        "UPDATE trains " +
                        "SET available_seats = " +
                        "available_seats + 1 " +
                        "WHERE train_id = ?";

                PreparedStatement updateTrain =
                        connection.prepareStatement(
                            updateTrainSQL
                        );

                updateTrain.setInt(
                        1,
                        trainId
                );

                updateTrain.executeUpdate();
            }


            /*
             * Step 6:
             * Commit transaction.
             */

            connection.commit();


            /*
             * Step 7:
             * Display cancellation result.
             */

            out.println("<div class='section'>");

            out.println("<div class='success-box'>");

            out.println(
                "<h2>Ticket Cancelled Successfully!</h2>"
            );

            out.println(
                "<p>The ticket cancellation has been completed.</p>"
            );

            out.println("</div>");


            /*
             * Cancelled PNR.
             */

            out.println(
                "<div class='pnr-box'>" +
                "Cancelled PNR: " +
                pnr +
                "</div>"
            );


            /*
             * Cancellation details.
             */

            out.println("<h2>Cancellation Details</h2>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Cancelled PNR:</strong> " +
                pnr
            );

            out.println("</div>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Released Seat:</strong> " +
                seatNumber
            );

            out.println("</div>");

            out.println("<div class='algorithm'>");

            out.println(
                "<strong>Status:</strong> CANCELLED"
            );

            out.println("</div>");

            out.println("</div>");


            /*
             * Waiting-list promotion.
             */

            if (passengerPromoted) {

                out.println("<div class='section'>");

                out.println(
                    "<h2>Waiting List Passenger Promoted!</h2>"
                );

                out.println("<div class='success-box'>");

                out.println(
                    "<p><b>Passenger:</b> " +
                    promotedPassengerName +
                    "</p>"
                );

                out.println(
                    "<p><b>New PNR:</b> " +
                    newPNR +
                    "</p>"
                );

                out.println(
                    "<p><b>New Seat Number:</b> " +
                    seatNumber +
                    "</p>"
                );

                out.println(
                    "<p><b>Travel Class:</b> " +
                    promotedTravelClass +
                    "</p>"
                );

                out.println(
                    "<p><b>Status:</b> CONFIRMED</p>"
                );

                out.println("</div>");

                out.println("</div>");


                /*
                 * ADSA FIFO explanation.
                 */

                out.println("<div class='section'>");

                out.println("<h2>ADSA Waiting Queue</h2>");

                out.println("<div class='algorithm'>");

                out.println(
                    "<strong>Data Structure:</strong> Queue"
                );

                out.println("</div>");

                out.println("<div class='algorithm'>");

                out.println(
                    "<strong>Queue Principle:</strong> " +
                    "FIFO (First In, First Out)"
                );

                out.println("</div>");

                out.println("<div class='algorithm'>");

                out.println(
                    "<strong>Process:</strong> " +
                    "The first passenger in the waiting " +
                    "queue received the cancelled seat."
                );

                out.println("</div>");

                out.println("<div class='algorithm'>");

                out.println(
                    "<strong>Time Complexity:</strong> O(1) " +
                    "for queue insertion/removal"
                );

                out.println("</div>");

                out.println("</div>");


            } else {

                out.println("<div class='section'>");

                out.println("<h2>Seat Released</h2>");

                out.println(
                    "<div class='warning-box'>"
                );

                out.println(
                    "<p>No waiting passenger was found.</p>"
                );

                out.println(
                    "<p>The cancelled seat has been returned " +
                    "to the available seats.</p>"
                );

                out.println("</div>");

                out.println("</div>");
            }


            /*
             * Navigation.
             */

            out.println("<div class='section'>");

            out.println(
                "<a class='back-link' href='pnr.html'>" +
                "Check PNR Status" +
                "</a>"
            );

            out.println("<br><br>");

            out.println(
                "<a class='back-link' href='index.html'>" +
                "Back to Home" +
                "</a>"
            );

            out.println("</div>");

        } catch (NumberFormatException e) {

            out.println("<div class='section'>");

            out.println("<h2>Invalid PNR</h2>");

            out.println(
                "<div class='warning-box'>" +
                "<strong>Please enter numbers only.</strong>" +
                "</div>"
            );

            out.println(
                "<a class='back-link' href='cancel.html'>" +
                "Back to Cancellation" +
                "</a>"
            );

            out.println("</div>");


        } catch (Exception e) {

            try {

                if (connection != null) {
                    connection.rollback();
                }

            } catch (Exception rollbackException) {

                rollbackException.printStackTrace();
            }

            e.printStackTrace();

            out.println("<div class='section'>");

            out.println("<h2>Cancellation Failed!</h2>");

            out.println(
                "<div class='cancel-box'>" +
                "<strong>Error:</strong> " +
                e.getMessage() +
                "</div>"
            );

            out.println(
                "<a class='back-link' href='cancel.html'>" +
                "Back to Cancellation" +
                "</a>"
            );

            out.println("</div>");

        } finally {

            try {

                if (connection != null) {

                    connection.setAutoCommit(true);

                    connection.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }


        /*
         * Close page.
         */

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