package io.github.squdan.swing.components.configuration;

import com.formdev.flatlaf.FlatIntelliJLaf;
import io.github.squdan.swing.components.SwingComponentsException;

import java.util.Objects;

/**
 * Swing-Components library configuration class.
 */
public class SwingComponents {

    private static SwingComponentsConfiguration configuration;

    public static void setup(final SwingComponentsConfiguration configuration) {
        SwingComponents.configuration = configuration;

        // Styles configuration (https://www.formdev.com/flatlaf/)
        // Intellij Themes
        // (https://github.com/JFormDesigner/FlatLaf/tree/main/flatlaf-intellij-themes)
        FlatIntelliJLaf.setup();
    }

    public static SwingComponentsConfiguration getConfiguration() {
        if (Objects.isNull(configuration)) {
            throw new SwingComponentsException("Configuration required. Use SwingComponents.setup(configuration) before use SwingComponents dependency.");
        }

        return configuration;
    }
}
