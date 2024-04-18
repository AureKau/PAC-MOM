package affichage;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class ValidationJTextField extends JPanel {

    private JTextField textField;
    private JLabel validationLabel;
    private Function<String, Boolean> validationFunction;

    public ValidationJTextField() {
        this("","", (String text) -> { return true; });
    }

    public ValidationJTextField(String initialValue, String errorMessage, Function<String, Boolean> validationFunction){
        this.textField = new JTextField(initialValue);
        this.validationLabel = new JLabel(errorMessage);
        this.validationFunction = validationFunction;

        this.validationLabel.setVisible(false);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(textField);
        this.add(validationLabel);
    }

    public boolean validateField(){
        return validationFunction.apply(textField.getText());
    }

    public String getValue(){
        return textField.getText();
    }

    public void setValue(String value){
        textField.setText(value);
    }

    public void setValidationLabel(String value){
        validationLabel.setText(value);
    }

    public void setValidationFunction(Function<String, Boolean> validationFunction){
        this.validationFunction = validationFunction;
    }

    public void removeValidation(){
        this.validationLabel.setVisible(false);
    }
}
