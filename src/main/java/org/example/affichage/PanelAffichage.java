package org.example.affichage;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class PanelAffichage extends JPanel {

    JButton btnAffichageNote = new JButton("Titre de Note");

    String txtTitre = "Le Petit Prince" ;
    String txtAuteur = "par Antoine de Saint-Exupéry" ;
    String txtResume = "Il était une fois un petit prince qui voulait un mouton. Pourquoi ? Ca personne ne le savait. Ce qui était sur c'était qui aimait les étoiles et le fromage";

    JButton btnTest = new JButton();

    public PanelAffichage(){

        setVisible(true);

        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(470,470));
        setBackground(new Color(134, 134, 134));


        for (int i = 0 ; i < 4 ; i++){

            btnAffichageNote = new JButton("<html>Le petit prince<br>- par Antoine de Saint-Exupéry <br><br>" +
                    "<br>Il était une fois un petit prince qui voulait un mouton. Pourquoi ? Ca personne ne le savait. Ce qui était sur c'était qui aimait les étoiles et le fromage</html>");

            btnAffichageNote.setFont(new Font("Times new roman",Font.BOLD,14));
            btnAffichageNote.setBackground(new Color(218, 218, 218));

            btnAffichageNote.setPreferredSize(new Dimension(200,200));
            add(btnAffichageNote);
        }
    }
}
