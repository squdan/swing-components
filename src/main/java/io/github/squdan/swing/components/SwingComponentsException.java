package io.github.squdan.swing.components;

/**
 * QueryDslFilter context exception.
 */
public class SwingComponentsException extends RuntimeException {

    /**
     * Constructor from message.
     *
     * @param msg: error message.
     */
    public SwingComponentsException(final String msg) {
        super(msg);
    }

    /**
     * Constructor from exception and message.
     *
     * @param exception: source exception.
     * @param msg:       error message.
     */
    public SwingComponentsException(final Throwable exception, final String msg) {
        super(msg, exception);
    }

}
