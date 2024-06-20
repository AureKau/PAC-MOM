package affichage;

import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResumeFrame extends JFrame {

    private ResumePanel panel = new ResumePanel();

    public ResumeFrame(){
        initComponent();
    }

    private void initComponent() {
        add(panel);
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setResizable(false);
        setTitle("Note");
        setVisible(true);
    }
}