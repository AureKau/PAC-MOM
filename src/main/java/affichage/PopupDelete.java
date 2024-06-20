package affichage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopupDelete implements ActionListener {
    private JPanel panel;
    private JButton ouiButton;
    private JButton nonButton;
    private JLabel textLabel;
    private JDialog dialog;

    private Runnable ouiAction;

    public PopupDelete(Runnable ouiAction) {
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(350, 75));
        panel.setBackground(new Color(239, 239, 239));

        ouiButton = new JButton("OUI");
        ouiButton.setBackground(new Color(114, 213, 192));
        ouiButton.setForeground(new Color(255, 255, 255));
        ouiButton.setBorderPainted(false);

        nonButton = new JButton("NON");
        nonButton.setBackground(new Color(114, 213, 192));
        nonButton.setForeground(new Color(255, 255, 255));
        nonButton.setBorderPainted(false);


        textLabel = new JLabel("Êtes-vous sûr(e) de vouloir supprimer la (les) note(s) ? ", SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 12));
        textLabel.setForeground(new Color(114, 213, 192));
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.add(textLabel, BorderLayout.CENTER);

        panel.add(textLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(ouiButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(nonButton);

        nonButton.addActionListener(this);
        nonButton.setActionCommand("non");
        ouiButton.addActionListener(this);
        nonButton.setActionCommand("oui");

        this.ouiAction = ouiAction;
    }

    public void showPopUp() {
        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, new Object[]{}, null);
        dialog = optionPane.createDialog("Confirmation");
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "NON":

                break;
            case "OUI":
                if (ouiAction != null) {
                    ouiAction.run();
                }
                break;
        }

        // Close the dialog
        if (dialog != null) {
            dialog.dispose();
        }
    }
}
