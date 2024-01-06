package io.github.squdan.swing.components.configuration;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import io.github.squdan.swing.components.SwingComponentsException;

import java.util.Objects;

/**
 * Swing-Components library configuration class.
 */
public class SwingComponents {

    private static SwingComponentsConfiguration configuration;

    /**
     * Initializes FlatLaf UI configuration with default theme "FlatIntelliJLaf".
     */
    public static void initFlatLaf() {
        initFlatLaf(null);
    }

    /**
     * Initializes FlatLaf UI configuration with selected theme.
     *
     * @param theme to apply.
     */
    public static void initFlatLaf(final FlatLaf theme) {
        // Styles configuration (https://www.formdev.com/flatlaf/)
        // Intellij Themes
        // (https://github.com/JFormDesigner/FlatLaf/tree/main/flatlaf-intellij-themes)
        if (Objects.nonNull(theme)) {
            FlatLaf.setup(theme);
        } else {
            FlatIntelliJLaf.setup();
        }
    }

    public static void setup(final SwingComponentsConfiguration configuration) {
        SwingComponents.configuration = configuration;
    }

    public static SwingComponentsConfiguration getConfiguration() {
        if (Objects.isNull(configuration)) {
            throw new SwingComponentsException("Configuration required. Use SwingComponents.setup(configuration) before use SwingComponents dependency.");
        }

        return configuration;
    }
}
