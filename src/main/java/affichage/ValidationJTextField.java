package affichage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.function.Function;

public class ValidationJTextField extends JPanel implements FocusListener, DocumentListener {

    private JTextField textField;
    private JLabel validationLabel;
    private Function<String, Boolean> validationFunction;

    private boolean isTouched;
    private boolean isDirty;
    private boolean isValidating;

    public ValidationJTextField() {
        this("","", (String text) -> { return true; });
    }

    public ValidationJTextField(String initialValue, String errorMessage, Function<String, Boolean> validationFunction){
        this.textField = new JTextField(initialValue);
        this.validationLabel = new JLabel(errorMessage);
        this.validationFunction = validationFunction;

        this.isTouched = false;
        this.isDirty = false;
        this.isValidating = false;

        this.textField.addFocusListener(this);
        this.textField.getDocument().addDocumentListener(this);

        this.validationLabel.setVisible(false);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(textField);
        this.add(validationLabel);
    }

    public boolean validateField(){
        return validationFunction.apply(textField.getText());
    }

    public String getText(){
        return textField.getText();
    }

    public void setText(String value){
        textField.setText(value);
    }

    public void setValidationLabel(String value){
        validationLabel.setText(value);
    }

    public void setValidationFunction(Function<String, Boolean> validationFunction){
        this.validationFunction = validationFunction;
    }

    public void removeValidation()
    {
        this.isValidating = false;
        this.validationLabel.setVisible(false);
        this.isDirty = false;
        this.isTouched = false;
    }

    public boolean isDirty(){
        return this.isDirty;
    }

    public boolean isTouched(){
        return this.isTouched;
    }

    @Override
    public void focusGained(FocusEvent e) {
        this.isTouched = true;
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(isValidating && validationFunction != null)
            validateField();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        // Nothing to do here
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // Nothing to do here
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        this.isDirty = true;
        if(isValidating)
            validateField();
    }
}
