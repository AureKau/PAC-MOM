package DataBase.src;

public class MainTestRunner {

    public static GestionDB g1 = new GestionDB();
    public static void main(String[] args) {
        testCreateDatabase();
        testExecuteSqlInsertOrUpdate();
        testExecuteSqlSelect();
        testSaveNoteWithAll();
        testSaveNoteWithBook();
        testSaveNoteOnly();
        testRecupNameTableAndColumn();
    }

    public static void testCreateDatabase() {
        try {
            GestionDB gestionDB = g1;
            gestionDB.createDataBase();
            System.out.println("Test testCreateDatabase réussi.");
        } catch (Exception e) {
            System.out.println("Test testCreateDatabase échoué : " + e.getMessage());
        }
    }

    public static void testExecuteSqlInsertOrUpdate() {
        try {
            GestionDB gestionDB = g1;
            gestionDB.executeSqlInsertOrUpdate("INSERT INTO Note (titre, texte) VALUES ('valeur', 'valeur')");
            System.out.println("Test testExecuteSqlInsertOrUpdate réussi.");
        } catch (Exception e) {
            System.out.println("Test testExecuteSqlInsertOrUpdate échoué : " + e.getMessage());
        }
    }

    public static void testExecuteSqlSelect() {
        try {
            GestionDB gestionDB = g1;
            gestionDB.executeSqlSelect("SELECT * FROM note");
            System.out.println("Test testExecuteSqlSelect réussi.");
        } catch (Exception e) {
            System.out.println("Test testExecuteSqlSelect échoué : " + e.getMessage());
        }
    }

    public static void testSaveNoteWithAll() {
        try {
            GestionDB gestionDB = g1;
            gestionDB.saveNoteWithAll("Auteur3", "Livre3", "Note3", "Contenu de la note3");
            System.out.println("Test testSaveNoteWithAll réussi.");
        } catch (Exception e) {
            System.out.println("Test testSaveNoteWithAll échoué : " + e.getMessage());
        }
    }

    public static void testSaveNoteWithBook() {
        try {
            GestionDB gestionDB = g1;
            gestionDB.saveNoteWithBook("Livre4", "Note4", "Contenu de la note4");
            System.out.println("Test testSaveNoteWithBook réussi.");
        } catch (Exception e) {
            System.out.println("Test testSaveNoteWithBook échoué : " + e.getMessage());
        }
    }

    public static void testSaveNoteOnly() {
        try {
            GestionDB gestionDB = g1;
            gestionDB.saveNoteOnly("Note5", "Contenu de la note5");
            System.out.println("Test testSaveNoteOnly réussi.");
        } catch (Exception e) {
            System.out.println("Test testSaveNoteOnly échoué : " + e.getMessage());
        }
    }

    public static void testRecupNameTableAndColumn() {
        try {
            GestionDB gestionDB = g1;
            gestionDB.toString();
            System.out.println("Test testRecupNameTableAndColumn réussi.");
        } catch (Exception e) {
            System.out.println("Test testRecupNameTableAndColumn échoué : " + e.getMessage());
        }
    }
}

