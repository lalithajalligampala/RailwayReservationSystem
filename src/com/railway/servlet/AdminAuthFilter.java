package com.railway.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = {
        "/admin.html",
        "/addTrain",
        "/updateTrain",
        "/deleteTrain",
        "/viewTrains",
        "/viewBookings",
        "/dashboard"
})
public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        boolean loggedIn = false;

        if (session != null) {

            Object adminStatus =
                    session.getAttribute("adminLoggedIn");

            if (adminStatus != null
                    && adminStatus.equals(true)) {

                loggedIn = true;
            }
        }

        if (loggedIn) {

            // Admin is authenticated
            chain.doFilter(request, response);

        } else {

            // Admin is not authenticated
            httpResponse.sendRedirect(
                    httpRequest.getContextPath()
                    + "/adminLogin.html"
            );
        }
    }
}