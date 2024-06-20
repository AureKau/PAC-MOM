package affichage;

import note.Note;
import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

public class ResumePanel extends JPanel implements ActionListener {


    private ImageUtils imgUtils = new ImageUtils();
    private Note note;
    private HashSet<Integer> selectedNotes;

    public JPanel contentPane = new JPanel();
    public JLabel titre = new JLabel("Titre");
    public JLabel paragraphe = new JLabel("Paragraphe");
    public ImageIcon checkIcon = new ImageIcon(imgUtils.GetImageURL("Check"));
    public ImageIcon noCheckIcon = new ImageIcon(imgUtils.GetImageURL("NoCheck"));
    public JButton checkButton = createIconButton(noCheckIcon, 50, 50);


    public ResumePanel() {
        this(null);
    }

    public ResumePanel(Note note) {
        this(note, new HashSet<>());
    }

    public ResumePanel(Note note, HashSet<Integer> notes) {
        this.note = note;
        this.selectedNotes = notes;
        initComponent();
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

        // Adding checkBox
        // Creating button panel
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        checkBoxPanel.add(checkButton);
        gbc.gridx = 1; // placing it in the second column
        gbc.gridy = 0; // placing it in the first row
        gbc.weightx = 0; // do not stretch horizontally
        gbc.anchor = GridBagConstraints.NORTHEAST; // align top-right corner
        contentPane.add(checkBoxPanel, gbc);

        // For the titre label, you must set gbc.gridx back to 0 and gbc.weightx to 1.
        gbc.gridx = 0;
        gbc.weightx = 1;

        titre.setFont(new Font("Arial", Font.BOLD, 30));
        titre.setForeground(new Color(114, 213, 192));
        contentPane.add(titre, gbc);

        // Creating button panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Button should be at the south and not take all the space
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.gridy = 2;

        contentPane.add(buttonsPanel, gbc);

        if(note != null){
            this.titre.setText(note.getTitre_note());
            this.paragraphe.setText(note.getTexte_note());
        }

        add(contentPane);
    }

    private JButton createIconButton(ImageIcon imgIcon, int width, int height) {
        JButton button = new JButton(imgIcon);

        button.addActionListener(new ActionListener() {
            boolean isChecked = false;
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                isChecked = !isChecked;
                if (isChecked) {
                    button.setIcon(checkIcon);
                    selectedNotes.add(note.getId_note());
                } else {
                    button.setIcon(noCheckIcon);
                    selectedNotes.remove(note.getId_note());
                }
            }
        });
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(50, 50);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draws the rounded panel with borders.
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
        graphics.setColor(getForeground());
        graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
