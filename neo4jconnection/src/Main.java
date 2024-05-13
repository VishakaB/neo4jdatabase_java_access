//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.jdbc.http.HttpDriver;
import java.sql.SQLException;
import org.neo4j.driver.v1.*;
import java.sql.*;
import java.sql.Statement;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Main {
    public static void main(String[] args) {
        // Maximum number of retry attempts
        int maxRetryAttempts = 3;
        // Delay between retry attempts (in milliseconds)
        long retryDelay = 1000; // 1 second

        // Neo4j HTTP connection URL
        String url = "jdbc:neo4j:http://localhost:7474";
        String username = "neo4j";
        String password = "dff8fkGG2";

        // Load Neo4j JDBC driver
        try {
            Class.forName("org.neo4j.jdbc.http.HttpDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Retry logic
        for (int attempt = 1; attempt <= maxRetryAttempts; attempt++) {
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                // Your code for interacting with the database goes here
                String query = "MATCH (n) RETURN n LIMIT 25";
                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(query)) {
                    while (resultSet.next()) {
                        // Process the result set
                        System.out.println(resultSet.getString("n"));
                    }
                }
                break; // Exit the loop if successful
            } catch (SQLException e) {
                System.out.println("Connection attempt " + attempt + " failed. Retrying...");
                if (attempt == maxRetryAttempts) {
                    // Max retry attempts reached, print error and exit
                    System.out.println("Max retry attempts reached. Unable to establish connection.");
                    e.printStackTrace();
                    System.exit(1);
                }
                // Wait for the specified delay before retrying
                try {
                    Thread.sleep(retryDelay);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

            }
        }
    }
}
