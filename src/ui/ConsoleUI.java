package ui;

import service.KursService;
import service.StudentService;
import service.KategorieService;

import java.sql.Connection;
import java.util.Scanner;

/**
 * Diese Klasse stellt eine textbasierte Benutzeroberfläche zur Verwaltung des Systems bereit.
 */
public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private final StudentService studentenService;
    private final KursService kursService;
    private final KategorieService kategorieService;

    /**
     * Konstruktor: Initialisiert die Benutzeroberfläche mit den entsprechenden Services.
     * @param verbindung Verbindung zur Datenbank
     */
    public ConsoleUI(Connection verbindung) {
        this.studentenService = new StudentService(verbindung);
        this.kursService = new KursService(verbindung);
        this.kategorieService = new KategorieService(verbindung);
    }

    /**
     * Startet das Hauptmenü.
     */
    public void startMenue() {
        while (true) {
            System.out.println("----- LearnSphere Menü -----");
            System.out.println("1. Zeige alle Kurse, die ein Student gekauft hat, und ihren Status.");
            System.out.println("2. Zeige die durchschnittliche Bewertung eines Kurses.");
            System.out.println("3. Finde alle Kurse in der Kategorie 'Mathematik' und ihren Unterkategorien.");
            System.out.println("4. Zeige alle Lernmaterialien eines Kurses und den Fortschritt eines Studenten.");
            System.out.println("5. Zeige alle Studenten mit einem Zertifikat für einen Kurs.");
            System.out.println("6. Zeige Kurs Statistiken.");
            System.out.println("7. Kategorie hinzufügen.");
            System.out.println("8. Kategorie-Hierarchie durchsuchen.");
            System.out.println("0. Beenden.");
            System.out.print("Wählen Sie eine Option: ");

            int auswahl = scanner.nextInt();
            switch (auswahl) {
                case 1 -> zeigeGekaufteKurse();
                case 2 -> zeigeDurchschnittsbewertung();
                case 3 -> findeKurseInMathematik();
                case 4 -> zeigeLernmaterialien();
                case 5 -> zeigeStudentenMitZertifikat();
                case 6 -> kursService.zeigeKursStatistiken();
                case 7 -> kategorieHinzufuegen();
                case 8 -> kategorieHierarchieDurchsuchen();
                case 0 -> {
                    System.out.println("Programm beendet.");
                    return;
                }
                default -> System.out.println("Ungültige Auswahl.");
            }
        }
    }

    private void zeigeGekaufteKurse() {
        System.out.print("Geben Sie die Studenten-ID ein: ");
        int studentenID = scanner.nextInt();
        kursService.findeGekaufteKurseEinesStudenten(studentenID);
    }

    private void zeigeDurchschnittsbewertung() {
        System.out.print("Geben Sie die Kurs-ID ein: ");
        int kursID = scanner.nextInt();
        kursService.kursDurchschnittlicheBewertung(kursID);
    }

    private void findeKurseInMathematik() {
        kursService.findeAlleKurseInnerhalbDerKategorieMathematik();
    }

    private void zeigeLernmaterialien() {
        System.out.print("Geben Sie die Kurs-ID ein: ");
        int kursID = scanner.nextInt();
        System.out.print("Geben Sie die Studenten-ID ein: ");
        int studentenID = scanner.nextInt();
        kursService.zeigeAlleLernmaterialienEinesKurses(kursID, studentenID);
    }

    private void zeigeStudentenMitZertifikat() {
        System.out.print("Geben Sie die Kurs-ID ein: ");
        int kursID = scanner.nextInt();
        studentenService.findeStudentenMitZertifikat(kursID);
    }

    private void kategorieHinzufuegen() {
        System.out.print("Geben Sie den Namen der neuen Kategorie ein: ");
        scanner.nextLine(); // Zeilenumbruch verarbeiten
        String name = scanner.nextLine();
        System.out.print("Geben Sie die Eltern-Kategorie-ID ein (oder 0 für keine): ");
        int elternID = scanner.nextInt();
        Integer elternKategorieID = (elternID == 0) ? null : elternID;
        kategorieService.kategorieHinzufuegen(name, elternKategorieID);
    }

    private void kategorieHierarchieDurchsuchen() {
        System.out.print("Geben Sie die Start-Kategorie-ID ein: ");
        int startKategorieID = scanner.nextInt();
        kategorieService.tiefeSuche(startKategorieID);
    }
}
