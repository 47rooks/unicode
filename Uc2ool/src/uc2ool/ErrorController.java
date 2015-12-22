/**
 * Copyright Daniel Semler 2015
 */
package uc2ool;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * @author dsemler
 *
 */
public class ErrorController {
    @FXML
    private Label m_errorMessage;
    
    public void setErrorText(String text) {
        m_errorMessage.setText(text);
    }

    @FXML
    private void close() {
        m_errorMessage.getScene().getWindow().hide();
    }
}
