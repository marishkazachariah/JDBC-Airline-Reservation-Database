package org.example;

import org.example.customer.CustomerService;
import org.example.flight.FlightService;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class TransactionsExercise {
    // Musie's exercise
    public static void main(String[] args) throws IOException {
        Connection connection = null;
        Properties prop = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            prop.load(input);
            String url = prop.getProperty("db.urltwo");
            String user = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(url, user, password);

                if (connection != null) {
                    System.out.println("Connected");
                    connection.setAutoCommit(false);

                    insertCourse(connection, "Math", "Arithmetics");
                    insertTheme(connection, "Bachelors", 1);

                    connection.setAutoCommit(true);
                    System.out.println("Transaction committed successfully.");
                }

            } catch (ClassNotFoundException | SQLException e) {
                try {
                    if (connection != null) {
                        connection.rollback();
                    }
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }

                e.printStackTrace();

            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException closeException) {
                    closeException.printStackTrace();
                }
            }
        }
    }

    private static int insertCourse(Connection connection, String courseName, String courseDescription) throws SQLException {
        String insertCourseSQL = "INSERT INTO Courses (CourseName, CourseDescription) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertCourseSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, courseName);
            preparedStatement.setString(2, courseDescription);
            preparedStatement.executeUpdate();

            int courseId = -1;
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                courseId = generatedKeys.getInt(1);
            }

            return courseId;
        }
    }

    private static void insertTheme(Connection connection, String themeName, int courseId) throws SQLException {
        String insertThemeSQL = "INSERT INTO Themes (ThemeName, CourseID) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertThemeSQL)) {
            preparedStatement.setString(1, themeName);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
        }
    }
}
