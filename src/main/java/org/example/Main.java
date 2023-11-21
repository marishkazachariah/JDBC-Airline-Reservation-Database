package org.example;

import org.example.flight.FlightService;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}