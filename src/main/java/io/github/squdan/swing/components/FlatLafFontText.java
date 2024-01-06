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
public enum FlatLafFontText {
    FONT_LARGE("large.font"), FONT_DEFAULT("defaultFont"), FONT_MEDIUM("medium.font"), FONT_SMALL("small.font"),
    FONT_MINI("mini.font"), FONT_BOLD("semibold.font");

    // Params
    private final String name;
    private final int size;
    private final Font font;

    // Constructor
    FlatLafFontText(final String name) {
        this.name = name;
        this.font = UIManager.getFont(name);
        this.size = this.font.getSize();
    }

    public static FlatLafFontText getBySize(final int size) {
        FlatLafFontText result;

        // Search for closest title size
        final Optional<FlatLafFontText> mayFontTitle = Arrays.stream(FlatLafFontText.values())
                .filter(f -> ((f.size - size) <= 1 && (f.size - size) >= -1)).findFirst();

        // DEFAULT
        result = mayFontTitle.orElse(FlatLafFontText.FONT_DEFAULT);

        return result;
    }
}
