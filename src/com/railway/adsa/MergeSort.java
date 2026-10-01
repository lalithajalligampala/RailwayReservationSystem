package com.railway.adsa;

import com.railway.model.Train;
import java.util.List;

public class MergeSort {

    public static void sortByDepartureTime(List<Train> trains) {

        if (trains.size() <= 1) {
            return;
        }

        mergeSort(trains, 0, trains.size() - 1);
    }

    private static void mergeSort(
            List<Train> trains,
            int left,
            int right) {

        if (left < right) {

            int mid = (left + right) / 2;

            mergeSort(trains, left, mid);

            mergeSort(trains, mid + 1, right);

            merge(trains, left, mid, right);
        }
    }

    private static void merge(
            List<Train> trains,
            int left,
            int mid,
            int right) {

        int i = left;
        int j = mid + 1;

        java.util.ArrayList<Train> temp =
                new java.util.ArrayList<Train>();

        while (i <= mid && j <= right) {

            String time1 =
                    trains.get(i).getDepartureTime();

            String time2 =
                    trains.get(j).getDepartureTime();

            if (time1.compareTo(time2) <= 0) {

                temp.add(trains.get(i));
                i++;

            } else {

                temp.add(trains.get(j));
                j++;
            }
        }

        while (i <= mid) {

            temp.add(trains.get(i));
            i++;
        }

        while (j <= right) {

            temp.add(trains.get(j));
            j++;
        }

        for (int k = 0; k < temp.size(); k++) {

            trains.set(left + k, temp.get(k));
        }
    }
}