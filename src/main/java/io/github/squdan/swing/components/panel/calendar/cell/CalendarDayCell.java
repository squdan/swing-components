package io.github.squdan.swing.components.panel.calendar.cell;

import io.github.squdan.swing.components.panel.calendar.CalendarPanel;
import io.github.squdan.swing.components.util.date.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cell element from {@link CalendarPanel} with information about
 * the day/month/year from the calendar and a list of values associated to this day.
 */
@Data
@AllArgsConstructor
public class CalendarDayCell {

    // Configuration
    private static final String HTML_INIT = "<html>";
    private static final String HTML_END = "</html>";
    private static final String BOLD_INIT = "<b>";
    private static final String BOLD_END = "</b>";
    private static final String NEW_LINE = "<br>";

    // Information
    private int day;
    private int month;
    private int year;

    private List<CalendarDayCellValue> values;

    public CalendarDayCell(final int year, final int month, final int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.values = new ArrayList<>();
    }

    public String getDateAsString() {
        return DateTimeUtils.LocalDateSourceMethods.toString(getLocalDate());
    }

    public Instant getInstantUtc() {
        return DateTimeUtils.LocalDateSourceMethods.toInstant(getLocalDate());
    }

    public LocalDate getLocalDate() {
        return LocalDate.of(year, month, day);
    }

    public String getValuesTimeAsString() {
        final StringBuilder result = new StringBuilder();
        result.append(HTML_INIT);

        if (CollectionUtils.isNotEmpty(values)) {
            values.stream().forEach(i -> {
                result.append(NEW_LINE);
                result.append(i.getTimeAsString());
            });
        }

        result.append(HTML_END);
        return result.toString();
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(HTML_INIT);
        result.append(BOLD_INIT);
        result.append(day);
        result.append(BOLD_END);

        if (CollectionUtils.isNotEmpty(values)) {
            values.stream().forEach(a -> {
                result.append(NEW_LINE);
                result.append(a.getValueAsString());
            });
        }
        result.append(HTML_END);

        return result.toString();
    }
}
