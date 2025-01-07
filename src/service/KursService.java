package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Diese Klasse bietet Funktionen zur Verwaltung von Kursen.
 */
public class KursService {
    private final Connection verbindung;

    /**
     * Konstruktor: Erhält die Verbindung zur Datenbank.
     * @param verbindung Verbindung zur Datenbank
     */
    public KursService(Connection verbindung) {
        this.verbindung = verbindung;
    }

    /**
     * Zeigt alle Kurse an, die ein Student gekauft hat, und deren Status.
     * @param studentenID ID des Studenten
     */
    public void findeGekaufteKurseEinesStudenten(int studentenID) {
        String abfrage = "SELECT k.KursID, k.Titel, " +
                "CASE WHEN ka.Fortschritt = 100 THEN 'Abgeschlossen' ELSE 'Nicht abgeschlossen' END AS Status " +
                "FROM Bestellung b " +
                "JOIN Kurs k ON b.KursID = k.KursID " +
                "LEFT JOIN Kurs_Anmeldung ka ON b.KursID = ka.KursID AND b.StudentID = ka.StudentID " +
                "WHERE b.StudentID = ?";

        try (PreparedStatement vorbereitung = verbindung.prepareStatement(abfrage)) {
            vorbereitung.setInt(1, studentenID);

            try (ResultSet ergebnisMenge = vorbereitung.executeQuery()) {
                while (ergebnisMenge.next()) {
                    System.out.printf("KursID: %d, Titel: %s, Status: %s\n",
                            ergebnisMenge.getInt("KursID"),
                            ergebnisMenge.getString("Titel"),
                            ergebnisMenge.getString("Status"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler bei der Abfrage der gekauften Kurse: " + e.getMessage());
        }
    }

    /**
     * Zeigt die durchschnittliche Bewertung eines Kurses an.
     * @param kursID ID des Kurses
     */
    public void kursDurchschnittlicheBewertung(int kursID) {
        String abfrage = "SELECT k.Titel, AVG(be.Bewertung) AS Durchschnittsbewertung " +
                "FROM Bewertung be " +
                "JOIN Kurs k ON be.KursID = k.KursID " +
                "WHERE k.KursID = ? " +
                "GROUP BY k.Titel";

        try (PreparedStatement vorbereitung = verbindung.prepareStatement(abfrage)) {
            vorbereitung.setInt(1, kursID);

            try (ResultSet ergebnisMenge = vorbereitung.executeQuery()) {
                if (ergebnisMenge.next()) {
                    System.out.printf("Titel: %s, Durchschnittsbewertung: %.2f\n",
                            ergebnisMenge.getString("Titel"),
                            ergebnisMenge.getDouble("Durchschnittsbewertung"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler bei der Abfrage der Durchschnittsbewertung: " + e.getMessage());
        }
    }

    /**
     * Findet alle Kurse in der Kategorie Mathematik und ihren Unterkategorien.
     */
    public void findeAlleKurseInnerhalbDerKategorieMathematik() {
        String abfrage = "WITH Kategorie_Hierarchie (KategorieID, Name, ElternKategorieID) AS (" +
                "    SELECT KategorieID, Name, ElternKategorieID " +
                "    FROM Kategorie " +
                "    WHERE KategorieID = 21 " +
                "    UNION ALL " +
                "    SELECT k.KategorieID, k.Name, k.ElternKategorieID " +
                "    FROM Kategorie k " +
                "    INNER JOIN Kategorie_Hierarchie kh ON k.ElternKategorieID = kh.KategorieID " +
                ") " +
                "SELECT k.Titel FROM Kurs k WHERE k.KategorieID IN (SELECT KategorieID FROM Kategorie_Hierarchie)";

        try (PreparedStatement vorbereitung = verbindung.prepareStatement(abfrage);
             ResultSet ergebnisMenge = vorbereitung.executeQuery()) {

            System.out.println("Kurse in der Kategorie Mathematik und Unterkategorien:");
            while (ergebnisMenge.next()) {
                System.out.println("Kurs Titel: " + ergebnisMenge.getString("Titel"));
            }
        } catch (SQLException fehler) {
            System.err.println("Fehler bei der Abfrage der Kurse in Mathematik: " + fehler.getMessage());
        }
    }


    /**
     * Zeigt alle Lernmaterialien eines Kurses und den Fortschritt eines Studenten an.
     * @param kursID ID des Kurses
     * @param studentenID ID des Studenten
     */
    public void zeigeAlleLernmaterialienEinesKurses(int kursID, int studentenID) {
        String abfrage = "SELECT lm.Titel AS LernmaterialTitel, lm.Typ AS MaterialTyp, ka.Fortschritt AS StudentenFortschritt " +
                "FROM Lernmaterial lm " +
                "JOIN Kurs k ON lm.KursID = k.KursID " +
                "LEFT JOIN Kurs_Anmeldung ka ON k.KursID = ka.KursID AND ka.StudentID = ? " +
                "WHERE k.KursID = ?";

        try (PreparedStatement vorbereitung = verbindung.prepareStatement(abfrage)) {
            vorbereitung.setInt(1, studentenID);
            vorbereitung.setInt(2, kursID);

            try (ResultSet ergebnisMenge = vorbereitung.executeQuery()) {
                System.out.println("Lernmaterialien des Kurses:");
                while (ergebnisMenge.next()) {
                    System.out.printf("Titel: %s, Typ: %s, Fortschritt: %d%%\n",
                            ergebnisMenge.getString("LernmaterialTitel"),
                            ergebnisMenge.getString("MaterialTyp"),
                            ergebnisMenge.getInt("StudentenFortschritt"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler bei der Abfrage der Lernmaterialien: " + e.getMessage());
        }
    }
    /**
     * Zeigt die Statistiken aller Kurse an, einschließlich Bewertungen, Anmeldungen und Zertifikate.
     */
    public void zeigeKursStatistiken() {
        String abfrage = "SELECT k.KursID, k.Titel AS KursTitel, u.Name AS LehrerName, " +
                "AVG(be.Bewertung) AS Durchschnittsbewertung, " +
                "COUNT(DISTINCT ka.StudentID) AS AnzahlAnmeldungen, " +
                "COUNT(DISTINCT z.ZertifikatID) AS AnzahlZertifikate " +
                "FROM Kurs k " +
                "JOIN Benutzer u ON k.LehrerID = u.BenutzerID " +
                "LEFT JOIN Bewertung be ON k.KursID = be.KursID " +
                "LEFT JOIN Kurs_Anmeldung ka ON k.KursID = ka.KursID " +
                "LEFT JOIN Zertifikat z ON k.KursID = z.KursID AND z.StudentID = ka.StudentID " +
                "GROUP BY k.KursID, k.Titel, u.Name " +
                "ORDER BY Durchschnittsbewertung DESC";

        try (PreparedStatement vorbereitung = verbindung.prepareStatement(abfrage);
             ResultSet ergebnisMenge = vorbereitung.executeQuery()) {

            System.out.printf("%-10s %-20s %-20s %-15s %-10s %-10s\n",
                    "KursID", "Titel", "Lehrer", "Durchschnitt", "Anmeldungen", "Zertifikate");
            System.out.println("-----------------------------------------------------------------------------------------");

            while (ergebnisMenge.next()) {
                System.out.printf("%-10d %-20s %-20s %-15.2f %-10d %-10d\n",
                        ergebnisMenge.getInt("KursID"),
                        ergebnisMenge.getString("KursTitel"),
                        ergebnisMenge.getString("LehrerName"),
                        ergebnisMenge.getDouble("Durchschnittsbewertung"),
                        ergebnisMenge.getInt("AnzahlAnmeldungen"),
                        ergebnisMenge.getInt("AnzahlZertifikate"));
            }
        } catch (SQLException fehler) {
            System.err.println("Fehler bei der Abfrage der Kursstatistiken: " + fehler.getMessage());
        }
    }
}
