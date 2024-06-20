package affichage;

import note.Note;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class DetailFrame extends JFrame implements ActionListener {

    private DetailPanel detailPanel = new DetailPanel();

    public DetailFrame() {
        initComponent();
    }

    public DetailFrame(Note note) {
        this.detailPanel = new DetailPanel(note);
        initComponent();
    }

    public void initComponent() {
        add(detailPanel);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(800, 400);
        setResizable(false);
        setTitle("Note");
        setVisible(true);
        this.detailPanel.backButton.addActionListener(this);
        this.detailPanel.backButton.setActionCommand("back");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (Objects.equals(e.getActionCommand(), "back")){
            dispose();
        }
    }
}
