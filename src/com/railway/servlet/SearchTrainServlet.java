package com.railway.servlet;

import com.railway.dao.TrainDAO;
import com.railway.model.Train;
import com.railway.adsa.Sorting;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchTrainServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        String source = request.getParameter("source");
        String destination = request.getParameter("destination");
        String sort = request.getParameter("sort");

        TrainDAO trainDAO = new TrainDAO();

        List<Train> trains =
                trainDAO.searchTrains(source, destination);

        /* ADSA Sorting */

        if ("fare".equals(sort)) {

            Sorting.sortByFare(trains);

        } else if ("seats".equals(sort)) {

            Sorting.sortByAvailableSeats(trains);
        }

        /* ================= HTML ================= */

        out.println("<html>");

        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println("<title>Train Search Results</title>");

        out.println(
            "<link rel='stylesheet' type='text/css' " +
            "href='/RailwayReservationSystem/css/style.css'>"
        );

        out.println("</head>");

        out.println("<body>");

        /* ================= HEADER ================= */

        out.println("<header>");

        out.println("<h1>&#128646; Railway Reservation System</h1>");

        out.println("<p>Train Search Results</p>");

        out.println("</header>");

        /* ================= CONTAINER ================= */

        out.println("<div class='container'>");

        out.println("<div class='section'>");

        out.println("<h2>Available Trains</h2>");

        /* ================= SEARCH DETAILS ================= */

        out.println(
            "<p><b>Source:</b> " +
            source +
            "</p>"
        );

        out.println(
            "<p><b>Destination:</b> " +
            destination +
            "</p>"
        );

        if ("fare".equals(sort)) {

            out.println(
                "<p><b>Sorted By:</b> " +
                "Fare - Low to High</p>"
            );

        } else if ("seats".equals(sort)) {

            out.println(
                "<p><b>Sorted By:</b> " +
                "Available Seats - High to Low</p>"
            );

        } else {

            out.println(
                "<p><b>Sorted By:</b> None</p>"
            );
        }

        /* ================= NO TRAINS ================= */

        if (trains.isEmpty()) {

            out.println(
                "<div class='algorithm'>"
            );

            out.println(
                "<strong>No trains found!</strong>"
            );

            out.println("</div>");

        } else {

            /* ================= TABLE ================= */

            out.println(
                "<div class='train-table-container'>"
            );

            out.println(
                "<table class='train-table'>"
            );

            /* Header */

            out.println("<tr>");

            out.println("<th>Train No.</th>");

            out.println("<th>Train Name</th>");

            out.println("<th>Source</th>");

            out.println("<th>Destination</th>");

            out.println("<th>Departure</th>");

            out.println("<th>Arrival</th>");

            out.println("<th>Seats</th>");

            out.println("<th>Fare</th>");

            out.println("<th>Action</th>");

            out.println("</tr>");

            /* Train records */

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

                /* Action buttons */

                out.println("<td class='action-cell'>");

                out.println(
                    "<a class='table-button' " +
                    "href='trainDetails?trainId=" +
                    train.getTrainId() +
                    "'>" +
                    "View" +
                    "</a>"
                );

                out.println(
                    "<a class='table-button' " +
                    "href='book.html?trainId=" +
                    train.getTrainId() +
                    "'>" +
                    "Book" +
                    "</a>"
                );

                out.println("</td>");

                out.println("</tr>");
            }

            out.println("</table>");

            out.println("</div>");
        }

        /* ================= NAVIGATION ================= */

        out.println("<br>");

        out.println(
            "<a class='back-link' href='search.html'>"
        );

        out.println("Search Again");

        out.println("</a>");

        out.println("<br><br>");

        out.println(
            "<a class='back-link' href='index.html'>"
        );

        out.println("Back to Home");

        out.println("</a>");

        out.println("</div>");

        out.println("</div>");

        /* ================= FOOTER ================= */

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