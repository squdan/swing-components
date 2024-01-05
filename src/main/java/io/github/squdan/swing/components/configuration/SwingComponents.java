package io.github.squdan.swing.components.configuration;

import io.github.squdan.swing.components.SwingComponentsException;

import java.util.Objects;

public class SwingComponents {

    private static SwingComponentsConfiguration configuration;

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
