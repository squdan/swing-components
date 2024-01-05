package io.github.squdan.swing.components.text;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.Serial;

import javax.swing.JTextField;

import io.github.squdan.swing.components.configuration.SwingComponents;
import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlaceholderTextField extends JTextField {

	/** Generated Serial Version UID */
	@Serial
	private static final long serialVersionUID = 3788430018309889126L;

	private String placeholder;

	public PlaceholderTextField() {
		this(null, null);
	}

	public PlaceholderTextField(final String placeholder) {
		this(placeholder, null);
	}

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