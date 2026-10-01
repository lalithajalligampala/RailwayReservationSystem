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

public class DeleteTrainServlet extends HttpServlet {

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

        out.println("<title>Delete Train</title>");

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

        out.println(".train-number-box {");
        out.println("background-color: #eef5fb;");
        out.println("border: 1px solid #c8dceb;");
        out.println("border-radius: 8px;");
        out.println("padding: 20px;");
        out.println("margin-top: 15px;");
        out.println("font-size: 18px;");
        out.println("}");

        out.println("</style>");

        out.println("</head>");

        out.println("<body>");

        /*
         * HEADER
         */

        out.println("<header>");

        out.println(
            "<h1>Railway Reservation System</h1>"
        );

        out.println(
            "<p>Delete Train</p>"
        );

        out.println("</header>");


        /*
         * MAIN CONTAINER
         */

        out.println("<div class='container'>");

        out.println("<div class='section'>");

        out.println(
            "<h2>Train Deletion</h2>"
        );


        try {

            /*
             * Read train number
             */

            int trainNumber =
                    Integer.parseInt(
                        request.getParameter("trainNumber")
                    );


            /*
             * DELETE QUERY
             */

            String sql =
                    "DELETE FROM trains " +
                    "WHERE train_number = ?";


            Connection connection =
                    DBConnection.getConnection();


            PreparedStatement statement =
                    connection.prepareStatement(sql);


            statement.setInt(
                    1,
                    trainNumber
            );


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
                    "<h2>Train Deleted Successfully!</h2>"
                );

                out.println(
                    "<p>" +
                    "The train has been removed " +
                    "from the railway system." +
                    "</p>"
                );

                out.println("</div>");


                out.println(
                    "<div class='train-number-box'>"
                );

                out.println(
                    "<b>Deleted Train Number:</b> " +
                    trainNumber
                );

                out.println("</div>");


            } else {

                /*
                 * TRAIN NOT FOUND
                 */

                out.println(
                    "<div class='error-box'>"
                );

                out.println(
                    "<h2>Train Not Found</h2>"
                );

                out.println(
                    "<p>" +
                    "No train exists with Train Number " +
                    trainNumber +
                    "." +
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
                "<h2>Invalid Train Number</h2>"
            );

            out.println(
                "<p>" +
                "Please enter a valid numeric Train Number." +
                "</p>"
            );

            out.println("</div>");


        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                "<div class='error-box'>"
            );

            out.println(
                "<h2>Error Deleting Train</h2>"
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
         * INFORMATION
         */

        out.println("<div class='section'>");

        out.println(
            "<div class='algorithm'>"
        );

        out.println(
            "<strong>Database Operation:</strong> " +
            "DELETE"
        );

        out.println("</div>");

        out.println(
            "<div class='algorithm'>"
        );

        out.println(
            "<strong>Search Condition:</strong> " +
            "Train Number"
        );

        out.println("</div>");

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
            "ADSA Course Based Project"
        );

        out.println("</footer>");

        out.println("</body>");

        out.println("</html>");
    }
}