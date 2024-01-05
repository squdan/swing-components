package io.github.squdan.swing.components.calendar;

import io.github.squdan.swing.components.calendar.cell.CalendarDayCell;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

@Slf4j
public class CalendarDayActions {

    // Services
    private final CalendarDataManagerService service;

    // Components
    private final JPopupMenu actionsMenu = new JPopupMenu();
    private final JMenuItem actionItemSee = new JMenuItem("Ver");
    private final JMenuItem actionItemAdd = new JMenuItem("Añadir");
    private final JMenuItem actionItemDelete = new JMenuItem("Eliminar");

    public CalendarDayActions(final CalendarDataManagerService service) {
        this.service = service;

        // Configure actions menu
        actionsMenu.add(actionItemSee);
        actionsMenu.add(actionItemAdd);
        actionsMenu.add(actionItemDelete);
    }

    public JPopupMenu getAvailableActions() {
        return actionsMenu;
    }

    public void manageActionEvents(final CalendarPanel calendar, final Object source, final String command, final CalendarDayCell data, final int row,
                                   final int column) {
        if (actionItemSee == source) {
            service.see(data);
        } else if (actionItemAdd == source) {
            service.create(data);
            calendar.refresh();
        } else if (actionItemDelete == source) {
            service.delete(data);
            calendar.refresh();
        } else {
            log.warn("Acción desconocida sobre elemento '{}' en fila '{}' y columna '{}'", data, row, column);
        }
    }

}
