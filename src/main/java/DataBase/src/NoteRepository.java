package DataBase.src;

import note.GestionNote;
import note.INote;
import note.Note;
import java.sql.*;

import java.util.ArrayList;

// improve with db result

public class NoteRepository {

    private static final GestionDB db = GestionDB.getInstance();

    private GestionNote gestionNote = new GestionNote();

    private static ArrayList<Note> notes = new ArrayList<>();

    public NoteRepository(){
        notes.add(new Note(false, new Date(2024,01,10), "This is a detailed note about the book.", 123, "Review of 'Effective Java'", "Effective Java", 456, 789, "Joshua Bloch"));
        notes.add(new Note(true, new Date(2024,01,10), "An insightful analysis on design patterns.", 124, "Thoughts on 'Design Patterns'", "Design Patterns", 457, 790, "Erich Gamma"));
        notes.add(new Note(false, new Date(2024,01,10), "A comprehensive guide to algorithms.", 125, "Notes on 'Introduction to Algorithms'", "Introduction to Algorithms", 458, 791, "Thomas H. Cormen"));
        notes.add(new Note(false, new Date(2024,01,10), "Discussion on refactoring techniques.", 126, "Review of 'Refactoring'", "Refactoring", 459, 792, "Martin Fowler"));
        notes.add(new Note(true, new Date(2024,01,10), "Review on clean code practices.", 127, "Clean Code Review", "Clean Code", 460, 793, "Robert C. Martin"));
        notes.add(new Note(false, new Date(2024,01,10), "Notes on software architecture.", 128, "Software Architecture Guide", "Software Architecture", 461, 794, "Mary Shaw"));
        notes.add(new Note(false, new Date(2024,01,10), "Review of the book on domain-driven design.", 129, "Domain-Driven Design Review", "Domain-Driven Design", 462, 795, "Eric Evans"));
        notes.add(new Note(false, new Date(2024,01,10), "A detailed analysis on programming paradigms.", 130, "Programming Paradigms", "Programming Paradigms", 463, 796, "Paul Graham"));
        notes.add(new Note(false, new Date(2024,01,10), "A comprehensive guide on computer networks.", 131, "Computer Networks Guide", "Computer Networks", 464, 797, "Andrew S. Tanenbaum"));
        notes.add(new Note(false, new Date(2024,01,10), "A detailed review on cryptography.", 132, "Cryptography Review", "Cryptography", 465, 798, "Bruce Schneier"));
    }

    public ArrayList<Note> getAll() throws SQLException, ClassNotFoundException {
//        ResultSet dbResult = db.executeSqlSelect("SELECT * FROM note");
//        ArrayList result = new ArrayList<Note>();
//        while (dbResult.next()) {
//            result.add(fromResultSetToNote(dbResult));
//        }
//        return result;

        for (INote notes: gestionNote.recupAllNote()){
            add((Note) notes);
        }
        return notes;
    }

    public Note get(int id){
//        ResultSet dbResult = db.executeSqlSelect("SELECT * FROM note");
//        ArrayList result = new ArrayList<Note>();
//        while (dbResult.next()) {
//            result.add(fromResultSetToNote(dbResult));
//        }
//        return result;

        for (Note note : notes) {
            if (note.getId_note() == id) {
                return note;
            }
        }
        return null; // Note not found
    }

    public ArrayList<Note> search(String searchText) throws SQLException, ClassNotFoundException {
        if(searchText == "")
            return getAll();
        ArrayList<Note> result = new ArrayList<>();
        for (Note note : notes) {
            if (note.getTexte_note().contains(searchText) || note.getTitre_note().contains(searchText) || note.getAuteur().contains(searchText)) {
                result.add(note);
            }
        }
        return result;
    }

    public boolean add(Note note){
        return notes.add(note);
    }

    public boolean update(Note note){
        for (int i = 0; i < notes.size(); i++) {
            Note noted = notes.get(i);
            if (noted.getId_note() == note.getId_note()) {
                notes.set(i, note);
                return true;
            }
        }
        return false; // Note not found
    }

    public boolean delete(int id){
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            if (note.getId_note() == id) {
                notes.remove(i);
                return true;
            }
        }
        return false;
    }

    private Note fromResultSetToNote(ResultSet result) throws SQLException {
        if (result.getRow() == 0) return null;
        return new Note(
                result.getBoolean("supprimer_note"),
                result.getDate("date_note"),
                result.getString("text_note"),
                result.getInt("id_note"),
                result.getString("titre_note"),
                result.getString("titre_livre"),
                result.getInt("id_titre"),
                result.getInt("id_auteur"),
                result.getString("auteur")
                );
    }
}
