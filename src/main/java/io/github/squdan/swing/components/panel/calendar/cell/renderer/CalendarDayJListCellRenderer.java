package io.github.squdan.swing.components.panel.calendar.cell.renderer;

import io.github.squdan.swing.components.panel.calendar.cell.CalendarDayCell;
import io.github.squdan.swing.components.configuration.SwingComponents;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * {@link CalendarDayCellRenderer} implementation that uses {@link JList} internally as value container.
 * <p>
 * This implementation manages a list of String values built from {@link CalendarDayCell} values converted to String.
 */
public class CalendarDayJListCellRenderer extends JList<String> implements CalendarDayCellRenderer {

    /**
     * Generated Serial Version UID
     */
    @Serial
    private static final long serialVersionUID = 8283173142906633818L;

    // Configuration
    private static final Color selectedForegroundColor = (Color) UIManager.get("Table.dropCellForeground");
    private static final Color selectedBackgroundColor = (Color) UIManager.get("Table.dropCellBackground");

    private static final int LIMIT_CALENDAR_CELL_ITEMS = 4;
    private static final String MORE_ITEMS = " más...";
    private static final String BOLD_INIT = "<html><b>";
    private static final String BOLD_END = "</b></html>";

    // Today information
    private final int currentDay;
    private final int currentMonth;
    private final int currentYear;

    // Calendar state references
    private int selectedMonth;
    private int selectedYear;

    public CalendarDayJListCellRenderer() {
        super();

        // Set current date
        final GregorianCalendar calendarFromToday = new GregorianCalendar();
        this.currentDay = calendarFromToday.get(GregorianCalendar.DAY_OF_MONTH);
        this.currentMonth = calendarFromToday.get(GregorianCalendar.MONTH);
        this.currentYear = calendarFromToday.get(GregorianCalendar.YEAR);
        updateSelectedDate(this.currentMonth, this.currentYear);
    }

    @Override
    public void updateSelectedDate(final int userSelectedMonth, final int userSelectedYear) {
        this.selectedMonth = userSelectedMonth;
        this.selectedYear = userSelectedYear;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        // Clear previous data
        this.setListData(ArrayUtils.EMPTY_STRING_ARRAY);

        // Change color for weekend days
        if (column == 5 || column == 6) {
            setBackground(SwingComponents.getConfiguration().getColorConfiguration().getDanger());
        } else {
            setBackground(Color.WHITE);
        }

        // Custom value
        final CalendarDayCell calendarDayCellValue = (CalendarDayCell) value;

        if (Objects.nonNull(calendarDayCellValue)) {
            String[] listValues;

            // Draw just first values
            if (calendarDayCellValue.getValues().size() > LIMIT_CALENDAR_CELL_ITEMS) {
                listValues = new String[LIMIT_CALENDAR_CELL_ITEMS + 1];
                listValues[0] = getBoldText(calendarDayCellValue.getDay());

                IntStream.range(0, LIMIT_CALENDAR_CELL_ITEMS).forEach(i -> {
                    listValues[i + 1] = calendarDayCellValue.getValues().get(i).toString();
                });

                listValues[LIMIT_CALENDAR_CELL_ITEMS] = (calendarDayCellValue.getValues().size() - LIMIT_CALENDAR_CELL_ITEMS)
                        + MORE_ITEMS;
            }

            // Draw all values
            else {
                listValues = new String[calendarDayCellValue.getValues().size() + 1];
                listValues[0] = getBoldText(calendarDayCellValue.getDay());

                IntStream.range(0, calendarDayCellValue.getValues().size()).forEach(i -> {
                    listValues[i + 1] = calendarDayCellValue.getValues().get(i).toString();
                });
            }

            this.setListData(listValues);
        }

        // Change color for today
        if (Objects.nonNull(calendarDayCellValue) && calendarDayCellValue.getDay() == currentDay && selectedMonth == currentMonth
                && selectedYear == currentYear) {
            setBackground(SwingComponents.getConfiguration().getColorConfiguration().getWarning());
            setForeground(SwingComponents.getConfiguration().getColorConfiguration().getPrimaryText());
        } else {
            setForeground(Color.BLACK);
        }

        // Set selection colors for cell
        if (isSelected) {
            setForeground(
                    Objects.isNull(selectedForegroundColor) ? table.getSelectionForeground() : selectedForegroundColor);
            setBackground(
                    Objects.isNull(selectedBackgroundColor) ? table.getSelectionBackground() : selectedBackgroundColor);
        }

        return this;
    }

    private String getBoldText(final Object source) {
        return BOLD_INIT + source + BOLD_END;
    }
}