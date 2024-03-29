package io.github.squdan.swing.components.panel.table.paginated;

import io.github.squdan.swing.components.panel.table.common.action.TableActions;
import io.github.squdan.swing.components.panel.table.common.model.ColumnInfo;
import io.github.squdan.swing.components.panel.table.common.model.GenericTableModel;
import io.github.squdan.swing.components.panel.table.normal.TablePanel;
import io.github.squdan.swing.components.panel.table.paginated.model.FilterPaginatedTextField;
import io.github.squdan.swing.components.util.ViewUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Table representation using {@link JPanel}.
 * <p>
 * This implementation will show a table using received source information from {@link GenericTableModel} implementation,
 * which contains elements to show in the table and column information. This component will offer some on-click action
 * if they are implemented at received {@link TableActions} implementation.
 * <p>
 * T: view type.
 * K: table type.
 */
@Slf4j
public class TablePaginatedPanel<T, K> extends TablePanel<T, K> {

    @Serial
    private static final long serialVersionUID = 8438204457727336847L;

    /**
     * Constructor to configure table requirements.
     *
     * @param configuration: configuration container with all required and optional features.
     */
    public TablePaginatedPanel(final TablePaginatedConfiguration<T, K> configuration) {
        super(configuration, false);

    }

    private void configureTablePaginatedRepresentation(final TablePaginatedConfiguration<T, K> configuration) {
        final JComponent tableContainer = new JScrollPane(this.table);

        // Adds table actions
        if (Objects.nonNull(this.tableActions)) {
            tableContainer.setComponentPopupMenu(this.tableActions.getAvailableTableActions());
        }

        // Generate filters
        if (BooleanUtils.isTrue(configuration.getEnableFilteringAndSorting())) {
            final List<FilterPaginatedTextField<GenericTableModel<K>>> filters = configureTablePaginatedFilters(configuration);

            // Panel configuration
            final JPanel tablePanel = ViewUtils.generateVerticalBigPanelMultipleHeaders(tableContainer,
                    filters.toArray(new FilterPaginatedTextField[0]));
            this.add(ViewUtils.generateVerticalBigPanelMultipleHeaders(tablePanel, getTableTitle(configuration.getTitle())));
        } else {
            // Configure default sorting
            this.table.setAutoCreateRowSorter(true);

            // Panel configuration
            this.add(ViewUtils.generateVerticalBigPanelMultipleHeaders(tableContainer, getTableTitle(configuration.getTitle())));
        }
    }

    private List<FilterPaginatedTextField<GenericTableModel<K>>> configureTablePaginatedFilters(final TablePaginatedConfiguration<T, K> configuration) {
        final List<FilterPaginatedTextField<GenericTableModel<K>>> filters = new ArrayList<>();

        // Generate table sorter from table model
        final TableRowSorter<GenericTableModel<K>> sorter = new TableRowSorter<>(this.tableModel);

        // Generate filters for each configured column
        for (ColumnInfo column : this.tableModel.getColumns()) {
            filters.add(new FilterPaginatedTextField<>(column.getName(), sorter, column.getNumber()));
        }

        // Configure filters to clean each other
        IntStream.range(0, filters.size()).forEach(i -> {
            // Adds each other filter different to the current one
            IntStream.range(0, filters.size()).forEach(j -> {
                if (i != j) {
                    filters.get(i).addFilterToClean(filters.get(j));
                }
            });
        });

        // Add sorter with filters to the table
        this.table.setRowSorter(sorter);

        return filters;
    }
}
