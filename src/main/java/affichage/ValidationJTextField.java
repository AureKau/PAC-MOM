package affichage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.function.Function;

public class ValidationJTextField extends JPanel implements FocusListener, DocumentListener {
//Test
    private JTextField _textField;
    private JLabel _validationLabel;
    private Function<String, Boolean> _validationFunction;

    private boolean _isTouched;
    private boolean _isDirty;
    private boolean _isValidating;

    public ValidationJTextField() {
        this("","", (String text) -> { return true; });
    }

    public ValidationJTextField(String initialValue, String errorMessage, Function<String, Boolean> validationFunction){
        this._textField = new JTextField(initialValue);
        this._validationLabel = new JLabel(errorMessage);
        this._validationFunction = validationFunction;

        this._isTouched = false;
        this._isDirty = false;
        this._isValidating = false;

        this._textField.addFocusListener(this);
        this._textField.getDocument().addDocumentListener(this);

        this._validationLabel.setVisible(false);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(_textField);
        this.add(_validationLabel);
    }

    public boolean validateField(){
        return _validationFunction.apply(_textField.getText());
    }

    public String getText(){
        return _textField.getText();
    }

    public void setText(String value){
        _textField.setText(value);
    }

    public void setValidationLabel(String value){
        _validationLabel.setText(value);
    }

    public void setValidationFunction(Function<String, Boolean> _validationFunction){
        this._validationFunction = _validationFunction;
    }

    public void removeValidation()
    {
        this._isValidating = false;
        this._validationLabel.setVisible(false);
        this._isDirty = false;
        this._isTouched = false;
    }

    public boolean isDirty(){
        return this._isDirty;
    }

    public boolean isTouched(){
        return this._isTouched;
    }

    @Override
    public void focusGained(FocusEvent e) {
        this._isTouched = true;
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(_isValidating && _validationFunction != null)
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
        this._isDirty = true;
        if(_isValidating)
            validateField();
    }
}
