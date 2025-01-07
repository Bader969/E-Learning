package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse bietet Funktionen zur Verwaltung von Kategorien.
 */
public class KategorieService {
    private final Connection verbindung;

    /**
     * Konstruktor: Erhält die Verbindung zur Datenbank.
     * @param verbindung Verbindung zur Datenbank
     */
    public KategorieService(Connection verbindung) {
        this.verbindung = verbindung;
    }

    /**
     * Fügt eine neue Kategorie hinzu.
     * @param name Name der Kategorie
     * @param elternKategorieID ID der Elternkategorie (null für Root-Kategorien)
     */
    public void kategorieHinzufuegen(String name, Integer elternKategorieID) {
        String abfrage = "INSERT INTO Kategorie (Name, ElternKategorieID) VALUES (?, ?)";

        try (PreparedStatement vorbereitung = verbindung.prepareStatement(abfrage)) {
            vorbereitung.setString(1, name);
            if (elternKategorieID == null) {
                vorbereitung.setNull(2, java.sql.Types.INTEGER);
            } else {
                vorbereitung.setInt(2, elternKategorieID);
            }

            int betroffeneZeilen = vorbereitung.executeUpdate();
            if (betroffeneZeilen > 0) {
                System.out.println("Kategorie erfolgreich hinzugefügt: " + name);
            } else {
                System.out.println("Kategorie konnte nicht hinzugefügt werden.");
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Hinzufügen der Kategorie: " + e.getMessage());
        }
    }

    /**
     * Führt eine Tiefensuche auf der Kategorie-Hierarchie aus und zeigt alle Elternkategorien iterativ an.
     * @param startKategorieID Start-ID der Kategorie
     */
    public void tiefeSuche(int startKategorieID) {
        String abfrage = "SELECT KategorieID, Name, ElternKategorieID FROM Kategorie WHERE KategorieID = ?";
        List<String> elternKategorien = new ArrayList<>();

        try {
            int aktuelleKategorieID = startKategorieID;

            // Iterative Verarbeitung der Hierarchie
            while (aktuelleKategorieID != 0) {
                try (PreparedStatement vorbereitung = verbindung.prepareStatement(abfrage)) {
                    vorbereitung.setInt(1, aktuelleKategorieID);

                    try (ResultSet ergebnisMenge = vorbereitung.executeQuery()) {
                        if (ergebnisMenge.next()) {
                            String name = ergebnisMenge.getString("Name");
                            aktuelleKategorieID = ergebnisMenge.getInt("ElternKategorieID");
                            elternKategorien.add(name);
                        } else {
                            break; // Keine weiteren Elternkategorien gefunden
                        }
                    }
                }
            }

            // Umgekehrte Reihenfolge anzeigen: Wurzel -> Eltern -> Startkategorie
            System.out.println(String.join(" -> ", elternKategorien));

        } catch (SQLException e) {
            System.err.println("Fehler beim Durchsuchen der Elternkategorien: " + e.getMessage());
        }
    }



}
