package note;

public class NoteException extends Exception{

    public NoteException() {
    }

    public NoteException(String message) {
        super(message);
    }

    public NoteException(String message, Throwable cause) {
        super(message, cause);
    }
}
