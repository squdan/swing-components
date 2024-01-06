package io.github.squdan.swing.components.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.awt.*;

/**
 * Model that contains required configuration by Swing-Components library.
 */
@Getter
@Builder
@AllArgsConstructor
public class SwingComponentsConfiguration {

    /**
     * Text configuration.
     */
    private SwingComponentsTextConfiguration textConfiguration;

    /**
     * Colors configuration.
     */
    private SwingComponentsColorConfiguration colorConfiguration;

    /**
     * Text configuration.
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class SwingComponentsTextConfiguration {
        private Font defaultText;
        private Font titleFont;
        private Font titleSecondaryFont;
    }

    /**
     * Colors configuration.
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class SwingComponentsColorConfiguration {

        // Main configuration
        private Color primary;
        private Color primaryText;

        private Color secondary;
        private Color secondaryText;

        // Notification configuration
        private Color success;
        private Color danger;
        private Color warning;
    }
}
