package io.github.squdan.swing.components.panel.table.normal;

import io.github.squdan.swing.components.SwingComponentsView;
import io.github.squdan.swing.components.configuration.SwingComponents;
import io.github.squdan.swing.components.panel.table.common.action.TableActions;
import io.github.squdan.swing.components.panel.table.common.cell.SwingComponentsTableCellRenderer;
import io.github.squdan.swing.components.panel.table.common.model.ColumnInfo;
import io.github.squdan.swing.components.panel.table.common.model.GenericTableModel;
import io.github.squdan.swing.components.panel.table.normal.model.FilterTextField;
import io.github.squdan.swing.components.util.ViewUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Table representation using {@link JPanel}.
 * <p>
 * This implementation will show a table using received source information from {@link GenericTableModel} implementation,
 * which contains elements to show in the table and column information. This component will offer some on-click action
 * if they are implemented at received {@link TableActions} implementation.
 * <p>
 * @param T: view type.
 * @param K: table type.
 */
@Slf4j
public class TablePanel<T, K> extends JPanel {

    @Serial
    private static final long serialVersionUID = 8438204457927336847L;

    // Table state
    private final JTable table;
    private final GenericTableModel<K> tableModel;
    private final TableActions<K> tableActions;
    private int selectedRow;
    private int selectedColumn;

    /**
     * Constructor to configure table requirements.
     *
     * @param configuration: configuration container with all required and optional features.
     */
    public TablePanel(final TableConfiguration<T, K> configuration) {
        super(new GridLayout(1, 1));
        this.tableModel = configuration.getTableModel();
        this.tableActions = configuration.getTableActions();

        // Generate and configure table
        this.table = getConfiguredTable(tableModel);
        final JComponent tableContainer = new JScrollPane(table);

        // Adds table actions
        if (Objects.nonNull(tableActions)) {
            table.setComponentPopupMenu(tableActions.getAvailableCellActions());
            tableContainer.setComponentPopupMenu(tableActions.getAvailableTableActions());

            // Register action listeners
            table.addMouseListener(new SelectCellMouseListener());
            final AvailableActionsListener availableActionsListener = new AvailableActionsListener(configuration.getView(), configuration.getViewInput());
            Stream.of(tableActions.getAvailableCellActions().getComponents()).map(c -> (JMenuItem) c)
                    .forEach(c -> c.addActionListener(availableActionsListener));
            Stream.of(tableActions.getAvailableTableActions().getComponents()).map(c -> (JMenuItem) c)
                    .forEach(c -> c.addActionListener(availableActionsListener));
        }

        // Generate filters
        if (BooleanUtils.isTrue(configuration.getEnableFilteringAndSorting())) {
            final List<FilterTextField<GenericTableModel<K>>> filters = configureTableFilters(tableModel, table);

            // Panel configuration
            final JPanel tablePanel = ViewUtils.generateVerticalBigPanelMultipleHeaders(tableContainer,
                    filters.toArray(new FilterTextField[0]));
            this.add(ViewUtils.generateVerticalBigPanelMultipleHeaders(tablePanel, getTableTitle(configuration.getTitle())));
        } else {
            // Configure default sorting
            table.setAutoCreateRowSorter(true);

            // Panel configuration
            this.add(ViewUtils.generateVerticalBigPanelMultipleHeaders(tableContainer, getTableTitle(configuration.getTitle())));
        }
    }

    private JTable getConfiguredTable(final GenericTableModel<K> tableModel) {
        final JTable result = new JTable(tableModel);

        // Table configuration
        result.getTableHeader().setResizingAllowed(false);
        result.getTableHeader().setReorderingAllowed(false);

        // Apply center align to each column
        final SwingComponentsTableCellRenderer centerRenderer = new SwingComponentsTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        IntStream.range(0, result.getColumnCount())
                .forEach(i -> result.getColumnModel().getColumn(i).setCellRenderer(centerRenderer));

        return result;
    }

    private List<FilterTextField<GenericTableModel<K>>> configureTableFilters(final GenericTableModel<K> tableModel, final JTable table) {
        final List<FilterTextField<GenericTableModel<K>>> filters = new ArrayList<>();

        // Generate table sorter from table model
        final TableRowSorter<GenericTableModel<K>> sorter = new TableRowSorter<>(tableModel);

        // Generate filters for each configured column
        for (ColumnInfo column : tableModel.getColumns()) {
            filters.add(new FilterTextField<>(column.getName(), sorter, column.getNumber()));
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
        table.setRowSorter(sorter);

        return filters;
    }

    @AllArgsConstructor
    private class AvailableActionsListener implements ActionListener {
        private final SwingComponentsView<T> view;
        private final T viewInput;

        @SuppressWarnings("unchecked")
        public void actionPerformed(ActionEvent e) {
            try {
                final Object cellValue = tableModel.getValueAt(selectedRow, selectedColumn);
                final Object rowValue = tableModel.getValueAt(selectedRow);
                final boolean refresh = tableActions.manageActionEvents(e.getSource(), e.getActionCommand(), (K) rowValue, cellValue,
                        selectedRow, selectedColumn);

                if (refresh && Objects.nonNull(this.view)) {
                    this.view.refresh(this.viewInput);
                }
            } catch (final Exception ex) {
                log.error("Error gestionando eventos de la tabla. Error: ", ex);
            }
        }
    }

    private class SelectCellMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            selectedRow = table.rowAtPoint(e.getPoint());
            selectedColumn = table.columnAtPoint(e.getPoint());
        }
    }

    private JComponent getTableTitle(final String text) {
        final JLabel dashboardTitle = new JLabel(text, SwingConstants.CENTER);
        dashboardTitle.setForeground(SwingComponents.getConfiguration().getColorConfiguration().getPrimaryText());
        dashboardTitle.setFont(SwingComponents.getConfiguration().getTextConfiguration().getTitleFont());

        // Panel to set background
        final JPanel result = new JPanel(new GridLayout(1, 1));
        result.setBackground(Color.DARK_GRAY);
        result.add(dashboardTitle);
        return result;
    }
}
