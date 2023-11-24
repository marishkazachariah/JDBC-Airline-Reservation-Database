package org.example.flight;

import org.example.Main;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

// Exercise 4.1
class FlightServiceTest {
    @Test
    void testQueryFlightsByDate() {
        Properties prop = new Properties();

        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            prop.load(input);

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                FlightService flightService = new FlightService();

                String date = "2023-11-20";

                // https://stackoverflow.com/questions/8708342/redirect-console-output-to-string-in-java
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                // Anything written to System.out will be redirected to outputStream
                System.setOut(new PrintStream(outputStream));
                // Resets the standard output to its original value
                flightService.queryFlightsByDate(connection, date);

                System.setOut(System.out);

                String output = outputStream.toString().trim();

                if (output.contains("Sorry, no flights on")) {
                    assertTrue(output.contains("Sorry, no flights on " + date));
                } else {
                    assertTrue(output.contains("Flights on " + date));
                    assertTrue(output.contains("Flight ID:"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testQueryFlightByOrigin() {
        Properties prop = new Properties();

        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            prop.load(input);

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                FlightService flightService = new FlightService();

                String origin = "CityA";

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                System.setOut(new PrintStream(outputStream));

                flightService.queryFlightByOrigin(connection, origin);

                System.setOut(System.out);

                String output = outputStream.toString().trim();

                if (output.contains("Sorry, no flights from")) {
                    assertTrue(output.contains("Sorry, no flights from " + origin));
                } else {
                    assertTrue(output.contains("Flights from " + origin));
                    assertTrue(output.contains("Flight ID:"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
