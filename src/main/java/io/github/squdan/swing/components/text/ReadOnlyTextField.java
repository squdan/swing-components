package io.github.squdan.swing.components.text;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.io.Serial;

/**
 * Non-editable text field.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReadOnlyTextField extends JTextField {

    @Serial
    private static final long serialVersionUID = -3657295021209136055L;

    /**
     * Default constructor.
     *
     * @param text: default text value.
     */
    public ReadOnlyTextField(final String text) {
        super(text);
        this.setEditable(false);
    }
}