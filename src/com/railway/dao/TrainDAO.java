package com.railway.dao;

import com.railway.model.Train;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TrainDAO {

    public List<Train> searchTrains(String source, String destination) {

        List<Train> trains = new ArrayList<>();

        String sql = "SELECT * FROM trains " +
                     "WHERE source = ? AND destination = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, source);
            statement.setString(2, destination);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Train train = new Train();

                train.setTrainId(resultSet.getInt("train_id"));
                train.setTrainNumber(resultSet.getInt("train_number"));
                train.setTrainName(resultSet.getString("train_name"));
                train.setSource(resultSet.getString("source"));
                train.setDestination(resultSet.getString("destination"));
                train.setDepartureTime(
                        resultSet.getString("departure_time")
                );
                train.setArrivalTime(
                        resultSet.getString("arrival_time")
                );
                train.setTotalSeats(
                        resultSet.getInt("total_seats")
                );
                train.setAvailableSeats(
                        resultSet.getInt("available_seats")
                );
                train.setFare(
                        resultSet.getDouble("fare")
                );

                trains.add(train);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return trains;
    }
}