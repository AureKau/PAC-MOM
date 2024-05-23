package note;

import DataBase.src.GestionDB;

import java.sql.Date;
import java.sql.SQLException;

public class GestionNote {
    private final GestionDB gestionDB = new GestionDB();

    public INote creationNote(boolean supprimer_note, Date date_note, String texte_note, int id_note, String titre_note, String titre_livre, int id_titre, int id_auteur, String auteur){
        Note note = new Note(supprimer_note, date_note, texte_note, id_note, titre_note, titre_livre, id_titre, id_auteur, auteur);
        return note;

    }

    // Méthode pour sauvegarder les variables de la note dans la base de données
    public void sauvegarderNote(INote note) throws SQLException, ClassNotFoundException {
        // Vérifiez si l'ID de la note est déjà défini
        if (note.getId_note() == 0) {
            gestionDB.saveNoteWithAll(note.getAuteur(), note.getTitre_livre(), note.getTitre_note(), note.getTexte_note());
        } else {

            gestionDB.executeSqlInsertOrUpdate("UPDATE Note SET titre = '"
                    + note.getTitre_note() + "', texte = '"
                    + note.getTexte_note() + "' WHERE id_note = " + note.getId_note());
        }
    }
}
