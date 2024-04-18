package note;

import DataBase.src.GestionDB;

import java.sql.Date;
import java.sql.SQLException;

public class Note {

    private int id_auteur;
    private String auteur;
    private int id_titre;
    private String titre_livre;
    private int id_note;
    private String titre_note;
    private String texte_note;
    private Date date_note;
    private boolean supprimer_note;


    public int getId_auteur() {
        return id_auteur;
    }

    public void setId_auteur(int id_auteur) {
        this.id_auteur = id_auteur;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getId_titre() {
        return id_titre;
    }

    public void setId_titre(int id_titre) {
        this.id_titre = id_titre;
    }

    public String getTitre_livre() {
        return titre_livre;
    }

    public void setTitre_livre(String titre_livre) {
        this.titre_livre = titre_livre;
    }

    public int getId_note() {
        return id_note;
    }

    public void setId_note(int id_note) {
        this.id_note = id_note;
    }

    public String getTitre_note() {
        return titre_note;
    }

    public void setTitre_note(String titre_note) {
        this.titre_note = titre_note;
    }

    public String getTexte_note() {
        return texte_note;
    }

    public void setTexte_note(String texte_note) {
        this.texte_note = texte_note;
    }

    public Date getDate_note() {
        return date_note;
    }

    public void setDate_note(Date date_note) {
        this.date_note = date_note;
    }

    public boolean isSupprimer_note() {
        return supprimer_note;
    }

    public void setSupprimer_note(boolean supprimer_note) {
        this.supprimer_note = supprimer_note;
    }

    public Note() {
        // Constructeur par défaut
    }



    // Méthode pour sauvegarder les variables de la note dans la base de données
    public void sauvegarderNote() throws SQLException, ClassNotFoundException {
        GestionDB gestionDB = new GestionDB();
        // Vérifiez si l'ID de la note est déjà défini
        if (id_note == 0) {
            gestionDB.saveNoteWithAll(auteur, titre_livre, titre_note, texte_note);
        } else {

            gestionDB.executeSqlInsertOrUpdate("UPDATE Note SET titre = '"
                    + titre_note + "', texte = '"
                    + texte_note + "' WHERE id_note = " + id_note);
        }
    }


    @Override
    public String toString() {
        return "Note{" +
                "id_auteur=" + id_auteur +
                ", auteur='" + auteur + '\'' +
                ", id_titre=" + id_titre +
                ", titre_livre='" + titre_livre + '\'' +
                ", id_note=" + id_note +
                ", titre_note='" + titre_note + '\'' +
                ", texte_note='" + texte_note + '\'' +
                ", date_note=" + date_note +
                ", supprimer_note=" + supprimer_note +
                '}';
    }
}
