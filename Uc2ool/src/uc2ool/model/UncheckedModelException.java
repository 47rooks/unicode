/**
 * Copyright Daniel Semler 2015
 */
package uc2ool.model;

import uc2ool.Uc2oolRuntimeException;

/**
 * Objects of this class represent an exception in the calculator (model)
 * and will contain translatable parameterised messages.
 * 
 * @author dsemler
 *
 */
public class UncheckedModelException extends Uc2oolRuntimeException {

    /**
     * First version
     */
    private static final long serialVersionUID = 1L;

    /*
     * Constructor taking message key and arguments to be substituted
     * 
     * @param msgKey the resource bundle message key for the message
     * @param arguments required by the message substitution string
     */
    UncheckedModelException(String msgKey, Object... args) {
        super(msgKey, args);
    }
}
