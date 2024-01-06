package io.github.squdan.swing.components.panel.calendar;

import io.github.squdan.swing.components.panel.calendar.cell.CalendarDayCell;

import java.util.concurrent.CompletableFuture;

/**
 * Interface to define required method from {@link CalendarPanel} to obtain {@link CalendarDayCell} information
 * when rendering each month.
 */
public interface CalendarDataProviderService {

    /**
     * Async method to obtain {@link CalendarDayCell} when information it's ready. Method must be async to render calendar
     * without stay waiting all information is ready.
     *
     * @param year   selected.
     * @param month  selected.
     * @param day    selected.
     * @param row    from day selected.
     * @param column from day selected.
     * @return CompletableFuture to obtain {@link CalendarDayCell} when it's ready.
     */
    CompletableFuture<CalendarDayCell> get(int year, int month, int day, int row, int column);

}
