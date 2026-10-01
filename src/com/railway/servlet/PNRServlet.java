package com.railway.servlet;

import com.railway.dao.DBConnection;
import com.railway.adsa.PNRHashTable;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PNRServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
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

        out.println("<title>PNR Status</title>");

        out.println(
            "<link rel='stylesheet' type='text/css' " +
            "href='/RailwayReservationSystem/css/style.css'>"
        );


        /*
         * Extra styling for PNR page.
         */

        out.println("<style>");

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

        out.println(".pnr-details {");
        out.println("width: 100%;");
        out.println("border-collapse: collapse;");
        out.println("margin-top: 15px;");
        out.println("}");

        out.println(".pnr-details td {");
        out.println("padding: 10px;");
        out.println("border-bottom: 1px solid #ddd;");
        out.println("}");

        out.println(".pnr-details td:first-child {");
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
            "<p>PNR Status Search</p>"
        );

        out.println("</header>");


        /*
         * MAIN CONTAINER
         */

        out.println("<div class='container'>");


        /*
         * Page heading.
         */

        out.println("<div class='section'>");

        out.println("<h2>PNR Status</h2>");

        out.println(
            "<p>Search for a booking using the PNR number.</p>"
        );

        out.println("</div>");


        /*
         * Check PNR input.
         */

        if (pnrText == null ||
            pnrText.trim().isEmpty()) {

            out.println("<div class='section'>");

            out.println("<h2>PNR Missing</h2>");

            out.println(
                "<div class='warning-box'>" +
                "<strong>Please enter a PNR number.</strong>" +
                "</div>"
            );

            out.println(
                "<a class='back-link' href='pnr.html'>" +
                "Back to PNR Search" +
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

            long pnr =
                    Long.parseLong(pnrText);

            Connection connection =
                    DBConnection.getConnection();


            /*
             * Database connection check.
             */

            if (connection == null) {

                out.println("<div class='section'>");

                out.println(
                    "<h2>Database Connection Failed!</h2>"
                );

                out.println(
                    "<div class='warning-box'>" +
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
             * Step 1:
             * Create PNR Hash Table.
             */

            PNRHashTable pnrTable =
                    new PNRHashTable();


            /*
             * Step 2:
             * Load PNR and booking ID
             * into the Hash Table.
             */

            String hashSQL =
                    "SELECT pnr, booking_id FROM bookings";

            PreparedStatement hashStatement =
                    connection.prepareStatement(hashSQL);

            ResultSet hashResult =
                    hashStatement.executeQuery();

            while (hashResult.next()) {

                long storedPNR =
                        hashResult.getLong("pnr");

                int bookingId =
                        hashResult.getInt("booking_id");

                pnrTable.insert(
                        storedPNR,
                        bookingId
                );
            }


            /*
             * Step 3:
             * Search requested PNR
             * using Hash Table.
             */

            int bookingId =
                    pnrTable.search(pnr);


            /*
             * PNR NOT FOUND
             */

            if (bookingId == -1) {

                out.println("<div class='section'>");

                out.println("<h2>No Booking Found</h2>");

                out.println(
                    "<div class='warning-box'>"
                );

                out.println(
                    "<p><strong>PNR:</strong> " +
                    pnr +
                    "</p>"
                );

                out.println(
                    "<p>No booking exists for this PNR.</p>"
                );

                out.println("</div>");

                out.println("</div>");
                
                connection.close();

                out.println("<div class='section'>");

                out.println(
                    "<a class='back-link' href='pnr.html'>" +
                    "Check Another PNR" +
                    "</a>"
                );

                out.println("<br><br>");

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

                return;
            }


            /*
             * Step 4:
             * Use booking ID returned by
             * Hash Table to get full details.
             */

            String sql =
                    "SELECT b.pnr, " +
                    "p.passenger_name, " +
                    "p.age, " +
                    "p.gender, " +
                    "p.phone, " +
                    "t.train_number, " +
                    "t.train_name, " +
                    "t.source, " +
                    "t.destination, " +
                    "b.journey_date, " +
                    "b.seat_number, " +
                    "b.travel_class, " +
                    "b.fare, " +
                    "b.booking_status " +
                    "FROM bookings b " +
                    "JOIN passengers p " +
                    "ON b.passenger_id = p.passenger_id " +
                    "JOIN trains t " +
                    "ON b.train_id = t.train_id " +
                    "WHERE b.booking_id = ?";


            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setInt(
                    1,
                    bookingId
            );

            ResultSet result =
                    statement.executeQuery();


            /*
             * Booking found.
             */

            if (result.next()) {

                out.println("<div class='section'>");

                out.println("<div class='success-box'>");

                out.println(
                    "<h2>Booking Found</h2>"
                );

                out.println(
                    "<p>The requested PNR was successfully found " +
                    "using the Hash Table.</p>"
                );

                out.println("</div>");


                /*
                 * PNR display.
                 */

                out.println(
                    "<div class='pnr-box'>" +
                    "PNR: " +
                    result.getLong("pnr") +
                    "</div>"
                );


                /*
                 * Booking details.
                 */

                out.println("<h2>Booking Details</h2>");

                out.println(
                    "<table class='pnr-details'>"
                );

                out.println(
                    "<tr><td>PNR</td><td>" +
                    result.getLong("pnr") +
                    "</td></tr>"
                );

                out.println(
                    "<tr><td>Passenger</td><td>" +
                    result.getString("passenger_name") +
                    "</td></tr>"
                );

                out.println(
                    "<tr><td>Age</td><td>" +
                    result.getInt("age") +
                    "</td></tr>"
                );

                out.println(
                    "<tr><td>Gender</td><td>" +
                    result.getString("gender") +
                    "</td></tr>"
                );

                out.println(
                    "<tr><td>Phone</td><td>" +
                    result.getString("phone") +
                    "</td></tr>"
                );

                out.println(
                    "<tr><td>Train Number</td><td>" +
                    result.getInt("train_number") +
                    "</td></tr>"
                );

                out.println(
                    "<tr><td>Train Name</td><td>" +
                    result.getString("train_name") +
                    "</td></tr>"
                );

                out.println(
                    "<tr><td>Route</td><td>" +
                    result.getString("source") +
                    " -> " +
                    result.getString("destination") +
                    "</td></tr>"
                );

                out.println(
                    "<tr><td>Journey Date</td><td>" +
                    result.getDate("journey_date") +
                    "</td></tr>"
                );

                out.println(
                    "<tr><td>Seat Number</td><td>" +
                    result.getInt("seat_number") +
                    "</td></tr>"
                );

                out.println(
                    "<tr><td>Travel Class</td><td>" +
                    result.getString("travel_class") +
                    "</td></tr>"
                );

                out.println(
                    "<tr><td>Fare</td><td>Rs. " +
                    result.getDouble("fare") +
                    "</td></tr>"
                );

                out.println(
                    "<tr><td>Status</td><td><b>" +
                    result.getString("booking_status") +
                    "</b></td></tr>"
                );

                out.println("</table>");

                out.println("</div>");

            } else {

                out.println("<div class='section'>");

                out.println("<h2>No Booking Found</h2>");

                out.println(
                    "<div class='warning-box'>"
                );

                out.println(
                    "No booking exists for PNR: " +
                    pnr
                );

                out.println("</div>");

                out.println("</div>");
            }


            /*
             * Close database resources.
             */

            result.close();
            statement.close();

            hashResult.close();
            hashStatement.close();

            connection.close();


        } catch (NumberFormatException e) {

            out.println("<div class='section'>");

            out.println("<h2>Invalid PNR</h2>");

            out.println(
                "<div class='warning-box'>" +
                "<strong>Please enter numbers only.</strong>" +
                "</div>"
            );

            out.println(
                "<a class='back-link' href='pnr.html'>" +
                "Back to PNR Search" +
                "</a>"
            );

            out.println("</div>");


        } catch (Exception e) {

            e.printStackTrace();

            out.println("<div class='section'>");

            out.println("<h2>Error</h2>");

            out.println(
                "<div class='warning-box'>" +
                "<strong>Error:</strong> " +
                e.getMessage() +
                "</div>"
            );

            out.println(
                "<a class='back-link' href='pnr.html'>" +
                "Back to PNR Search" +
                "</a>"
            );

            out.println("</div>");
        }


        /*
         * Navigation.
         */

        out.println("<div class='section'>");

        out.println(
            "<a class='back-link' href='pnr.html'>" +
            "Check Another PNR" +
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
         * FOOTER
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