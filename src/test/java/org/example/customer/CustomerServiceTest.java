package org.example.customer;

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
class CustomerServiceTest {
    @Test
    void testQueryCustomersWithBookings() {
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
                CustomerService customerService = new CustomerService();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                System.setOut(new PrintStream(outputStream));

                customerService.queryCustomersWithBookings(connection);

                System.setOut(System.out);

                String output = outputStream.toString().trim();

                assertTrue(output.contains("Customers with Bookings:"));
                assertTrue(output.contains("Customer Name:"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testQueryBookingsByCustomerID() {
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
                CustomerService customerService = new CustomerService();

                int testCustomerID = 1;

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                System.setOut(new PrintStream(outputStream));

                customerService.queryBookingsByCustomerID(connection, testCustomerID);

                System.setOut(System.out);

                String output = outputStream.toString().trim();

                if (output.contains("Sorry, no bookings with Customer ID:")) {
                    assertTrue(output.contains("Sorry, no bookings with Customer ID: " + testCustomerID));
                } else {
                    assertTrue(output.contains("Bookings from " + testCustomerID));
                    assertTrue(output.contains("Booking ID:"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
