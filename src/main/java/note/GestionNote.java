package note;



import DataBase.src.GestionDB;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class GestionNote {
    public final GestionDB gestionDB = GestionDB.getInstance();

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
    public void sauvegarderNote(INote note) throws SQLException, ClassNotFoundException, NoteException {
        if (note.isSupprimer_note()) throw new NoteException("La note est dans la corbeille");
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

    public Set<INote> recupAllNote() throws SQLException, ClassNotFoundException {
        Set<INote> notes = new HashSet<>();
        ResultSet resultSet = gestionDB.executeSelectAllTable();
        while (resultSet.next()) {
            // Récupération des données de la base de données pour chaque note
            int idAuteur = resultSet.getInt("id_auteur");
            String auteur = resultSet.getString("nom_auteur");
            int idTitre = resultSet.getInt("id_livre");
            String titreLivre = resultSet.getString("titre");
            int idNote = resultSet.getInt("id_note");
            String titreNote = resultSet.getString("titre");
            String texteNote = resultSet.getString("texte");
            Date dateNote = resultSet.getDate("date_creation");
            boolean supprimerNote = resultSet.getBoolean("supprimee");

            // Création de l'objet Note correspondant et ajout à la liste
            Note note = new Note(supprimerNote, dateNote, texteNote, idNote, titreNote, titreLivre, idTitre, idAuteur, auteur);
            notes.add(note);
        }

        return notes;

    }

    public Set<INote> recupNotePasDansCorbeillle() throws SQLException, ClassNotFoundException {
        Set<INote> notes = new HashSet<>();
        for (INote note: recupAllNote()) {
            if (!note.isSupprimer_note()){
                notes.add(note);
            }
        }

        return notes;
    }

    public Set<INote> recupNoteDansCorbeillle() throws SQLException, ClassNotFoundException {
        Set<INote> notes = new HashSet<>();
        for (INote note: recupAllNote()) {
            if (note.isSupprimer_note()){
                notes.add(note);
            }
        }

        return notes;
    }

    public void supprimerNoteDansDB(INote note) throws SQLException, ClassNotFoundException{
        gestionDB.executeSqlInsertOrUpdate("DELETE FROM note WHERE id_note = " + note.getId_note() + ";");
    }

    public void mettreUneNoteEnCorbeil(INote note) throws SQLException, ClassNotFoundException {
        gestionDB.executeSqlInsertOrUpdate("UPDATE note SET supprimee = true WHERE id_note = " + note.getId_note() + ";");
    }

    public void sortirUneNoteDeCorbeil(INote note) throws SQLException, ClassNotFoundException {
        gestionDB.executeSqlInsertOrUpdate("UPDATE note SET supprimee = false WHERE id_note = " + note.getId_note() + ";");
    }

    public Set<INote> rechercheDansDB(String motCle) throws SQLException, ClassNotFoundException {
        Set<INote> notes = new HashSet<>();
        String command = "SELECT * FROM note LEFT JOIN Livre USING(id_livre) LEFT JOIN auteur USING (id_auteur) WHERE texte LIKE \"%" + motCle + "%\"" +
                           "OR note.titre LIKE \"%" + motCle + "%\" OR livre.titre LIKE \"%" + motCle + "%\" OR nom_auteur LIKE \"%" + motCle + "%\";";
        ResultSet resultSet = gestionDB.executeSqlSelect(command);
        while (resultSet.next()) {
            // Récupération des données de la base de données pour chaque note
            int idAuteur = resultSet.getInt("id_auteur");
            String auteur = resultSet.getString("nom_auteur");
            int idTitre = resultSet.getInt("id_livre");
            String titreLivre = resultSet.getString("titre");
            int idNote = resultSet.getInt("id_note");
            String titreNote = resultSet.getString("titre");
            String texteNote = resultSet.getString("texte");
            Date dateNote = resultSet.getDate("date_creation");
            boolean supprimerNote = resultSet.getBoolean("supprimee");

            // Création de l'objet Note correspondant et ajout à la liste
            Note note = new Note(supprimerNote, dateNote, texteNote, idNote, titreNote, titreLivre, idTitre, idAuteur, auteur);
            notes.add(note);
        }

        return notes;
    }

    public Set<String> recupAllMotCle() throws SQLException, ClassNotFoundException {
        Set<String> mots = new HashSet<>();
        ResultSet resultSet = gestionDB.executeSqlSelect("SELECT * FROM historique ORDER BY date_historique");
        while (resultSet.next()) {
            mots.add(resultSet.getString("mot_cle"));
        }

        return mots;
    }

    public Set<String> rechercheMotCle(String mot) throws SQLException, ClassNotFoundException {
        Set<String> mots = new HashSet<>();
        ResultSet resultSet = gestionDB.executeSqlSelect("SELECT * FROM historique  WHERE mot_cle LIKE \"%" + mot + "%\" ORDER BY date_historique;");
        while (resultSet.next()) {
            mots.add(resultSet.getString("mot_cle"));
        }

        return mots;
    }

    public Set<INote> rechercheDansDBAvecSaveMotCle(String motCle) throws SQLException, ClassNotFoundException {
        saveMotCle(motCle);
        return rechercheDansDB(motCle);
    }

    public void saveMotCle(String motCle) throws SQLException, ClassNotFoundException {
        gestionDB.executeSqlInsertOrUpdate("INSERT INTO historique(mot_cle) VALUES(\"" + motCle + "\");");
    }
}
