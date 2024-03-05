package io.github.squdan.swing.components.panel.table.cell;

import io.github.squdan.swing.components.SwingComponentsItem;
import io.github.squdan.swing.components.util.date.DateTimeUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class SwingComponentsTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value,
                                                   final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        // Reset previous configuration
        super.setBackground(null);
        super.setForeground(null);

        // Select value to draw into the cell
        Object cellValue = value;

        if (value instanceof SwingComponentsItem<?> valueCasted) {
            cellValue = valueCasted.toTextField();
        } else if (value instanceof Instant date) {
            cellValue = DateTimeUtils.InstantSourceMethods.toString(date, TimeZone.getDefault().toZoneId());
        } else if (value instanceof LocalDate date) {
            cellValue = DateTimeUtils.LocalDateSourceMethods.toString(date);
        } else if (value instanceof LocalDateTime date) {
            cellValue = DateTimeUtils.LocalDateTimeSourceMethods.toString(date);
        } else if (value instanceof Color) {
            cellValue = colorToString((Color) value);
        }

        // Call to DefaultTableCellRenderer to render value
        final Component result = super.getTableCellRendererComponent(table, cellValue, isSelected, hasFocus, row, column);

        // Change background if color configured
        if (value instanceof SwingComponentsItem<?> valueCasted) {
            super.setBackground(valueCasted.getColor());
        } else if (value instanceof Color) {
            super.setBackground((Color) value);
        }

        return result;
    }

    private static String colorToString(final Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }
}
