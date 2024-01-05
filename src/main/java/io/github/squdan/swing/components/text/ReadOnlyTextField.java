package io.github.squdan.swing.components.text;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReadOnlyTextField extends JTextField {

	/** Generated Serial Version UID */
	@Serial
	private static final long serialVersionUID = -3657295021209136055L;

	public ReadOnlyTextField() {
		this(null);
	}

	public ReadOnlyTextField(final String text) {
		super(text);
		this.setEditable(false);
	}
}