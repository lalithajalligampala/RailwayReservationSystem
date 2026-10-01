package com.railway.adsa;

public class FareCalculator {

    public static double calculateFare(double baseFare,
                                       String travelClass) {

        if (travelClass.equalsIgnoreCase("Sleeper")) {
            return baseFare;
        }

        if (travelClass.equalsIgnoreCase("3A")) {
            return baseFare * 1.50;
        }

        if (travelClass.equalsIgnoreCase("2A")) {
            return baseFare * 2.00;
        }

        if (travelClass.equalsIgnoreCase("1A")) {
            return baseFare * 2.50;
        }

        return baseFare;
    }
}