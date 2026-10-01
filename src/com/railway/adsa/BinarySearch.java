package com.railway.adsa;

import com.railway.model.Train;
import java.util.List;

public class BinarySearch {

    public static Train searchByTrainNumber(
            List<Train> trains, int trainNumber) {

        int low = 0;
        int high = trains.size() - 1;

        while (low <= high) {

            int mid = (low + high) / 2;

            int midTrainNumber =
                    trains.get(mid).getTrainNumber();

            if (midTrainNumber == trainNumber) {
                return trains.get(mid);
            }

            if (midTrainNumber < trainNumber) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return null;
    }
}