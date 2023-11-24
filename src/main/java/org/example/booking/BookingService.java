package org.example.booking;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

// Exercises for Week 12 Day 05
public class BookingService {
    public void bookFlight(Connection connection, int customerId, int flightId, int numberOfPassengers) throws SQLException {
        connection.setAutoCommit(false);

        Savepoint savepoint = connection.setSavepoint("booking_savepoint");

        try {
            // Exercise 1.1
            String insertBookingQuery = "INSERT INTO Booking (CustomerID, FlightID, BookingDate, NumberOfPassengers, Status) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement bookingStatement = connection.prepareStatement(insertBookingQuery)) {
                bookingStatement.setInt(1, customerId);
                bookingStatement.setInt(2, flightId);
                bookingStatement.setString(3, formatDateTime());
                bookingStatement.setInt(4, numberOfPassengers);
                bookingStatement.setString(5, "BOOKED");
                bookingStatement.executeUpdate();
                System.out.println("Booking for customer with id: " + customerId + " completed sucessfully");
            }

            // Exercise 1.2 - update SeatsAvailable in Flights
            String checkSeatsQuery = "SELECT SeatsAvailable FROM Flight WHERE FlightID = ?";
            try (PreparedStatement checkSeatsStatement = connection.prepareStatement(checkSeatsQuery)) {
                checkSeatsStatement.setInt(1, flightId);

                try (ResultSet rs = checkSeatsStatement.executeQuery()) {
                    if (rs.next()) {
                        int seatsAvailable = rs.getInt("SeatsAvailable");

                        if (seatsAvailable >= numberOfPassengers) {
                            String updateSeatsQuery = "UPDATE Flight SET SeatsAvailable = SeatsAvailable - ? WHERE FlightID = ?";
                            try (PreparedStatement updateSeatsStatement = connection.prepareStatement(updateSeatsQuery)) {
                                updateSeatsStatement.setInt(1, numberOfPassengers);
                                updateSeatsStatement.setInt(2, flightId);
                                updateSeatsStatement.executeUpdate();
                                System.out.println("Updated seats available");
                            } catch (SQLException e) {
                                connection.rollback(savepoint);
                                throw new SQLException("Failed to update SeatsAvailable in Flights table.");
                            }
                        } else {
                            connection.rollback(savepoint);
                            throw new SQLException("Not enough available seats for booking.");
                        }
                    } else {
                        connection.rollback(savepoint);
                        throw new SQLException("Flight not found.");
                    }
                }
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback(savepoint);
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private String formatDateTime() {
        Date date = new Date();

        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return localDateTime.format(formatter);
    }
}
