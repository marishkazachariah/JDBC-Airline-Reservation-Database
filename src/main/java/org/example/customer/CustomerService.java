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
}

