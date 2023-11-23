package org.example.flight;

import java.sql.*;
import java.util.Scanner;

// Exercise 2.1 & 2.2
public class FlightService {
    public void insertFlight(Connection connection) throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter airline: ");
        String airline = sc.nextLine();
        System.out.println("Enter the origin city: ");
        String origin = sc.nextLine();
        System.out.println("Enter the destination city: ");
        String destination = sc.nextLine();
        System.out.println("Enter the departure time in DATETIME format (i.e. 2023-11-20 10:00:00): ");
        String departure = sc.nextLine();
        System.out.println("Enter the arrival time in DATETIME format (i.e. 2023-11-20 10:00:00): ");
        String arrival = sc.nextLine();
        System.out.println("Enter the price:");
        double price = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter the amount of seats available:");
        int seatsAvailable = sc.nextInt();
        sc.nextLine();

        String insertQuery = "INSERT INTO Flight (Airline, Origin, " +
                "Destination, DepartureTime, ArrivalTime, Price, " +
                "SeatsAvailable) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(insertQuery)) {
            pst.setString(1, airline);
            pst.setString(2, origin);
            pst.setString(3, destination);
            pst.setString(4, departure);
            pst.setString(5, arrival);
            pst.setDouble(6, price);
            pst.setInt(7, seatsAvailable);

            int rowsAffected = pst.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        }

        sc.close();
    }

    public void queryAllFlights(Connection connection) throws SQLException {
        String selectQuery = "SELECT * FROM Flight";

        try (PreparedStatement pst = connection.prepareStatement(selectQuery);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("All Flights:");

            while (rs.next()) {
                int flightID = rs.getInt("FlightID");
                String airline = rs.getString("Airline");
                String origin = rs.getString("Origin");
                String destination = rs.getString("Destination");
                String departureTime = rs.getString("DepartureTime");
                String arrivalTime = rs.getString("ArrivalTime");
                double price = rs.getDouble("Price");
                double seatsAvailable = rs.getInt("SeatsAvailable");


                System.out.println("Flight ID: " + flightID + ", Airline: " +
                        airline + ", Origin: " + origin + " Destination: " +
                        destination +  " Departure Time: " + departureTime +
                        " Arrival Time: " + arrivalTime + " Price: " + price +
                        " Seats Available: " + seatsAvailable + "\n");
            }
        }
    }

    public void selectFlightByID(Connection connection, int searchID) throws SQLException {
        String selectQuery = "SELECT * FROM Flight WHERE FlightId = ?";

        try (PreparedStatement pst = connection.prepareStatement(selectQuery)) {
             pst.setInt(1, searchID);
             ResultSet rs = pst.executeQuery();
            System.out.println("Flight with query: " + searchID);

            while (rs.next()) {
                int flightID = rs.getInt("FlightID");
                String airline = rs.getString("Airline");
                String origin = rs.getString("Origin");
                String destination = rs.getString("Destination");
                String departureTime = rs.getString("DepartureTime");
                String arrivalTime = rs.getString("ArrivalTime");
                double price = rs.getDouble("Price");
                double seatsAvailable = rs.getInt("SeatsAvailable");


                System.out.println("Flight ID: " + flightID + ", Airline: " +
                        airline + ", Origin: " + origin + " Destination: " +
                        destination +  " Departure Time: " + departureTime +
                        " Arrival Time: " + arrivalTime + " Price: " + price +
                        " Seats Available: " + seatsAvailable + "\n");
            }
        }
    }

    public void updateFlight(Connection connection, int flightId, double newPrice) throws SQLException {
        String updateQuery = "UPDATE Flight SET Price = ? WHERE FlightId = ?";

        try (PreparedStatement pst = connection.prepareStatement(updateQuery)) {
            pst.setDouble(1, newPrice);
            pst.setInt(2, flightId);

            int rowsAffected = pst.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        }
    }

    public void deleteFlight(Connection connection, int flightId) throws SQLException {
        String deleteQuery = "DELETE FROM Flight WHERE FlightId = ?";

        try (PreparedStatement pst = connection.prepareStatement(deleteQuery)) {
            pst.setInt(1, flightId);

            int rowsAffected = pst.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        }
    }

    // Week 12 Day 4 - Exercise 2.2
    public void queryFlightByOrigin(Connection connection, String airportOrigin) throws SQLException {
        String joinQuery = "SELECT * FROM Flight WHERE Origin = ?";

        try (PreparedStatement pst = connection.prepareStatement(joinQuery)) {
            pst.setString(1, airportOrigin);

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("Sorry, no flights from " + airportOrigin);
                } else {
                    System.out.println("Flights from " + airportOrigin + " :");

                    while (rs.next()) {
                        int flightID = rs.getInt("FlightID");
                        String airline = rs.getString("Airline");
                        String origin = rs.getString("Origin");
                        String destination = rs.getString("Destination");
                        String departureTime = rs.getString("DepartureTime");
                        String arrivalTime = rs.getString("ArrivalTime");
                        double price = rs.getDouble("Price");
                        int seatsAvailable = rs.getInt("SeatsAvailable");

                        System.out.println("Flight ID: " + flightID + ", Airline: " +
                                airline + ", Origin: " + origin + ", Destination: " + destination +
                                ", Departure Time: " + departureTime + ", Arrival Time: " + arrivalTime +
                                ", Price: " + price + ", Seats Available: " + seatsAvailable);
                    }
                }
            }
        }
    }

    public void queryFlightsByDate(Connection connection, String date) throws SQLException {
        String partialDateQuery = "SELECT * FROM Flight WHERE DATE(DepartureTime) LIKE ?";

        try (PreparedStatement pst = connection.prepareStatement(partialDateQuery)) {
            pst.setString(1, date + "%"); // Appending '%' to perform a partial match

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("Sorry, no flights on " + date);
                } else {
                    System.out.println("Flights on " + date + " :");

                    while (rs.next()) {
                        int flightID = rs.getInt("FlightID");
                        String airline = rs.getString("Airline");
                        String origin = rs.getString("Origin");
                        String destination = rs.getString("Destination");
                        String departureTime = rs.getString("DepartureTime");
                        String arrivalTime = rs.getString("ArrivalTime");
                        double price = rs.getDouble("Price");
                        int seatsAvailable = rs.getInt("SeatsAvailable");

                        System.out.println("Flight ID: " + flightID + ", Airline: " +
                                airline + ", Origin: " + origin + ", Destination: " + destination +
                                ", Departure Time: " + departureTime + ", Arrival Time: " + arrivalTime +
                                ", Price: " + price + ", Seats Available: " + seatsAvailable);
                    }
                }
            }
        }
    }

}

