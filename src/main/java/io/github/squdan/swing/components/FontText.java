package io.github.squdan.swing.components;

import java.awt.Font;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.UIManager;

import lombok.Getter;

/**
 * Fonts availables by FlatLaf (https://www.formdev.com/flatlaf/typography/)
 */
@Getter
public enum FontText {
	FONT_LARGE("large.font"), FONT_DEFAULT("defaultFont"), FONT_MEDIUM("medium.font"), FONT_SMALL("small.font"),
	FONT_MINI("mini.font"), FONT_BOLD("semibold.font");

	// Params
	private final String name;
	private final int size;
	private final Font font;

	// Constructor
	private FontText(final String name) {
		this.name = name;
		this.font = UIManager.getFont(name);
		this.size = this.font.getSize();
	}

	public static FontText getBySize(final int size) {
		FontText result;

		// Search for closest title size
		final Optional<FontText> mayFontTitle = Arrays.stream(FontText.values())
				.filter(f -> ((f.size - size) <= 1 && (f.size - size) >= -1)).findFirst();

        // DEFAULT
        result = mayFontTitle.orElse(FontText.FONT_DEFAULT);

		return result;
	}
}
