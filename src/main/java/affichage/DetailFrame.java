package affichage;

import note.Note;

import javax.swing.*;

public class DetailFrame extends JFrame {

    private DetailPanel detailPanel = new DetailPanel();

    public DetailFrame() {
        initComponent();
    }

    public DetailFrame(Note note) {
        initComponent();
    }

    public void initComponent() {
        add(detailPanel);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(800, 400);
        setResizable(false);
        setTitle("Note");
        setVisible(true);
    }
}
