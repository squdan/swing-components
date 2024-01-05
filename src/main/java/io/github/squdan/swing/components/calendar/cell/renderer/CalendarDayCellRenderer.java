package io.github.squdan.swing.components.calendar.cell.renderer;

import javax.swing.table.TableCellRenderer;

/**
 * Interface to define {@link TableCellRenderer} behaviour for {@link io.github.squdan.swing.components.calendar.cell.CalendarDayCell}.
 */
public interface CalendarDayCellRenderer extends TableCellRenderer {

    void updateSelectedDate(int userSelectedMonth, int userSelectedYear);
}
