package uc2ool;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import ds.debug.DebugLogger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import uc2ool.model.Uc2oolModel;
import uc2ool.model.Uc2oolModel.InputType;

public class Uc2oolController {
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle m_resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL m_location;

	@FXML //  fx:id="m_process"
	private Button m_process; // Value injected by FXMLLoader
	
	@FXML //  fx:id="m_inputCharacter"
	private TextField m_inputCharacter; // Value injected by FXMLLoader
	
	// RadioButton set for input types
	@FXML // fx:id="m_inputType"
	private ToggleGroup m_inputType;
	@FXML // fx:id="m_characterRB"
    private RadioButton m_characterRB;
	@FXML // fx:id="m_hexCodePoint"
    private RadioButton m_hexCodePointRB;
    @FXML // fx:id="m_decimalCodePoint"	
    private RadioButton m_decimalCodePointRB;
    @FXML // fx:id="m_UTF8Encoding"     
    private RadioButton m_UTF8EncodingRB;
    
    @FXML // fx:id="m_unicodeName"
    private Label m_unicodeName;
    
    @FXML // fx:id="m_unicodeName"
    private Label m_utf16Encoding;
    
    @FXML // fx:id="m_unicodeName"
    private Label m_utf8Encoding;
    
    @FXML // fx:id="m_unicodeName"
    private Label m_decimalCodePoint;
    
    @FXML // fx:id="m_glyph"
    private TextArea m_glyph;
    
    @FXML // fx:id="m_font"
    private ComboBox<String> m_font;
    private final String DEFAULT_FONT_NAME = "Cardo";
    private String m_fontValue = DEFAULT_FONT_NAME;
    
    @FXML // fx:id="m_fontSize"
    private ComboBox<String> m_fontSize;
    private final String DEFAULT_FONT_SIZE = "80";
    private double m_fontSizeValue = Double.valueOf(DEFAULT_FONT_SIZE);
    
    @FXML // fx:id="m_statusBar"
    private Label m_statuBar;
    
    // The calculator for doing all the conversions
	private Uc2oolModel m_model;
	
	// Debug logger
	private Logger m_logger;
	private final static String LOGGER_NAME = "uc2ool";
	private final static String CLASS_NAME =
	        Uc2oolController.class.getName();
	private final static String DEBUG_FILE_NAME = "%t/uc2ool%g.log";
	
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        try {
            m_logger = new DebugLogger(LOGGER_NAME, DEBUG_FILE_NAME);
            
            m_logger.entering(CLASS_NAME, "initialize");
            
            if (m_process == null) {
            	m_logger.log(Level.SEVERE, 
            	    "fx:id=\"m_process\" was not injected: " +
            	    "check your FXML file 'IssueTrackingLite.fxml'.");
            }
            if (m_inputCharacter == null) {
            	m_logger.log(Level.SEVERE, 
            		"fx:id=\"m_inputCharacter\" was not injected: " +
            	    "check your FXML file 'IssueTrackingLite.fxml'.");
            }
            // Dump initial values of the radio buttons
            if (m_inputType != null) {
                StringBuilder sb = new StringBuilder("m_inputType buttons:\n");
                m_characterRB.setUserData(InputType.CHARACTER);
                m_hexCodePointRB.setUserData(InputType.HEXCODEPOINT);
                m_decimalCodePointRB.setUserData(InputType.DECCODEPOINT);
                m_UTF8EncodingRB.setUserData(InputType.UTF8);
                m_inputType.getToggles().forEach(
                        (rb) -> {sb.append("  ").append(rb.toString()).
                                 append(" ").append(rb.isSelected()).
                                 append("\n");});
                m_logger.log(Level.FINEST, sb.toString());
            }
            if (m_font != null) {
                // Get and sort all font names on the system and add
                // them to the m_font ComboBox.
                List<String> fonts = Font.getFontNames();
                Collections.sort(fonts);
                m_font.getItems().addAll(fonts);
                m_font.valueProperty().addListener(
                        new ChangeListener<String>() {
                                @Override
                                public void changed(ObservableValue<? extends String> o, String ov, String nv) {
                                    m_fontValue = nv;
                                    m_glyph.setFont(new Font(m_fontValue, m_fontSizeValue));
                                }
                });

                m_font.setValue(DEFAULT_FONT_NAME);
                
            }
            if (m_fontSize != null) {
                m_fontSize.getItems().addAll("5", "7", "9", "12", "14", "16",
                                             "18", "20", "24", "28", "32", "40",
                                             "45", "52", "60", "72", "80", "100",
                                             "130");
                m_fontSize.valueProperty().addListener(
                        new ChangeListener<String>() {
                                @Override
                                public void changed(ObservableValue<? extends String> o, String ov, String nv) {
                                    m_fontSizeValue = Double.valueOf(nv);
                                    m_glyph.setFont(new Font(m_fontValue, m_fontSizeValue));
                                }
                });
                m_fontSize.setValue(DEFAULT_FONT_SIZE);
            }
            connectToCalculator();
            
            m_logger.exiting(CLASS_NAME, "initialize");
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Called when the Process button is fired.
     *
     * @param event the action event.
     */
    @FXML
    void processFired(ActionEvent event) {
    	try {
            if (m_inputCharacter != null) {
            	m_logger.log(Level.FINEST, m_inputCharacter.getText());
            	
            	// Get the input type and prime Uc2oolModel
            	InputType type = 
            	    (InputType)
            	        ((RadioButton)m_inputType.getSelectedToggle()).
            	        getUserData();
            	m_model.setInput(m_inputCharacter.getText(), type);
            	m_logger.log(Level.FINEST, m_model.toString());
            	
            	// Now populate output display fields with data from the 
            	// Uc2oolModel
            	m_unicodeName.setText(m_model.getUnicodeCharacterName());
            	m_utf16Encoding.setText(m_model.getUTF16Encoding());
            	m_utf8Encoding.setText(m_model.getUTF8Encoding());
            	m_decimalCodePoint.setText(m_model.getDecimalCodePoint());
            	m_glyph.setFont(new Font(m_fontValue, m_fontSizeValue));
            	m_glyph.setText(m_model.getUnicodeCharacter());    		
            }
        } catch (Exception cre) {
            handleException(cre);
        }
    }

    /*
     * Handle exception popping up error dialogs to the user and logging
     * the error to the diagnostic log if required.
     * 
     * @param cre the CalculatorRuntimeException or subclass to handle
     */
    private void handleException(Exception e) {
        if (e instanceof Uc2oolRuntimeException) {
            
            showErrorDialog(e);
        } else {
            e.printStackTrace();
        }
    }
    
    private void showErrorDialog(Throwable e) {
        try {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("ErrorDialog.fxml"));
            Parent root = loader.load();
            ErrorController ec = loader.getController();
            ec.setErrorText(e.getLocalizedMessage());
            dialog.setScene(new Scene(root));
            dialog.setTitle("Error");
            dialog.show();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
    
    // Connect to the calculator model so that we can process Unicode character
    // conversions.
    private void connectToCalculator() {
        if (m_model == null) {
            m_model = new Uc2oolModel(m_logger);
        }
    }

}
