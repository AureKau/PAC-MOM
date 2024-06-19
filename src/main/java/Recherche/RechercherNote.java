package Recherche;

/*

    Ici on doit etre capable de pouvoir chercher et afficher une note recherchée,
    parmis toute celle que l'on possède. On doit afficher les notes qui correspondent à la recherche.


    ILLIAS - gère la partie recherche dans la base de donnée

    MOI - je gère la partie qui affichera le(s) résultat(s).


    - Afficher les resultats des recherches
    - Proposition Automatique    - Auto Suggestion quand on clique sur la barre de recherche
    - Selection d'une note depuis les résultats de recherche
    - Sauvegarde de l'historique ( Illias ? )



    /----

    Quand on tape des lettres on affiche les notes correspondante dans la db
    Si trop compliqué ne faire ça que si on appuie sur "ENTER"

    Apres une recherche on enregistre le mot dans la DB
    Que ce soit après "ENTER" ou après avoir cliqué sur une note



    Afficher l'historique des notes
    Que ce sois en popup menu ou autre



    /----


    BONUS
    creer un placeHolder qui disparait quand on ecrit
    autosugg

 */


import note.GestionNote;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class RechercherNote {

    GestionNote gestionNote ;

    String motRecherche;

    //  a delete pour le final
    int compteurPourBugRecherche = 0;


    JTextField barreDeRecherche;
    JWindow windowHistorique = new JWindow();


    LinkedList<String> listeDerniereRecherche = new LinkedList<>();
    ArrayList<JPanel> listeRececementNote = new ArrayList<>();






    public void lancerRecherche(){

        // pour lancer une recher il faudra que chaque note qui est cree soit comprise dans
        // la listeRececementNote sinon il n'y aura pas d'endroit ou faire une recherche
        String searchText = barreDeRecherche.getText().toLowerCase();
        for (JPanel panNote : listeRececementNote) {
            boolean matches = false;
            for (Component component : panNote.getComponents()) {

                // !!!!!!    Verifier avec Illias et Aurelien quel type de contenair pour
                if (component instanceof JTextArea) {
                    JTextArea textArea = (JTextArea) component;
                    if (textArea.getText().toLowerCase().contains(searchText)) {
                        matches = true;
                        break;
                    }
                }
            }
            panNote.setVisible(matches);
        }
        /*
        panListeNote.revalidate();
        panListeNote.repaint();
         */

        compteurPourBugRecherche++;
        System.out.println(compteurPourBugRecherche);
    }





    public void actualiserAfficherSelectionerHistoriqueRecherche(){

        // ++ initialiser historique ici - Jason du futur oublie pas !!! ++
        // utiliser une boucle for ???


        // rempli l'historique visible
        for (String elementHistorique : listeDerniereRecherche) {
            JLabel labelHistorique = new JLabel(elementHistorique);
            labelHistorique.setOpaque(true);
            // labelHistorique.setBackground(new Color(216, 229, 167));
            // labelHistorique.setBorder(BorderFactory.createLineBorder(Color.blue));
            labelHistorique.setPreferredSize(new Dimension(200, 30));

            // ferme l'historique quand on choisit un element
            labelHistorique.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    barreDeRecherche.setText(labelHistorique.getText());
                    windowHistorique.setVisible(false);
                }
            });
            windowHistorique.add(labelHistorique);
        }

        // Affiche l'historique si on clique sur barre recherche
        barreDeRecherche.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point location = barreDeRecherche.getLocationOnScreen();
                windowHistorique.setLocation(location.x, location.y + barreDeRecherche.getHeight());
                windowHistorique.setPreferredSize(new Dimension(100,100));
                windowHistorique.pack();

                if (barreDeRecherche.getText().isEmpty()){
                    windowHistorique.setVisible(true);
                }
            }
        });


        // affiche et cache l'historique en fonction du texte dans la barre de recherche
        // ++ enregistrer dans historique de recherche de la DB part 1
        barreDeRecherche.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if ( !barreDeRecherche.getText().isEmpty() ){

                    windowHistorique.setVisible(false);
                } else {
                    windowHistorique.setVisible(true);
                }


                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    try {
                        ajoutHistoriqueRecherche();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }






    private void ajoutHistoriqueRecherche() throws SQLException, ClassNotFoundException {

        motRecherche = barreDeRecherche.getText();

        gestionNote.saveMotCle(motRecherche);

    }

}
