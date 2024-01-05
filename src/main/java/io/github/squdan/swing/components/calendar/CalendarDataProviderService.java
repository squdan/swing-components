package io.github.squdan.swing.components.calendar;

import io.github.squdan.swing.components.calendar.cell.CalendarDayCell;

import java.util.concurrent.CompletableFuture;

public interface CalendarDataProviderService {

	CalendarDayCell get(int year, int month, int day, int row, int column);

	CompletableFuture<CalendarDayCell> getAsync(int year, int month, int day, int row, int column);

}
