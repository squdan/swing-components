package io.github.squdan.swing.components.calendar.cell.renderer;

import io.github.squdan.swing.components.calendar.cell.CalendarDayCell;
import io.github.squdan.swing.components.configuration.SwingComponents;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.Serial;
import java.util.GregorianCalendar;
import java.util.Objects;

public class CalendarDayJLabelCellRenderer extends DefaultTableCellRenderer implements CalendarDayCellRenderer {

	/** Generated Serial Version UID */
	@Serial
	private static final long serialVersionUID = -690754082536120115L;

	// Configuration
	private static final Color selectedForegroundColor = (Color) UIManager.get("Table.dropCellForeground");
	private static final Color selectedBackgroundColor = (Color) UIManager.get("Table.dropCellBackground");

	private static final String HTML_INIT = "<html>";
	private static final String HTML_END = "</html>";
	private static final String BOLD_INIT = "<b>";
	private static final String BOLD_END = "</b>";
	private static final String NEW_LINE = "<br>";

	private static final String ITEMS_DESC = " citas";

	// Today information
	private final int currentDay;
	private final int currentMonth;
	private final int currentYear;

	// Calendar state references
	private int selectedMonth;
	private int selectedYear;

	public CalendarDayJLabelCellRenderer() {
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
	public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
			final boolean hasFocus, final int row, final int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setHorizontalAlignment(SwingConstants.CENTER);

		// Change color for weekend days
		if (column == 5 || column == 6) {
			setBackground(SwingComponents.getConfiguration().getColorConfiguration().getDanger());
		} else {
			setBackground(Color.WHITE);
		}

		// Custom value
		final CalendarDayCell calendarDayCellValue = (CalendarDayCell) value;

		if (Objects.nonNull(calendarDayCellValue)) {
			final StringBuilder cellValue = new StringBuilder();
			cellValue.append(HTML_INIT);
			cellValue.append(BOLD_INIT);
			cellValue.append(getBoldText(calendarDayCellValue.getDay()));
			cellValue.append(BOLD_END);
			cellValue.append(NEW_LINE);
			cellValue.append(NEW_LINE);
			cellValue.append(calendarDayCellValue.getCustomInfo().size() + ITEMS_DESC);
			cellValue.append(HTML_END);
			this.setText(cellValue.toString());
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
		return BOLD_INIT + String.valueOf(source) + BOLD_END;
	}
}