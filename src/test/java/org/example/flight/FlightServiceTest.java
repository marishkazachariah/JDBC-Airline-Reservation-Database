package org.example.flight;

import org.example.Main;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class FlightServiceTest {
    @Test
    void testQueryFlightsByPartialDate() throws SQLException {
        Properties prop = new Properties();

        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            prop.load(input);

            // Get values from properties file
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                FlightService flightService = new FlightService();

                String date = "2023-11-20";

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                System.setOut(new PrintStream(outputStream));

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
}
