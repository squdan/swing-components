package io.github.squdan.swing.components.calendar;

import io.github.squdan.swing.components.calendar.cell.CalendarDayCell;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Default calendar actions that user can apply over each cell into {@link CalendarPanel}.
 */
@Slf4j
public class CalendarDayActions {

    // Services
    private final CalendarDataManagerService service;

    // Components
    private final JPopupMenu actionsMenu = new JPopupMenu();
    private final JMenuItem actionItemSee = new JMenuItem("Ver");
    private final JMenuItem actionItemAdd = new JMenuItem("Añadir");
    private final JMenuItem actionItemDelete = new JMenuItem("Eliminar");

    /**
     * Constructor that enables default actions:
     * - See: uses {@link CalendarDataManagerService} see method.
     * - Create: uses {@link CalendarDataManagerService} create method.
     * - Delete: uses {@link CalendarDataManagerService} delete method.
     *
     * @param service: {@link CalendarDataManagerService} implementation.
     */
    public CalendarDayActions(final CalendarDataManagerService service) {
        this(service, true);
    }

    /**
     * Constructor that NOT enables default actions. If user don't add manually some actions, no actions
     * will be available over the calendar cells.
     * <p>
     * Developers could extend this class to add their custom actions using this constructor method.
     *
     * @param service: {@link CalendarDataManagerService} implementation.
     */
    public CalendarDayActions(final CalendarDataManagerService service, final boolean useDefaultActions) {
        this.service = service;

        if (useDefaultActions) {
            // Configure actions menu
            actionsMenu.add(actionItemSee);
            actionsMenu.add(actionItemAdd);
            actionsMenu.add(actionItemDelete);
        }
    }

    /**
     * Method used at {@link CalendarPanel} when some {@link java.awt.event.ActionEvent} is caught.
     *
     * @param calendar  panel instance.
     * @param source    {@link ActionEvent} source event.
     * @param command   {@link ActionEvent} action command.
     * @param cellValue {@link CalendarDayCell} cell value where user clicked to select some action.
     * @param row       from selected cell from user.
     * @param column    from selected cell from user.
     */
    public void manageActionEvents(final CalendarPanel calendar, final Object source, final String command, final CalendarDayCell cellValue, final int row, final int column) {
        if (actionItemSee == source) {
            service.see(cellValue);
        } else if (actionItemAdd == source) {
            service.create(cellValue);
            calendar.refresh();
        } else if (actionItemDelete == source) {
            service.delete(cellValue);
            calendar.refresh();
        } else {
            log.warn("Acción desconocida sobre elemento '{}' en fila '{}' y columna '{}'", cellValue, row, column);
        }
    }

    /**
     * Method used at {@link CalendarPanel} to detect available actions and offer them to the users.
     *
     * @return JPopupMenu actions menu.
     */
    public JPopupMenu getAvailableActions() {
        return actionsMenu;
    }

}
