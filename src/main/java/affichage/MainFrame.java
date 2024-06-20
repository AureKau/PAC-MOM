package affichage;

import DataBase.src.NoteRepository;
import note.Note;
import utils.ImageUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;


public class MainFrame extends JFrame implements ActionListener {

    private ImageUtils imgUtils = new ImageUtils();
    private NoteRepository noteRepository = new NoteRepository();
    private HashSet<Integer> selectedNoteIds = new HashSet<>();

    private static final int NOTES_PER_ROW = 4;

    public JPanel panel = (JPanel) getContentPane();

    public ImageIcon logo = new ImageIcon(imgUtils.GetImageURL("Logo"));
    public JLabel logoLabel = new JLabel(logo);

    public ImageIcon ajouter = new ImageIcon(imgUtils.GetImageURL("Add button"));
    public JButton ajouterButton = createButton(ajouter, 100, 100);

    public ImageIcon supprimerButton = new ImageIcon(imgUtils.GetImageURL("Delete_Button"));
    public JButton deleteButton ;

    public ImageIcon rechercherButton = new ImageIcon(imgUtils.GetImageURL("el_search-alt"));
    public JButton searchButton = createButton(rechercherButton, 100, 100);
    public JPanel searchPanel = new JPanel();
    public JTextField searchText = new JTextField(20);
    
    public JPanel listPanel = new JPanel();

    public MainFrame () {
        deleteButton = createButton(supprimerButton, 100, 100);
        initComponent();

        ArrayList<Note> notes = new ArrayList<Note>();
        try{
            notes = noteRepository.getAll();
        }
        catch (Exception e){
            // BOUM !!!
        }
        refreshList(notes);

        setVisible(true);
    }

    private void refreshList(ArrayList<Note> notes){
        listPanel.removeAll();
        listPanel.setLayout(new GridLayout((notes.size() / NOTES_PER_ROW) + 1, NOTES_PER_ROW, 10, 10));
        notes.forEach(note -> {
            listPanel.add(new ResumePanel(note, selectedNoteIds));
        });
        listPanel.revalidate();
        listPanel.repaint();
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

    private void initComponent() {
        setTitle("Mon application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(800, 600);
        setResizable(false);
        setLayout(new BorderLayout());
        panel.setBackground(new Color(255, 253, 246));

        ajouterButton.setOpaque(false);
        deleteButton.setOpaque(false);
        searchButton.setOpaque(false);
        logoLabel.setOpaque(false);

        searchText.setBorder(BorderFactory.createLineBorder(Color.black));
        searchText.setBackground(new Color(233, 233, 233));
        searchText.setBorder(null);

        Border roundedBorder = new LineBorder(new Color(233, 233, 233), 15, true);
        searchText.setBorder(roundedBorder);
        searchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(searchText);
        searchPanel.add(searchButton);
        searchPanel.setOpaque(false);

        // North panel for logo and search
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(logoLabel, BorderLayout.WEST);
        northPanel.add(searchPanel, BorderLayout.EAST);
        northPanel.setBackground(new Color(255, 253, 246));
        add(northPanel, BorderLayout.NORTH);

        // South panel for delete and add
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(deleteButton, BorderLayout.WEST);
        southPanel.add(ajouterButton, BorderLayout.EAST);
        southPanel.setBackground(new Color(255, 253, 246));
        add(southPanel, BorderLayout.SOUTH);

        listPanel = new JPanel(new GridLayout());
        add(listPanel, BorderLayout.CENTER);

        deleteButton.addActionListener(this);
        deleteButton.setActionCommand("delete");
        ajouterButton.addActionListener(this);
        ajouterButton.setActionCommand("add");
        searchButton.addActionListener(this);
        searchButton.setActionCommand("search");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "delete":
                PopupDelete popupDelete = new PopupDelete(() -> {
                    for(Integer i: selectedNoteIds)
                        noteRepository.delete(i);
                    try {
                        refreshList(noteRepository.getAll());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                popupDelete.showPopUp();
                break;
            case "add":
                    // new window add
                break;
            case "search":
                try {
                    refreshList(noteRepository.search(searchText.getText()));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + actionCommand);
        }
    }
}