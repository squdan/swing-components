package io.github.squdan.swing.components.table;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

/**
 * Actions available at the table.
 * 
 * @author Daniel Torres Rojano
 *
 * @param <T> Data type.
 */
@Slf4j
public class TableActions<T> {

	// Services
	protected final TableDataManagerService<T> service;

	// Components: global actions
	protected final JPopupMenu globalActionsMenu = new JPopupMenu();
	protected final JMenuItem globalActionNew = new JMenuItem("Nuevo");

	// Components: table actions
	protected final JPopupMenu tableActionsMenu = new JPopupMenu();
	protected final JMenuItem tableActionSee = new JMenuItem("Ver");
	protected final JMenuItem tableActionNew = new JMenuItem("Nuevo");
	protected final JMenuItem tableActionUpdate = new JMenuItem("Actualizar");
	protected final JMenuItem tableActionDelete = new JMenuItem("Eliminar");

	public TableActions(final TableDataManagerService<T> service) {
		this(service, true);
	}

	protected TableActions(final TableDataManagerService<T> service, final boolean useDefaultActions) {
		this.service = service;

		if (useDefaultActions) {
			// Initialize global actions JPopupMenu
			globalActionsMenu.add(globalActionNew);

			// Initialize table actions JPopupMenu
			tableActionsMenu.add(tableActionSee);
			tableActionsMenu.add(tableActionNew);
			tableActionsMenu.add(tableActionUpdate);
			tableActionsMenu.add(tableActionDelete);
		}
	}

	public JPopupMenu getAvailableGlobalActions() {
		return globalActionsMenu;
	}

	public JPopupMenu getAvailableTableActions() {
		return tableActionsMenu;
	}

	public void manageActionEvents(final Object source, final String command, final T rowData, final Object columnData,
			final int row, final int column) {
		if (globalActionNew == source) {
			service.create(command, rowData, columnData);
		} else if (tableActionSee == source) {
			service.see(command, rowData, columnData);
		} else if (tableActionNew == source) {
			service.create(command, rowData, columnData);
		} else if (tableActionUpdate == source) {
			service.update(command, rowData, columnData);
		} else if (tableActionDelete == source) {
			service.delete(command, rowData, columnData);
		} else {
			log.warn("Acción desconocida sobre la fila '{}' en el elemento '{}' en fila '{}' y columna '{}'", rowData,
					columnData, row, column);
		}
	}

}
