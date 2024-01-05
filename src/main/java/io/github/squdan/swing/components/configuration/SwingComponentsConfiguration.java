package io.github.squdan.swing.components.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.awt.*;

@Getter
@Builder
@AllArgsConstructor
public class SwingComponentsConfiguration {

    private SwingComponentsTextConfiguration textConfiguration;
    private SwingComponentsColorConfiguration colorConfiguration;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SwingComponentsTextConfiguration {
        private Font defaultText;
        private Font titleFont;
        private Font titleSecondaryFont;
    }

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
