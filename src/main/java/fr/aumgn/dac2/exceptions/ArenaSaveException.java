package fr.aumgn.dac2.exceptions;

/**
 * Thrown when the creation of a .json arena file failed.
 */
public class ArenaSaveException extends DACException {

    private static final long serialVersionUID = 8633465801666617065L;

    public ArenaSaveException(String message) {
        super(message);
    }
}
