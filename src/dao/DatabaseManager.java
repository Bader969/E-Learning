package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Die Klasse verwaltet die Verbindung zur Datenbank.
 */
public class DatabaseManager {
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521/xepdb1";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "******";

    private Connection verbindung;

    /**
     * Konstruktor: Initialisiert die Datenbankverbindung.
     */
    public DatabaseManager() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            this.verbindung = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            System.out.println("Datenbankverbindung erfolgreich hergestellt.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Fehler bei der Datenbankverbindung: " + e.getMessage());
        }
    }

    /**
     * Gibt die aktuelle Verbindung zurück.
     * @return Verbindung zur Datenbank
     */
    public Connection getConnection() {
        return verbindung;
    }

    /**
     * Schließt die Verbindung zur Datenbank.
     */
    public void closeConnection() {
        if (verbindung != null) {
            try {
                verbindung.close();
                System.out.println("Datenbankverbindung erfolgreich geschlossen.");
            } catch (SQLException e) {
                System.err.println("Fehler beim Schließen der Verbindung: " + e.getMessage());
            }
        }
    }
}
