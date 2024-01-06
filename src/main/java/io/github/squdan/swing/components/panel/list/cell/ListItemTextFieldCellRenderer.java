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
            result.setForeground(SwingComponents.getConfiguration().getColorConfiguration().getPrimaryText());
            result.setFont(SwingComponents.getConfiguration().getTextConfiguration().getTitleSecondaryFont());

            if (value instanceof SwingComponentsItem<?>) {
                final SwingComponentsItem<?> valueCasted = (SwingComponentsItem<?>) value;
                result.setText(valueCasted.toTextField());
            } else {
                result.setText(value.toString());
            }

            // Set selection colors for cell
            if (isSelected) {
                ViewUtils.changeButtonColorBackground(result, SwingComponents.getConfiguration().getColorConfiguration().getDanger());
            } else {
                ViewUtils.changeButtonColorBackground(result, SwingComponents.getConfiguration().getColorConfiguration().getPrimary());
            }
        }

        return result;
    }

}
