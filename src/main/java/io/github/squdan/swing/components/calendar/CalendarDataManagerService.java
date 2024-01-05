package io.github.squdan.swing.components.calendar;

import io.github.squdan.swing.components.calendar.cell.CalendarDayCell;

public interface CalendarDataManagerService extends CalendarDataProviderService {

    void see(CalendarDayCell source);

    void create(CalendarDayCell source);

    void delete(CalendarDayCell source);

}
