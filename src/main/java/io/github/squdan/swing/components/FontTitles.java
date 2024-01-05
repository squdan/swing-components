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
public enum FontTitles {
	FONT_TITLE_00("h00.font"), FONT_TITLE_0("h0.font"), FONT_TITLE_1("h1.font"), FONT_TITLE_2("h2.font"),
	FONT_TITLE_3("h3.font"), FONT_TITLE_4("h4.font");

	// Params
	private final String name;
	private final int size;
	private final Font font;

	// Constructor
	private FontTitles(final String name) {
		this.name = name;
		this.font = UIManager.getFont(name);
		this.size = this.font.getSize();
	}

	public static FontTitles getBySize(final int size) {
		FontTitles result;

		// Search for closest title size
		final Optional<FontTitles> mayFontTitle = Arrays.stream(FontTitles.values())
				.filter(f -> ((f.size - size) <= 3 && (f.size - size) >= -3)).findFirst();

        // DEFAULT
        result = mayFontTitle.orElse(FontTitles.FONT_TITLE_3);

		return result;
	}
}
