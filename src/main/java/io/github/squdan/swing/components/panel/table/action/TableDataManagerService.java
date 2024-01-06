package io.github.squdan.swing.components.panel.table.action;

import io.github.squdan.swing.components.panel.calendar.cell.CalendarDayCell;
import io.github.squdan.swing.components.panel.table.TablePanel;
import io.github.squdan.swing.components.panel.table.model.ColumnInfo;

/**
 * Interface to define service actions to use into {@link TableActions} that will be offered in each cell
 * from {@link TablePanel} with mouse right click.
 */
public interface TableDataManagerService<T> {

    /**
     * Shows cell information with details.
     *
     * @param command:     operation information.
     * @param cellElement: {@link CalendarDayCell} cell information.
     * @param columnInfo:  {@link ColumnInfo} column information.
     */
    void see(final String command, final T cellElement, final Object columnInfo);

    /**
     * Creates new cell element.
     *
     * @param command:     operation information.
     * @param cellElement: {@link CalendarDayCell} cell information.
     * @param columnInfo:  {@link ColumnInfo} column information.
     */
    void create(final String command, final T cellElement, final Object columnInfo);

    /**
     * Updates selected cell element.
     *
     * @param command:     operation information.
     * @param cellElement: {@link CalendarDayCell} cell information.
     * @param columnInfo:  {@link ColumnInfo} column information.
     */
    void update(final String command, final T cellElement, final Object columnInfo);

    /**
     * Deletes selected cell element.
     *
     * @param command:     operation information.
     * @param cellElement: {@link CalendarDayCell} cell information.
     * @param columnInfo:  {@link ColumnInfo} column information.
     */
    void delete(final String command, final T cellElement, final Object columnInfo);

}
