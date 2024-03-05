package io.github.squdan.swing.components;

import io.github.squdan.swing.components.panel.color.ColorSelectorPanel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultSwingComponentsItem implements Comparable<DefaultSwingComponentsItem>, SwingComponentsItem<String> {

    // Configuration
    private static final String SPLITTER = " - ";

    // Data
    private String id;
    private String value;
    private Color color;

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        if (Objects.nonNull(id)) {
            result.append(id);
        }

        if (Objects.nonNull(value)) {
            if (!result.isEmpty()) {
                result.append(SPLITTER);
            }

            result.append(value);
        }

        if (Objects.nonNull(color)) {
            if (!result.isEmpty()) {
                result.append(SPLITTER);
            }

            result.append(ColorSelectorPanel.colorToString(color));
        }

        return result.toString();
    }

    @Override
    public String toTextField() {
        final StringBuilder result = new StringBuilder();

        if (Objects.nonNull(id)) {
            result.append(id);
        }

        if (Objects.nonNull(value) && !value.equals(id)) {
            if (!result.isEmpty()) {
                result.append(SPLITTER);
            }

            result.append(value);
        }

        if (Objects.nonNull(color)) {
            if (!result.isEmpty()) {
                result.append(SPLITTER);
            }

            result.append(ColorSelectorPanel.colorToString(color));
        }

        return result.toString();
    }

    @Override
    public int compareTo(final DefaultSwingComponentsItem compareWith) {
        return this.value.compareTo(compareWith.getValue());
    }
}
