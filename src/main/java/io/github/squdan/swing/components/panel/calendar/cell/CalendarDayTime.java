package io.github.squdan.swing.components.panel.calendar.cell;

import io.github.squdan.swing.components.panel.calendar.CalendarUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Time object that can be used in custom {@link CalendarDayCellValue} implementations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDayTime {

    private int hour;
    private int minutes;

    @Override
    public String toString() {
        return CalendarUtils.getTimeFormatted(hour, minutes);
    }
}
