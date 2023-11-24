package org.example.customer;

import java.sql.*;
import java.util.Scanner;

// Exercise 3.1 & 3.2
public class CustomerService {
    public void insertCustomer(Connection connection) throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter name: ");
        String name = sc.nextLine();
        System.out.println("Enter email: ");
        String email = sc.nextLine();
        System.out.println("Enter phone number: ");
        String phoneNumber = sc.nextLine();

        String insertQuery = "INSERT INTO Customer (Name, Email, " +
                "Phone) VALUES (?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(insertQuery)) {
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, phoneNumber);

            int rowsAffected = pst.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        }

        sc.close();
    }

    public void queryAllCustomers(Connection connection) throws SQLException {
        String selectQuery = "SELECT * FROM Customer";

        try (PreparedStatement pst = connection.prepareStatement(selectQuery);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("All Customers:");

            while (rs.next()) {
                int customerID = rs.getInt("CustomerID");
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                String phoneNumber = rs.getString("Phone");

                System.out.println("Customer ID: " + customerID + ", Name: " +
                        name + ", Email: " + email + " Phone Number: " +
                        phoneNumber + "\n");
            }
        }
    }

    public void selectCustomerByName(Connection connection, String searchName) throws SQLException {
        // accept partial search names
        String selectQuery = "SELECT * FROM Customer WHERE Name LIKE ?";

        try (PreparedStatement pst = connection.prepareStatement(selectQuery)) {
            // accept partial search names
            pst.setString(1, "%" + searchName + "%");
            ResultSet rs = pst.executeQuery();
            System.out.println("Flight with query: " + searchName);

            while (rs.next()) {
                int customerID = rs.getInt("CustomerID");
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                String phoneNumber = rs.getString("Phone");

                System.out.println("Customer ID: " + customerID + ", Name: " +
                        name + ", Email: " + email + " Phone Number: " +
                        phoneNumber + "\n");
            }
        }
    }

    public void updateCustomer(Connection connection, int customerId, String newName) throws SQLException {
        String updateQuery = "UPDATE Customer SET Name = ? WHERE CustomerID = ?";

        try (PreparedStatement pst = connection.prepareStatement(updateQuery)) {
            pst.setString(1, newName);
            pst.setInt(2, customerId);

            int rowsAffected = pst.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        }
    }

    public void deleteCustomer(Connection connection, int customerId) throws SQLException {
        String deleteQuery = "DELETE FROM Customer WHERE CustomerID = ?";

        try (PreparedStatement pst = connection.prepareStatement(deleteQuery)) {
            pst.setInt(1, customerId);

            int rowsAffected = pst.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        }
    }

    // Week 12 Day 4 - Exercise 1.2 & 2.1
    public void queryCustomersWithBookings(Connection connection) throws SQLException {
        String joinQuery = "SELECT Customer.Name AS CustomerName, Booking.BookingID, Booking.BookingDate " +
                "FROM Customer " +
                "JOIN Booking ON Customer.CustomerID = Booking.CustomerID";

        try (PreparedStatement pst = connection.prepareStatement(joinQuery);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("Customers with Bookings:");

            while (rs.next()) {
                String customerName = rs.getString("CustomerName");
                int bookingID = rs.getInt("BookingID");
                Date bookingDate = rs.getDate("BookingDate");

                System.out.println("Customer Name: " + customerName + ", Booking ID: " +
                        bookingID + ", Booking Date: " + bookingDate);
            }
        }
    }

    // Exercise 3.2
    public void queryBookingsByCustomerID(Connection connection, int customerID) throws SQLException {
        String joinQuery = "SELECT Booking.*, Flight.Airline, Flight.DepartureTime, Flight.ArrivalTime " +
                "FROM Booking " + "JOIN Flight ON Booking.FlightID = Flight.FlightID " +
                "WHERE Booking.CustomerID = ?";

        try (PreparedStatement pst = connection.prepareStatement(joinQuery)) {
            pst.setInt(1, customerID);

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("Sorry, no bookings with Customer ID: " + customerID);
                } else {
                    System.out.println("Bookings from " + customerID + " :");

                    while (rs.next()) {
                        int bookingID = rs.getInt("BookingID");
                        String flightID = rs.getString("FlightID");
                        String bookingDate = rs.getString("BookingDate");
                        String numberOfPassengers = rs.getString("NumberOfPassengers");
                        String status = rs.getString("Status");
                        String airline = rs.getString("Airline");
                        String departureTime = rs.getString("DepartureTime");
                        String arrivalTime = rs.getString("ArrivalTime");

                        System.out.println("Booking ID: " + bookingID + ", Flight ID: " +
                                flightID + ", Booking Date: " + bookingDate +
                                ", Number of Passengers: " + numberOfPassengers +
                                ", Status: " + status +
                                ", Airline: " + airline +
                                ", Departure Time: " + departureTime +
                                ", Arrival Time: " + arrivalTime);
                    }
                }
            }
        }
    }

}

