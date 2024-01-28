package io.github.squdan.swing.components.panel.table;

import io.github.squdan.swing.components.SwingComponentsView;
import io.github.squdan.swing.components.configuration.SwingComponents;
import io.github.squdan.swing.components.panel.table.action.TableActions;
import io.github.squdan.swing.components.panel.table.cell.SwingComponentsTableCellRenderer;
import io.github.squdan.swing.components.panel.table.model.ColumnInfo;
import io.github.squdan.swing.components.panel.table.model.FilterTextField;
import io.github.squdan.swing.components.panel.table.model.GenericTableModel;
import io.github.squdan.swing.components.util.ViewUtils;
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
 */
public class TablePanel<T, K extends GenericTableModel<T>> extends JPanel {

    @Serial
    private static final long serialVersionUID = 8438204457927336847L;

    // Table state
    private final SwingComponentsView origin;
    private final JTable table;
    private final K tableModel;
    private final TableActions<T> tableActions;
    private int selectedRow;
    private int selectedColumn;

    /**
     * Constructor to configure table requirements.
     *
     * @param origin:        current view to refresh when some action is executed.
     * @param title:         title to show in the table.
     * @param tableModel:    source elements information.
     * @param tableActions:  available user actions over table and cells.
     * @param enableFilters: if true, filter and sorting will be enabled.
     */
    public TablePanel(final SwingComponentsView origin, final String title, final K tableModel, final TableActions<T> tableActions,
                      final Boolean enableFilters) {
        super(new GridLayout(1, 1));
        this.origin = origin;
        this.tableModel = tableModel;
        this.tableActions = tableActions;

        // Generate and configure table
        this.table = getConfiguredTable(tableModel);
        final JComponent tableContainer = new JScrollPane(table);

        // Adds table actions
        if (Objects.nonNull(tableActions)) {
            table.setComponentPopupMenu(tableActions.getAvailableCellActions());
            tableContainer.setComponentPopupMenu(tableActions.getAvailableTableActions());

            // Register action listeners
            table.addMouseListener(new SelectCellMouseListener());
            Stream.of(tableActions.getAvailableCellActions().getComponents()).map(c -> (JMenuItem) c)
                    .forEach(c -> c.addActionListener(new OpenPopupDayActionListener()));
            Stream.of(tableActions.getAvailableTableActions().getComponents()).map(c -> (JMenuItem) c)
                    .forEach(c -> c.addActionListener(new OpenPopupDayActionListener()));
        }

        // Generate filters
        if (BooleanUtils.isTrue(enableFilters)) {
            final List<FilterTextField<K>> filters = configureTableFilters(tableModel, table);

            // Panel configuration
            final JPanel tablePanel = ViewUtils.generateVerticalBigPanelMultipleHeaders(tableContainer,
                    filters.toArray(new FilterTextField[0]));
            this.add(ViewUtils.generateVerticalBigPanelMultipleHeaders(tablePanel, getTableTitle(title)));
        } else {
            // Configure default sorting
            table.setAutoCreateRowSorter(true);

            // Panel configuration
            this.add(ViewUtils.generateVerticalBigPanelMultipleHeaders(tableContainer, getTableTitle(title)));
        }
    }

    private JTable getConfiguredTable(final K tableModel) {
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

    private List<FilterTextField<K>> configureTableFilters(final K tableModel, final JTable table) {
        final List<FilterTextField<K>> filters = new ArrayList<>();

        // Generate table sorter from table model
        final TableRowSorter<K> sorter = new TableRowSorter<K>(tableModel);

        // Generate filters for each configured column
        for (ColumnInfo column : tableModel.getColumns()) {
            filters.add(new FilterTextField<K>(column.getName(), sorter, column.getNumber()));
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

    private class OpenPopupDayActionListener implements ActionListener {
        @SuppressWarnings("unchecked")
        public void actionPerformed(ActionEvent e) {
            try {
                final Object cellValue = tableModel.getValueAt(selectedRow, selectedColumn);
                final Object rowValue = tableModel.getValueAt(selectedRow);
                tableActions.manageActionEvents(e.getSource(), e.getActionCommand(), (T) rowValue, cellValue,
                        selectedRow, selectedColumn);
                origin.refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
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
