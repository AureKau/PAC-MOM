package note;

import DataBase.src.GestionDB;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionNote {
    public final GestionDB gestionDB = new GestionDB();

    public INote creationNote(String texte_note, String titre_note){
        System.out.println(gestionDB);
        return new Note(texte_note, titre_note);

    }

    public INote getNoteFromDb(String titre_note) throws SQLException, ClassNotFoundException {
        ResultSet set = gestionDB.executeSqlSelect("SELECT * from note where titre = \"" + titre_note + "\";");
        return new Note(set.getBoolean("supprimee"), set.getDate("date_creation"),
                set.getString("texte"), set.getInt("id_Note"), set.getString("titre"));

    }

    // Méthode pour sauvegarder les variables de la note dans la base de données
    public void sauvegarderNote(INote note) throws SQLException, ClassNotFoundException {
        // Vérifiez si l'ID de la note est déjà défini
        if (note.getId_note() == 0) {
            if (note.getAuteur() != null && note.getTitre_note() != null) {
                gestionDB.saveNoteWithAll(note.getAuteur(), note.getTitre_livre(), note.getTitre_note(), note.getTexte_note());
            } else if (note.getTitre_livre() != null) {
                gestionDB.saveNoteWithBook(note.getTitre_livre(), note.getTitre_note(), note.getTexte_note());

            } else {
                gestionDB.saveNoteOnly(note.getTitre_note(), note.getTexte_note());
            }

        } else {

            gestionDB.executeSqlInsertOrUpdate("UPDATE Note SET titre = '"
                    + note.getTitre_note() + "', texte = '"
                    + note.getTexte_note() + "' WHERE id_note = " + note.getId_note());
        }
    }

    public void updateNote(INote note) throws NoteException, SQLException, ClassNotFoundException {
        if (note.getTexte_note() == null || note.getTexte_note() == null) throw new NoteException();
        String command = "UPDATE note SET titre = \"" + note.getTitre_note() + "\", texte = \"" + note.getTexte_note() + "\"";
        command += ", supprimee = " + note.isSupprimer_note();
        command += " WHERE id_note = " + note.getId_note() + ";";
        gestionDB.executeSqlInsertOrUpdate(command);
        if (note.getTitre_livre() != null){
            addBookToNote(note, note.getTitre_livre());
        }
        gestionDB.executeSelectAllTable();
    }

    public void addBookToNote(INote note, String titreLivre) throws SQLException, ClassNotFoundException {
        String command = "SELECT * FROM livre WHERE id_livre = (SELECT id_livre from livre WHERE titre = \"" + titreLivre + "\");";
        if (gestionDB.executeSqlSelect(command).getFetchSize() <= 0){
            addBook(titreLivre);
            if (note.getAuteur() != null){
                addAuteurToBook(titreLivre, note.getAuteur());
            }
        }
        gestionDB.executeSqlInsertOrUpdate("UPDATE note SET id_livre = (SELECT id_livre from livre WHERE titre = \"" + titreLivre + "\") WHERE id_note = " + note.getId_note() +";");

    }


    public void addBook(String titreLivre) throws SQLException, ClassNotFoundException {
        gestionDB.executeSqlInsertOrUpdate("INSERT INTO livre(titre) VALUES(\"" + titreLivre + "\");");

    }

    public void addAuteurToBook(String titreLivre, String nomAuteur) throws SQLException, ClassNotFoundException {
        String command = "SELECT * FROM auteur WHERE id_auteur = (SELECT id_auteur from auteur WHERE nom_auteur = \"" + nomAuteur + "\");";
        if (gestionDB.executeSqlSelect(command).getFetchSize() <= 0){
            addAuteur(nomAuteur);
        }
        gestionDB.executeSqlInsertOrUpdate("UPDATE livre SET id_auteur = (SELECT id_auteur from auteur WHERE nom_auteur = \"" + nomAuteur + "\");");


    }


    public void addAuteur(String nomAuteur) throws SQLException, ClassNotFoundException {
        gestionDB.executeSqlInsertOrUpdate("INSERT INTO auteur(nom_auteur) VALUES(\"" + nomAuteur + "\");");
    }
}
