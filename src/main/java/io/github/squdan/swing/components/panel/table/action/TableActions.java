package io.github.squdan.swing.components.panel.table.action;

import io.github.squdan.swing.components.panel.calendar.action.CalendarDataManagerService;
import io.github.squdan.swing.components.panel.calendar.cell.CalendarDayCell;
import io.github.squdan.swing.components.panel.table.TablePanel;
import io.github.squdan.swing.components.panel.table.model.ColumnInfo;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Default table actions that user can apply over each cell into {@link TablePanel}.
 *
 * @param <T> Data type.
 * @author Daniel Torres Rojano
 */
@Slf4j
public class TableActions<T> {

    // Services
    protected final TableDataManagerService<T> service;

    // Components: global actions
    protected final JPopupMenu tableActionsMenu = new JPopupMenu();
    protected final JMenuItem globalActionNew = new JMenuItem("Nuevo");

    // Components: table actions
    protected final JPopupMenu cellActionsMenu = new JPopupMenu();
    protected final JMenuItem tableActionSee = new JMenuItem("Ver");
    protected final JMenuItem tableActionNew = new JMenuItem("Nuevo");
    protected final JMenuItem tableActionUpdate = new JMenuItem("Actualizar");
    protected final JMenuItem tableActionDelete = new JMenuItem("Eliminar");

    /**
     * Constructor that enables default actions:
     * <p>
     * - See: uses {@link TableDataManagerService} see method.
     * - Create: uses {@link TableDataManagerService} create method.
     * - Update: uses {@link TableDataManagerService} update method.
     * - Delete: uses {@link TableDataManagerService} delete method.
     *
     * @param service: {@link CalendarDataManagerService} implementation.
     */
    public TableActions(final TableDataManagerService<T> service) {
        this(service, true);
    }

    /**
     * Constructor that NOT enables default actions. If user don't add manually some actions, no actions
     * will be available over the table cells.
     * <p>
     * Developers could extend this class to add their custom actions using this constructor method.
     *
     * @param service: {@link TableDataManagerService} implementation.
     */
    protected TableActions(final TableDataManagerService<T> service, final boolean useDefaultActions) {
        this.service = service;

        if (useDefaultActions) {
            // Initialize global actions JPopupMenu
            tableActionsMenu.add(globalActionNew);

            // Initialize table actions JPopupMenu
            cellActionsMenu.add(tableActionSee);
            cellActionsMenu.add(tableActionNew);
            cellActionsMenu.add(tableActionUpdate);
            cellActionsMenu.add(tableActionDelete);
        }
    }

    /**
     * Method used at {@link TablePanel} when some {@link java.awt.event.ActionEvent} is caught.
     *
     * @param source     {@link ActionEvent} source event.
     * @param command    {@link ActionEvent} action command.
     * @param cellValue  {@link CalendarDayCell} cell value where user clicked to select some action.
     * @param columnInfo {@link ColumnInfo} from selected cell from user.
     * @param row        from selected cell from user.
     * @param column     from selected cell from user.
     * @return true if refresh needed.
     */
    public boolean manageActionEvents(final Object source, final String command, final T cellValue, final Object columnInfo,
                                      final int row, final int column) {
        if (globalActionNew == source) {
            service.create(command, cellValue, columnInfo);
        } else if (tableActionSee == source) {
            service.see(command, cellValue, columnInfo);
        } else if (tableActionNew == source) {
            service.create(command, cellValue, columnInfo);
        } else if (tableActionUpdate == source) {
            service.update(command, cellValue, columnInfo);
        } else if (tableActionDelete == source) {
            service.delete(command, cellValue, columnInfo);
        } else {
            log.warn("Acción desconocida sobre la fila '{}' en el elemento '{}' en fila '{}' y columna '{}'", cellValue,
                    columnInfo, row, column);
        }

        return true;
    }

    /**
     * Method used at {@link TablePanel} to detect available actions
     * over any location of the table and offer them to the users.
     *
     * @return JPopupMenu actions menu.
     */
    public JPopupMenu getAvailableTableActions() {
        return tableActionsMenu;
    }

    /**
     * Method used at {@link TablePanel} to detect available actions
     * over cell elements and offer them to the users.
     *
     * @return JPopupMenu actions menu.
     */
    public JPopupMenu getAvailableCellActions() {
        return cellActionsMenu;
    }
}
