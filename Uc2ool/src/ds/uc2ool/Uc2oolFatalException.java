/**
 * Copyright Daniel Semler 2015
 */
package ds.uc2ool;

/**
 * Objects of this class represent aa fatal exception in Uc2ool. This usually
 * means that it is a bug or at the very least development assistance will be
 * required to debug the problem. It should be used rarely and with care.
 * 
 * @author  Daniel Semler
 * @version %I%, %G%
 * @since   1.0
 */
public class Uc2oolFatalException extends Uc2oolRuntimeException {

    /**
     * First version
     */
    private static final long serialVersionUID = 1L;

    /*
     * Constructor taking message key and arguments to be substituted
     * 
     * @param msgKey the resource bundle message key for the message
     * @param args arguments required by the message substitution string
     */
    public Uc2oolFatalException(String msgKey, Object... args) {
        super(msgKey, args);
    }
}
