package io.github.squdan.swing.components.panel.table.normal;

import io.github.squdan.swing.components.SwingComponentsView;
import io.github.squdan.swing.components.panel.table.common.action.TableActions;
import io.github.squdan.swing.components.panel.table.common.model.GenericTableModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class TableConfiguration<T, K> {

    /**
     * [Optional] Current source view rendering table.
     * <p>
     * Necessary to use refresh method to update the table.
     */
    private SwingComponentsView<T> view;

    /**
     * [Optional] View input to send with refresh method.
     */
    private T viewInput;

    /**
     * [Optional] Title to show as header.
     */
    private String title;

    /**
     * [Required] Table elements container that will be used to render, filter and search into.
     */
    private final GenericTableModel<K> tableModel;

    /**
     * [Optional] Available user actions over table and cells.
     * <p>
     * If null, no actions will be available for users.
     */
    private TableActions<K> tableActions;

    /**
     * [Optional] Flag to enable or disable filtering and sorting features.
     * <p>
     * If null, no filtering and sorting will be available.
     */
    private Boolean enableFilteringAndSorting;

}
