package com.railway.adsa;

public class SeatArray {

    private boolean[] seats;

    public SeatArray(int totalSeats) {
        seats = new boolean[totalSeats];
    }

    // Mark a particular seat as occupied
    public void occupySeat(int seatNumber) {

        if (seatNumber >= 1 &&
            seatNumber <= seats.length) {

            seats[seatNumber - 1] = true;
        }
    }

    // Find and book the first available seat
    public int bookSeat() {

        for (int i = 0; i < seats.length; i++) {

            if (!seats[i]) {

                seats[i] = true;

                return i + 1;
            }
        }

        return -1;
    }

    // Release a seat
    public void releaseSeat(int seatNumber) {

        if (seatNumber >= 1 &&
            seatNumber <= seats.length) {

            seats[seatNumber - 1] = false;
        }
    }

    // Check whether a seat is occupied
    public boolean isOccupied(int seatNumber) {

        if (seatNumber < 1 ||
            seatNumber > seats.length) {

            return false;
        }

        return seats[seatNumber - 1];
    }

    // Get total number of seats
    public int getTotalSeats() {

        return seats.length;
    }

    // Get status of a particular seat
    public String getSeatStatus(int seatNumber) {

        if (seatNumber < 1 ||
            seatNumber > seats.length) {

            return "INVALID";
        }

        if (seats[seatNumber - 1]) {

            return "BOOKED";

        } else {

            return "AVAILABLE";
        }
    }

    // Count available seats
    public int getAvailableSeatCount() {

        int count = 0;

        for (int i = 0; i < seats.length; i++) {

            if (!seats[i]) {

                count++;
            }
        }

        return count;
    }

    // Get all available seats
    public String getAvailableSeats() {

        String result = "";

        for (int i = 0; i < seats.length; i++) {

            if (!seats[i]) {

                if (!result.equals("")) {
                    result = result + ", ";
                }

                result = result + (i + 1);
            }
        }

        if (result.equals("")) {

            return "No seats available";
        }

        return result;
    }
}