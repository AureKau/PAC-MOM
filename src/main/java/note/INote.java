package note;

import java.sql.Date;

public interface INote {

    public int getId_auteur();

    public void setId_auteur(int id_auteur);

    public String getAuteur();

    public void setAuteur(String auteur);

    public int getId_titre();

    public void setId_titre(int id_titre);

    public String getTitre_livre();

    public void setTitre_livre(String titre_livre);

    public int getId_note();

    public void setId_note(int id_note);

    public String getTitre_note();

    public void setTitre_note(String titre_note);

    public String getTexte_note();

    public void setTexte_note(String texte_note);

    public Date getDate_note();

    public void setDate_note(Date date_note);

    public boolean isSupprimer_note();

    public void setSupprimer_note(boolean supprimer_note);
}
