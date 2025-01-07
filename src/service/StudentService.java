package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Diese Klasse bietet Funktionen zur Verwaltung von Studenten.
 */
public class StudentService {
    private final Connection verbindung;

    /**
     * Konstruktor: Erhält die Verbindung zur Datenbank.
     * @param verbindung Verbindung zur Datenbank
     */
    public StudentService(Connection verbindung) {
        this.verbindung = verbindung;
    }

    /**
     * Zeigt alle Studenten mit einem Zertifikat für einen bestimmten Kurs an.
     * @param kursID ID des Kurses
     */
    public void findeStudentenMitZertifikat(int kursID) {
        String abfrage = "SELECT b.Name AS StudentenName, b.E_Mail AS StudentenEmail " +
                "FROM Zertifikat z " +
                "JOIN Benutzer b ON z.StudentID = b.BenutzerID " +
                "WHERE z.KursID = ?";

        try (PreparedStatement vorbereitung = verbindung.prepareStatement(abfrage)) {
            vorbereitung.setInt(1, kursID);

            try (ResultSet ergebnisMenge = vorbereitung.executeQuery()) {
                System.out.println("Studenten mit Zertifikat:");
                while (ergebnisMenge.next()) {
                    System.out.printf("Name: %s, E-Mail: %s\n",
                            ergebnisMenge.getString("StudentenName"),
                            ergebnisMenge.getString("StudentenEmail"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler bei der Abfrage der Studenten mit Zertifikat: " + e.getMessage());
        }
    }
}
