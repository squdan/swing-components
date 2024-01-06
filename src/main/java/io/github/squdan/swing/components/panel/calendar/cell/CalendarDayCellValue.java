package io.github.squdan.swing.components.panel.calendar.cell;

import java.util.UUID;

/**
 * Interface that defines required information to be returned from custom values used at {@link CalendarDayCell}.
 */
public interface CalendarDayCellValue {

    UUID getId();

    String getTimeAsString();

    String getValueAsString();

}
