package affichage;

import javax.swing.*;
import java.awt.*;


public class FenetreAffichage extends JFrame {

    PanelAffichage panAffich = new PanelAffichage();

    public static void main(String[] args) {
        new FenetreAffichage();
    }
    public FenetreAffichage(){

        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(500,500);
        setLocationRelativeTo(null);
        add(panAffich);
        pack();
    }
}