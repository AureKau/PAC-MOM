package affichage;

import note.Note;
import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

public class DetailPanel extends JPanel {


    private ImageUtils imgUtils = new ImageUtils();
    private Note note;

    public JPanel contentPane = new JPanel();
    public JLabel titre = new JLabel("Titre");
    public JLabel paragraphe = new JLabel("Paragraphe");

    public ImageIcon modifier = new ImageIcon(imgUtils.GetImageURL("Modif_Button"));
    public JButton modifierButton = createButton(modifier, 50, 50);

    public ImageIcon back = new ImageIcon(imgUtils.GetImageURL("Back_Button"));
    public JButton backButton = createButton(back, 50, 50);

    public ImageIcon validation = new ImageIcon(imgUtils.GetImageURL("Save_Button"));
    public JButton validationButton = createButton(validation, 50, 50);

    public DetailPanel(){
        initComponent();
    }

    public DetailPanel(Note note){
        this.note = note;
        initComponent();
    }

    private JButton createButton(ImageIcon imgIcon, int width, int height) {
        Image img = imgIcon.getImage();
        Image resizedImage = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
        imgIcon = new ImageIcon(resizedImage);
        JButton button = new JButton(imgIcon);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

    public void initComponent() {
        setBackground(new Color(239, 239, 239));

        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Content should be whole panel breadth and vertically in North
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        titre.setFont(new Font("Arial", Font.BOLD, 30));
        titre.setForeground(new Color(114, 213, 192));
        contentPane.add(titre, gbc);

        // Adding paragraph
        gbc.gridy = 1;
        paragraphe.setFont(new Font("Arial", Font.CENTER_BASELINE, 16));
        paragraphe.setForeground(new Color(51, 51, 51));
        contentPane.add(paragraphe, gbc);

        // Creating button panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(modifierButton);
        buttonsPanel.add(backButton);
        buttonsPanel.add(validationButton);

        // Button should be at the south and not take all the space
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.gridy = 2;

        contentPane.add(buttonsPanel, gbc);

        add(contentPane);

        if(this.note != null){
            titre.setText(note.getTitre_note());
            paragraphe.setText(note.getTexte_note());
        }
    }
}
