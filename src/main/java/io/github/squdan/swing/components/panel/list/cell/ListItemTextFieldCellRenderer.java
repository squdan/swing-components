package io.github.squdan.swing.components.panel.list.cell;

import io.github.squdan.swing.components.SwingComponentsItem;
import io.github.squdan.swing.components.configuration.SwingComponents;
import io.github.squdan.swing.components.panel.list.EditableListPanel;
import io.github.squdan.swing.components.panel.list.ListPanel;
import io.github.squdan.swing.components.util.ViewUtils;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.Objects;

/**
 * {@link DefaultListCellRenderer} implementation that defines how {@link ListPanel} and {@link EditableListPanel}
 * should be rendered.
 * <p>
 * This implementation represents each element as a button. Elements must implement {@link SwingComponentsItem} to use defined
 * toTextField() method implementation to write result as button text content.
 */
public class ListItemTextFieldCellRenderer extends DefaultListCellRenderer {

    /**
     * Serial Version UID
     */
    @Serial
    private static final long serialVersionUID = -3583283682794360096L;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        JButton result = null;

        if (Objects.nonNull(value)) {
            result = new JButton();

            // Text configuration
            result.setForeground(SwingComponents.getConfiguration().getColorConfiguration().getPrimaryText());
            result.setFont(SwingComponents.getConfiguration().getTextConfiguration().getTitleSecondaryFont());

            // Background color configuration
            ViewUtils.changeButtonColorBackground(result, SwingComponents.getConfiguration().getColorConfiguration().getPrimary());

            // Text content configuration
            if (value instanceof SwingComponentsItem<?> valueCasted) {
                result.setText(valueCasted.toTextField());

                // If a color is configured for item, then change background
                if (Objects.nonNull(valueCasted.getColor())) {
                    ViewUtils.changeButtonColorBackground(result, valueCasted.getColor());
                }
            } else {
                result.setText(value.toString());
            }

            // Background color configuration for selected item
            if (isSelected) {
                ViewUtils.changeButtonColorBackground(result, selectedColor(result.getBackground()));
            }
        }

        return result;
    }

    private static Color selectedColor(final Color color) {
        int r = Math.min(255, color.getRed() + 70);
        int g = Math.min(255, color.getGreen() + 70);
        int b = Math.min(255, color.getBlue() + 70);
        return new Color(r, g, b);
    }
}
