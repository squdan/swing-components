package io.github.squdan.swing.components;

import com.formdev.flatlaf.ui.FlatButtonBorder;

import java.awt.*;
import java.io.Serial;

/**
 * This class allows to change button border color which is protected at
 * FlatButtonBorder and can't be accesed directly.
 * 
 * @author Daniel Torres Rojano
 *
 */
public class CustomButtonBorder extends FlatButtonBorder {

	/** Generated Serial Version UID */
	@Serial
	private static final long serialVersionUID = 6666785477446930321L;

	public CustomButtonBorder(final Color color) {
		super();
		this.borderColor = color;
		this.defaultBorderColor = color;
	}

}
