package Util;

import java.sql.*;

public class ConnectionUtil {
    private static final String Postgre_URL = "jdbc:postgresql://localhost:5432/clashofclans1d";
    private static final String Postgre_User = "postgres";
    private static final String Postgre_PW = "p0";
    private static Connection connection;

    public static Connection GetConnection(){
        if (connection == null){
            while (connection == null){
                try {
                    IO.println("Attempting to connect to the database...");
                    connection = DriverManager.getConnection(Postgre_URL, Postgre_User, Postgre_PW);
                } catch (Exception e1){
                    IO.println("Failed to connect to the database. Trying again...: " + e1);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e2) {
                        IO.println("Sleep to connect to the database likely interrupted: " + e2);
                    }
                }
            }

            IO.println("Connected to the database!");
        }

        return connection;
    }

    public static void CloseConnection() {
        if (connection != null){
            try {
                connection.close();
                IO.println("Database connection closed.");
            } catch (Exception e) {
                IO.println("Failed to close the connection properly.\nMay automatically close anyway when application terminates: " + e);
            }
        }
    }
}
