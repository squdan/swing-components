package io.github.squdan.swing.components.text;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlaceholderPasswordTextField extends JPasswordField {

	/** Generated Serial Version UID */
	@Serial
	private static final long serialVersionUID = 7435835769257943177L;

	private String placeholder;

	public PlaceholderPasswordTextField() {
		this(null, null);
	}

	public PlaceholderPasswordTextField(final String placeholder) {
		this(placeholder, null);
	}

	public PlaceholderPasswordTextField(final String placeholder, final String text) {
		super(text);
		this.placeholder = placeholder;

		// Masking behaviour
		setEchoChar('*'); // Hides all characters with '*'
	}

	@Override
	protected void paintComponent(final Graphics pG) {
		super.paintComponent(pG);

		// Draw placeholder
		if (StringUtils.isBlank(getPasswordAsString())) {
			final Graphics2D g = (Graphics2D) pG;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(getDisabledTextColor());
			g.drawString(placeholder, getInsets().left, pG.getFontMetrics().getMaxAscent() + getInsets().top);
		}
	}

	public String getPasswordAsString() {
		return new String(getPassword());
	}
}