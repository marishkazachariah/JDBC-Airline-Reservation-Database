package org.example;

import org.example.customer.CustomerService;
import org.example.flight.FlightService;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        // Week 12 Day 3
        // Exercise 1.1 & 1.2
        Properties prop = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            // Load a properties file from class path, inside static method
            prop.load(input);

            // Get values from properties file
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                if (connection != null) {
                    System.out.println("Connected");

                    // Exercise 2.3
                    FlightService flightService = new FlightService();
                    flightService.insertFlight(connection);
                    flightService.queryAllFlights(connection);
                    flightService.updateFlight(connection, 2, 324.32);
                    flightService.deleteFlight(connection, 3);
                    flightService.selectFlightByID(connection, 2);

                    // Exercise 3.3
                    CustomerService customerService = new CustomerService();
                    customerService.insertCustomer(connection);
                    customerService.queryAllCustomers(connection);
                    customerService.selectCustomerByName(connection, "Jane");
                    customerService.updateCustomer(connection, 1, "John Smith");
                    customerService.deleteCustomer(connection, 3);
                    customerService.queryAllCustomers(connection);

                    // Week 12 Day 4
                    // Exercise 1.3
                    customerService.queryCustomersWithBookings(connection);

                    // Exercise 2.3
                    flightService.queryFlightByOrigin(connection, "CityA");
                    flightService.queryFlightsByDate(connection, "2023-11-20");

                    // Exercise 3.3
                    customerService.queryBookingsByCustomerID(connection, 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}