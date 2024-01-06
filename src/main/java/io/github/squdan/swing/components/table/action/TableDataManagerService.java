package io.github.squdan.swing.components.table.action;

import io.github.squdan.swing.components.calendar.cell.CalendarDayCell;

/**
 * Interface to define service actions to use into {@link TableActions} that will be offered in each cell
 * from {@link io.github.squdan.swing.components.table.TablePanel} with mouse right click.
 */
public interface TableDataManagerService<T> {

    /**
     * Shows cell information with details.
     *
     * @param command:     operation information.
     * @param cellElement: {@link CalendarDayCell} cell information.
     * @param columnInfo:  {@link io.github.squdan.swing.components.table.model.ColumnInfo} column information.
     */
    void see(final String command, final T cellElement, final Object columnInfo);

    /**
     * Creates new cell element.
     *
     * @param command:     operation information.
     * @param cellElement: {@link CalendarDayCell} cell information.
     * @param columnInfo:  {@link io.github.squdan.swing.components.table.model.ColumnInfo} column information.
     */
    void create(final String command, final T cellElement, final Object columnInfo);

    /**
     * Updates selected cell element.
     *
     * @param command:     operation information.
     * @param cellElement: {@link CalendarDayCell} cell information.
     * @param columnInfo:  {@link io.github.squdan.swing.components.table.model.ColumnInfo} column information.
     */
    void update(final String command, final T cellElement, final Object columnInfo);

    /**
     * Deletes selected cell element.
     *
     * @param command:     operation information.
     * @param cellElement: {@link CalendarDayCell} cell information.
     * @param columnInfo:  {@link io.github.squdan.swing.components.table.model.ColumnInfo} column information.
     */
    void delete(final String command, final T cellElement, final Object columnInfo);

}
