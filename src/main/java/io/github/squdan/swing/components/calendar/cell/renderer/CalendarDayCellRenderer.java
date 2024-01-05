package io.github.squdan.swing.components.calendar.cell.renderer;

import javax.swing.table.TableCellRenderer;

public interface CalendarDayCellRenderer extends TableCellRenderer {

	public void updateSelectedDate(final int userSelectedMonth, final int userSelectedYear);
}
