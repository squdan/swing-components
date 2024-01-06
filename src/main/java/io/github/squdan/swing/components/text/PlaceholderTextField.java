package io.github.squdan.swing.components.text;

import io.github.squdan.swing.components.configuration.SwingComponents;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

/**
 * {@link JTextField} implementation that shows some placeholder when input text is empty.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlaceholderTextField extends JTextField {

    @Serial
    private static final long serialVersionUID = 3788430018309889126L;

    private String placeholder;

    /**
     * Default {@link JTextField} without placeholder.
     */
    public PlaceholderTextField() {
        this(null, null);
    }

    /**
     * Constructor with placeholder, which will be showed when input is empty.
     *
     * @param placeholder: text to show.
     */
    public PlaceholderTextField(final String placeholder) {
        this(placeholder, null);
    }

    /**
     * Constructor with placeholder and default value.
     * <p>
     * Placeholder won't be show while this {@link JTextField} contains text.
     *
     * @param placeholder: text to show when value is empty.
     * @param text:        default text value in the {@link JTextField}.
     */
    public PlaceholderTextField(final String placeholder, final String text) {
        super(text);
        this.placeholder = placeholder;
        this.setFont(SwingComponents.getConfiguration().getTextConfiguration().getDefaultText());
    }

    @Override
    protected void paintComponent(final Graphics pG) {
        super.paintComponent(pG);

        // Draw placeholder
        if (StringUtils.isBlank(getText()) && StringUtils.isNotBlank(placeholder)) {
            final Graphics2D g = (Graphics2D) pG;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(getDisabledTextColor());
            g.drawString(placeholder, getInsets().left, pG.getFontMetrics().getMaxAscent() + getInsets().top);
        }
    }
}