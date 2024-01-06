package io.github.squdan.swing.components.panel.calendar.cell.renderer;

import io.github.squdan.swing.components.panel.calendar.cell.CalendarDayCell;

import javax.swing.table.TableCellRenderer;

/**
 * Interface to define {@link TableCellRenderer} behaviour for {@link CalendarDayCell}.
 */
public interface CalendarDayCellRenderer extends TableCellRenderer {

    void updateSelectedDate(int userSelectedMonth, int userSelectedYear);
}
