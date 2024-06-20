package affichage.ilias;



import note.INote;
import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class OverlayFroCreateNote extends JDialog implements ActionListener, KeyListener {

    private final JTextField txtTitre = new JTextField("titre");

    private final JTextField txtTexte = new JTextField("texte de la note");

    private final JButton btnValidate = new JButton("validé");

    private final JButton btnRetour = new JButton("retour");

    private final Set<OverlayForCreateNoteListener> listeners = new HashSet<>();

    private final String actionCommandRetour = "retour";

    private final String actionCommandValidate = "valide";
    private ImageUtils imgUtils = new ImageUtils();


    private INote note;

    private JFrame parentFrame;


    public OverlayFroCreateNote(INote note, JFrame parentFrame, OverlayForCreateNoteListener listener) {
        addOverlayForCreateNoteListener(listener);
        setNote(note);
        setParentFrame(parentFrame);
        if (note.getTexte_note() != null) getTxtTexte().setText(note.getTexte_note());
        if (getNote().getTitre_note()!= null) getTxtTitre().setText(getNote().getTitre_note());
        initComponent();
        setVisible(true);
    }

    private void initComponent(){
        setSize(300, 200);
        setLayout(new BorderLayout());
        setModalityType(ModalityType.APPLICATION_MODAL);
        add(getTxtTitre(), BorderLayout.NORTH);
        add(getTxtTexte(), BorderLayout.CENTER);
        JPanel panelSouth = new JPanel(new FlowLayout(FlowLayout.LEFT));
        getBtnRetour().addActionListener(this);
        getBtnRetour().setActionCommand(actionCommandRetour);
        panelSouth.add(getBtnRetour());
        getBtnValidate().addActionListener(this);
        getBtnValidate().setActionCommand(actionCommandValidate);
        panelSouth.add(getBtnValidate());
        add(panelSouth, BorderLayout.SOUTH);
        setLocationRelativeTo(getParentFrame());
        this.addKeyListener(this);
        getTxtTitre().addKeyListener(this);
        getTxtTexte().addKeyListener(this);
    }


    public JTextField getTxtTitre() {
        return txtTitre;
    }

    public JTextField getTxtTexte() {
        return txtTexte;
    }

    public JButton getBtnValidate() {
        return btnValidate;
    }

    public JButton getBtnRetour() {
        return btnRetour;
    }

    public INote getNote() {
        return note;
    }

    public void setNote(INote note) {
        this.note = note;
    }

    public Set<OverlayForCreateNoteListener> getListeners() {
        return listeners;
    }

    public JFrame getParentFrame() {
        return parentFrame;
    }

    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
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

    public void addOverlayForCreateNoteListener(OverlayForCreateNoteListener listener){
        getListeners().add(listener);
    }

    public void removeOverlayForCreateNoteListener(OverlayForCreateNoteListener listener){
        getListeners().remove(listener);
    }


    private void fireListenerBtnValidate(INote note){

        getNote().setTexte_note(getTxtTexte().getText());
        getNote().setTitre_note(getTxtTitre().getText());
        for (OverlayForCreateNoteListener listener: getListeners()) {
            listener.btnValidate(note);
        }
        dispose();
    }

    private void fireListenerBtnRetour(){
        for (OverlayForCreateNoteListener listener: getListeners()) {
            listener.btnRetour();
        }
        dispose();
    }




    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case actionCommandValidate -> fireListenerBtnValidate(getNote());
            case actionCommandRetour -> fireListenerBtnRetour();
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            fireListenerBtnValidate(getNote());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
