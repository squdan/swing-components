package io.github.squdan.swing.components.panel.table.paginated;

import io.github.squdan.swing.components.configuration.SwingComponents;
import io.github.squdan.swing.components.panel.table.common.action.TableActions;
import io.github.squdan.swing.components.panel.table.common.model.ColumnInfo;
import io.github.squdan.swing.components.panel.table.common.model.GenericTableModel;
import io.github.squdan.swing.components.panel.table.normal.TablePanel;
import io.github.squdan.swing.components.panel.table.paginated.model.FilterPaginatedTextField;
import io.github.squdan.swing.components.panel.table.paginated.provider.TablePaginatedContext;
import io.github.squdan.swing.components.text.PlaceholderValidatedTextField;
import io.github.squdan.swing.components.text.ReadOnlyTextField;
import io.github.squdan.swing.components.util.ViewUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
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

    // Configuration
    public static Integer DEFAULT_PAGE_SIZE = 2;

    // Data
    private final TablePaginatedContext<K> paginationContext;

    // Components
    private final JTextField page = new PlaceholderValidatedTextField(PlaceholderValidatedTextField.TextFieldRestrictions.CommonRestrictions.INTEGER.getRestrictions());
    private final JTextField pages = new ReadOnlyTextField("Páginas");
    private final JButton previous = ViewUtils.getDefaultColorButton("Previa");
    private final JButton next = ViewUtils.getDefaultColorButton("Siguiente");

    /**
     * Constructor to configure table requirements.
     *
     * @param configuration: configuration container with all required and optional features.
     */
    public TablePaginatedPanel(final TablePaginatedConfiguration<T, K> configuration) {
        super(configuration, false);
        this.paginationContext = new TablePaginatedContext<>(
                this,
                configuration.getTableModel(),
                configuration.getProvider(),
                configuration.getBaseFilters(),
                Objects.nonNull(configuration.getPageElements()) ? configuration.getPageElements() : DEFAULT_PAGE_SIZE);

        // Update pagination components
        this.page.setText(String.valueOf(0));
        this.pages.setText(String.valueOf(0));

        // Configure action listeners
        /*page.getDocument().addDocumentListener(e -> {

        });*/
        next.addActionListener(e -> {
            final String currentPage = this.page.getText();

            if (NumberUtils.isDigits(currentPage) && NumberUtils.toInt(currentPage) < NumberUtils.toInt(this.pages.getText())) {
                this.paginationContext.nextPage();
                this.page.setText(String.valueOf(this.paginationContext.getCurrentPage() + 1));
            }
        });

        previous.addActionListener(e -> {
            final String currentPage = this.page.getText();

            if (NumberUtils.isDigits(currentPage) && NumberUtils.toInt(currentPage) > 1) {
                this.paginationContext.previousPage();
                this.page.setText(String.valueOf(this.paginationContext.getCurrentPage() + 1));
            }
        });

        // Configure paginated representation
        configureTablePaginatedRepresentation(configuration);
    }

    private void configureTablePaginatedRepresentation(final TablePaginatedConfiguration<T, K> configuration) {
        final JComponent tableContainer = new JScrollPane(this.table);

        // Adds table actions
        if (Objects.nonNull(this.tableActions)) {
            tableContainer.setComponentPopupMenu(this.tableActions.getAvailableTableActions());
        }

        // Generate filters
        if (BooleanUtils.isTrue(configuration.getEnableFilteringAndSorting())) {
            final List<FilterPaginatedTextField<K>> filters = configureTablePaginatedFilters(configuration);

            // Panel configuration
            final JPanel tablePanel = ViewUtils.generateVerticalBigPanelMultipleHeaders(tableContainer,
                    filters.toArray(new FilterPaginatedTextField[0]));

            final JPanel paginationConfigurationPanel = new JPanel(new GridLayout(1, 0));
            paginationConfigurationPanel.setBackground(Color.DARK_GRAY);
            paginationConfigurationPanel.add(getTableHeader("Página"));
            paginationConfigurationPanel.add(this.page);
            paginationConfigurationPanel.add(getTableHeader("de"));
            paginationConfigurationPanel.add(this.pages);

            final JPanel tablePaginatedPanel = ViewUtils.generateVerticalBigPanelMultipleHeaders(
                    tablePanel,
                    previous,
                    paginationConfigurationPanel,
                    next);
            this.add(ViewUtils.generateVerticalBigPanelMultipleHeaders(tablePaginatedPanel, getTableTitle(configuration.getTitle())));
        } else {
            // Configure default sorting
            this.table.setAutoCreateRowSorter(true);

            // Panel configuration
            this.add(ViewUtils.generateVerticalBigPanelMultipleHeaders(tableContainer, getTableTitle(configuration.getTitle())));
        }
    }

    private List<FilterPaginatedTextField<K>> configureTablePaginatedFilters(final TablePaginatedConfiguration<T, K> configuration) {
        final List<FilterPaginatedTextField<K>> filters = new ArrayList<>();

        // Generate table sorter from table model
        final TableRowSorter<GenericTableModel<K>> sorter = new TableRowSorter<>(this.tableModel);

        // Generate filters for each configured column
        for (ColumnInfo column : this.tableModel.getColumns()) {
            filters.add(new FilterPaginatedTextField<>(column.getName(), paginationContext, column.getNumber()));
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

    public void reload() {
        log.info("[TABLE-PAGINATED] Recargando tabla.");

        // Reload table information
        this.paginationContext.reload();

        // Update pagination components
        this.page.setText(String.valueOf(this.paginationContext.getCurrentPage() + 1));
        this.pages.setText(String.valueOf(this.paginationContext.getTotalPages()));
    }

    private JComponent getTableHeader(final String text) {
        final JLabel dashboardTitle = new JLabel(text, SwingConstants.CENTER);
        dashboardTitle.setForeground(SwingComponents.getConfiguration().getColorConfiguration().getPrimaryText());
        dashboardTitle.setFont(SwingComponents.getConfiguration().getTextConfiguration().getTitleSecondaryFont());

        // Panel to set background
        final JPanel result = new JPanel(new GridLayout(1, 1));
        result.setBackground(Color.DARK_GRAY);
        result.add(dashboardTitle);
        return result;
    }
}
