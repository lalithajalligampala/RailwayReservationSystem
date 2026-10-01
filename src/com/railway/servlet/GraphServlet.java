package com.railway.servlet;

import com.railway.adsa.RailwayGraph;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GraphServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        RailwayGraph railwayGraph = new RailwayGraph();

        railwayGraph.addConnection("Bhimavaram", "Vijayawada");
        railwayGraph.addConnection("Vijayawada", "Rajahmundry");
        railwayGraph.addConnection("Vijayawada", "Hyderabad");
        railwayGraph.addConnection("Rajahmundry", "Visakhapatnam");
        railwayGraph.addConnection("Hyderabad", "Visakhapatnam");

        String startStation = request.getParameter("station");

        if (startStation == null ||
            startStation.trim().equals("")) {

            startStation = "Bhimavaram";
        }

        List<String> bfsResult =
                railwayGraph.BFS(startStation);

        List<String> dfsResult =
                railwayGraph.DFS(startStation);

        String destinationStation =
                request.getParameter("destination");

        if (destinationStation == null ||
            destinationStation.trim().equals("")) {

            destinationStation = "Visakhapatnam";
        }

        List<String> shortestRoute =
                railwayGraph.shortestPath(
                        startStation,
                        destinationStation
                );


        /*
         * HTML START
         */

        out.println("<!DOCTYPE html>");
        out.println("<html>");

        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println("<title>Railway Route Network</title>");

        out.println(
            "<link rel='stylesheet' type='text/css' " +
            "href='/RailwayReservationSystem/css/style.css'>"
        );


        /*
         * Graph-specific styling
         */

        out.println("<style>");

        out.println(".route-box {");
        out.println("background-color: #eef5fb;");
        out.println("border-left: 5px solid #1f4e79;");
        out.println("padding: 15px;");
        out.println("margin: 10px 0;");
        out.println("line-height: 1.8;");
        out.println("}");

        out.println(".result-box {");
        out.println("background-color: #f8f8f8;");
        out.println("border: 1px solid #ddd;");
        out.println("border-radius: 8px;");
        out.println("padding: 15px;");
        out.println("margin-top: 15px;");
        out.println("font-size: 16px;");
        out.println("}");

        out.println(".path-box {");
        out.println("background-color: #eef9ee;");
        out.println("border-left: 5px solid #008000;");
        out.println("padding: 15px;");
        out.println("margin-top: 15px;");
        out.println("font-weight: bold;");
        out.println("}");

        out.println(".station-count {");
        out.println("font-size: 18px;");
        out.println("font-weight: bold;");
        out.println("color: #1f4e79;");
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
            "<p>Railway Route Network</p>"
        );

        out.println("</header>");


        /*
         * MAIN CONTAINER
         */

        out.println("<div class='container'>");


        /*
         * Station Connections
         */

        out.println("<div class='section'>");

        out.println("<h2>Station Connections</h2>");

        out.println("<div class='route-box'>");

        out.println(
            "<b>Bhimavaram</b> -> <b>Vijayawada</b><br>"
        );

        out.println(
            "<b>Vijayawada</b> -> <b>Rajahmundry</b><br>"
        );

        out.println(
            "<b>Vijayawada</b> -> <b>Hyderabad</b><br>"
        );

        out.println(
            "<b>Rajahmundry</b> -> <b>Visakhapatnam</b><br>"
        );

        out.println(
            "<b>Hyderabad</b> -> <b>Visakhapatnam</b>"
        );

        out.println("</div>");

        out.println("</div>");


        /*
         * Station Selection
         */

        out.println("<div class='section'>");

        out.println("<h2>Select Route</h2>");

        out.println(
            "<form method='get' action='graph'>"
        );

        out.println(
            "<label><b>Starting Station:</b></label><br>"
        );

        out.println("<select name='station'>");

        out.println(
            "<option value='Bhimavaram'>Bhimavaram</option>"
        );

        out.println(
            "<option value='Vijayawada'>Vijayawada</option>"
        );

        out.println(
            "<option value='Rajahmundry'>Rajahmundry</option>"
        );

        out.println(
            "<option value='Hyderabad'>Hyderabad</option>"
        );

        out.println(
            "<option value='Visakhapatnam'>Visakhapatnam</option>"
        );

        out.println("</select>");

        out.println("<br><br>");

        out.println(
            "<label><b>Destination Station:</b></label><br>"
        );

        out.println(
            "<select name='destination'>"
        );

        out.println(
            "<option value='Bhimavaram'>Bhimavaram</option>"
        );

        out.println(
            "<option value='Vijayawada'>Vijayawada</option>"
        );

        out.println(
            "<option value='Rajahmundry'>Rajahmundry</option>"
        );

        out.println(
            "<option value='Hyderabad'>Hyderabad</option>"
        );

        out.println(
            "<option value='Visakhapatnam'>Visakhapatnam</option>"
        );

        out.println("</select>");

        out.println("<br><br>");

        out.println(
            "<input type='submit' value='Run Traversal'>"
        );

        out.println("</form>");

        out.println("</div>");

        /*
         * Shortest Route
         */

        out.println("<div class='section'>");

        out.println("<h2>Shortest Route</h2>");

        out.println(
            "<p><b>From:</b> " +
            startStation +
            "</p>"
        );

        out.println(
            "<p><b>To:</b> " +
            destinationStation +
            "</p>"
        );

        if (shortestRoute.isEmpty()) {

            out.println("<div class='result-box'>");

            out.println(
                "No route found."
            );

            out.println("</div>");

        } else {

            out.println("<div class='path-box'>");

            for (int i = 0;
                 i < shortestRoute.size();
                 i++) {

                out.println(
                    shortestRoute.get(i)
                );

                if (i <
                    shortestRoute.size() - 1) {

                    out.println(" -> ");
                }
            }

            out.println("</div>");
        }

        out.println(
            "<p class='station-count'>" +
            "Number of Stations: " +
            shortestRoute.size() +
            "</p>"
        );

        out.println("</div>");

        /*
         * Navigation
         */

        out.println("<div class='section'>");

        out.println(
            "<a class='back-link' href='graph.html'>" +
            "Run Another Route Search" +
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