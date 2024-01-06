package io.github.squdan.swing.components.calendar.action;

import io.github.squdan.swing.components.calendar.CalendarDataProviderService;
import io.github.squdan.swing.components.calendar.cell.CalendarDayCell;

/**
 * Interface to define service actions to use into {@link CalendarDayActions} that will be offered in each cell
 * from {@link io.github.squdan.swing.components.calendar.CalendarPanel} with mouse right click.
 */
public interface CalendarDataManagerService extends CalendarDataProviderService {

    /**
     * Shows cell information with details.
     *
     * @param source: {@link CalendarDayCell} cell information.
     */
    void see(CalendarDayCell source);

    /**
     * Creates new cell element.
     *
     * @param source: {@link CalendarDayCell} cell information.
     */
    void create(CalendarDayCell source);

    /**
     * Deletes selected cell element.
     *
     * @param source: {@link CalendarDayCell} cell information.
     */
    void delete(CalendarDayCell source);

}
