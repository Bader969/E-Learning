import dao.DatabaseManager;
import ui.ConsoleUI;

/**
 * Die Hauptklasse der Anwendung. Sie initialisiert die Verbindung zur Datenbank und startet die Benutzeroberfläche.
 */
public class MainApp {
    public static void main(String[] args) {
        // Erstellen des DatabaseManager zur Verwaltung der Verbindung
        DatabaseManager databaseManager = new DatabaseManager();

        try {
            // Starten der Benutzeroberfläche mit der Datenbankverbindung
            ConsoleUI consoleUI = new ConsoleUI(databaseManager.getConnection());
            consoleUI.startMenue();
        } finally {
            // Sicherstellen, dass die Verbindung nach Beendigung geschlossen wird
            databaseManager.closeConnection();
        }
    }
}
