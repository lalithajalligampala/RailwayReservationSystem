package com.railway.adsa;

import com.railway.model.Train;
import java.util.List;

public class Sorting {

    public static void sortByFare(List<Train> trains) {

        for (int i = 0; i < trains.size() - 1; i++) {

            for (int j = 0; j < trains.size() - i - 1; j++) {

                if (trains.get(j).getFare() >
                    trains.get(j + 1).getFare()) {

                    Train temp = trains.get(j);

                    trains.set(j, trains.get(j + 1));

                    trains.set(j + 1, temp);
                }
            }
        }
    }

    public static void sortByAvailableSeats(List<Train> trains) {

        for (int i = 0; i < trains.size() - 1; i++) {

            for (int j = 0; j < trains.size() - i - 1; j++) {

                if (trains.get(j).getAvailableSeats() <
                    trains.get(j + 1).getAvailableSeats()) {

                    Train temp = trains.get(j);

                    trains.set(j, trains.get(j + 1));

                    trains.set(j + 1, temp);
                }
            }
        }
    }
}