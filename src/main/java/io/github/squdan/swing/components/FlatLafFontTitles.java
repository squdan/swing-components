package io.github.squdan.swing.components;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

/**
 * Fonts availables by FlatLaf (https://www.formdev.com/flatlaf/typography/)
 */
@Getter
public enum FlatLafFontTitles {
    FONT_TITLE_00("h00.font"), FONT_TITLE_0("h0.font"), FONT_TITLE_1("h1.font"), FONT_TITLE_2("h2.font"),
    FONT_TITLE_3("h3.font"), FONT_TITLE_4("h4.font");

    // Params
    private final String name;
    private final int size;
    private final Font font;

    // Constructor
    FlatLafFontTitles(final String name) {
        this.name = name;
        this.font = UIManager.getFont(name);
        this.size = this.font.getSize();
    }

    public static FlatLafFontTitles getBySize(final int size) {
        FlatLafFontTitles result;

        // Search for closest title size
        final Optional<FlatLafFontTitles> mayFontTitle = Arrays.stream(FlatLafFontTitles.values())
                .filter(f -> ((f.size - size) <= 3 && (f.size - size) >= -3)).findFirst();

        // DEFAULT
        result = mayFontTitle.orElse(FlatLafFontTitles.FONT_TITLE_3);

        return result;
    }
}
