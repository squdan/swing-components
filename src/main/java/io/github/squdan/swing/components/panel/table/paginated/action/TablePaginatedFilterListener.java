package io.github.squdan.swing.components.panel.table.paginated.action;

import io.github.squdan.querydsl.filters.QueryDslFilter;
import io.github.squdan.querydsl.filters.QueryDslOperators;
import io.github.squdan.querydsl.filters.util.DateTimeUtils;
import io.github.squdan.swing.components.SwingComponentsItem;
import io.github.squdan.swing.components.panel.table.common.model.ColumnInfo;
import io.github.squdan.swing.components.panel.table.normal.TablePanel;
import io.github.squdan.swing.components.panel.table.paginated.model.FilterPaginatedTextField;
import io.github.squdan.swing.components.panel.table.paginated.provider.TablePaginatedContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.time.temporal.Temporal;
import java.util.*;

/**
 * Defines how filters should work at {@link TablePanel}.
 *
 * @param <T> AbstractTableModel implementation.
 */
@Slf4j
public class TablePaginatedFilterListener<T> implements DocumentListener {

    // Data
    private final List<FilterPaginatedTextField<T>> filtersToClean = new ArrayList<>();
    private final TablePaginatedContext<T> paginationContext;
    private final int column;

    public TablePaginatedFilterListener(final TablePaginatedContext<T> paginationContext, final int column) {
        this.paginationContext = paginationContext;
        this.column = column;
    }

    public void addFilterToClean(final FilterPaginatedTextField<T> filter) {
        this.filtersToClean.add(filter);
    }

    public void addFiltersToClean(final List<FilterPaginatedTextField<T>> filter) {
        this.filtersToClean.addAll(filter);
    }

    @Override
    public void changedUpdate(final DocumentEvent e) {
        final String filter = getDocumentEventValue(e);
        applyFilter(filter);
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
        final String filter = getDocumentEventValue(e);
        applyFilter(filter);
    }

    @Override
    public void insertUpdate(final DocumentEvent e) {
        final String filter = getDocumentEventValue(e);
        applyFilter(filter);
    }

    private String getDocumentEventValue(final DocumentEvent event) {
        String result = null;

        try {
            result = event.getDocument().getText(0, event.getDocument().getLength());
        } catch (final BadLocationException e) {
            log.error("Error al recuperar la información de un filtro. Error: ", e);
        }

        return result;
    }

    private void applyFilter(final String filter) {

        // This will reset filters to show all results
        if (StringUtils.isBlank(filter)) {
            paginationContext.find(null);
        }

        // Apply current filter
        else {
            final List<QueryDslFilter> filters = generateFilters(filter);
            paginationContext.find(filters);

            // Clean other filters
            if (CollectionUtils.isNotEmpty(filtersToClean)) {
                filtersToClean.forEach(this::clearOrderToFilter);
            }
        }
    }

    private void clearOrderToFilter(final FilterPaginatedTextField<T> filter) {
        final Runnable clearFilter = () -> {
            filter.getDocument().removeDocumentListener(filter.getFilterListener());
            filter.setText(StringUtils.EMPTY);
            filter.getDocument().addDocumentListener(filter.getFilterListener());
        };

        SwingUtilities.invokeLater(clearFilter);
    }

    private List<QueryDslFilter> generateFilters(final String filter) {
        final List<QueryDslFilter> result = new ArrayList<>();

        // Check column type
        final ColumnInfo.TablePaginatedConfiguration paginatedConfiguration = this.paginationContext.getTableModel().getPaginatedConfiguration(this.column);

        if (Objects.nonNull(paginatedConfiguration)) {
            if (SwingComponentsItem.class.isAssignableFrom(paginatedConfiguration.getType())) {
                final List<ColumnInfo.TablePaginatedConfiguration.SwingComponentsItemField> searchBy = paginatedConfiguration.getSearchBy();

                if (CollectionUtils.isNotEmpty(searchBy)) {
                    searchBy.forEach(sb -> {
                        final String columnModelName = String.join(".", this.paginationContext.getTableModel().getColumnModelName(this.column), sb.getModelName());
                        final QueryDslFilter queryDslFilter = generateFilter(sb.getType(), columnModelName, filter);

                        if (Objects.nonNull(queryDslFilter)) {
                            result.add(queryDslFilter);
                        }
                    });
                }
            } else {
                final QueryDslFilter queryDslFilter = generateFilter(paginatedConfiguration.getType(), this.paginationContext.getTableModel().getColumnModelName(this.column), filter);

                if (Objects.nonNull(queryDslFilter)) {
                    result.add(queryDslFilter);
                }
            }
        }

        return result;
    }

    private QueryDslFilter generateFilter(final Class<?> columnType, final String columnName, final String filter) {
        QueryDslFilter result = null;

        if (Boolean.class.isAssignableFrom(columnType) || boolean.class.isAssignableFrom(columnType)) {
            // Filter avoid until valid boolean is introduced into the filter
            // - Contains not supported
            final Boolean filterAsBoolean = BooleanUtils.toBooleanObject(filter);
            if (Objects.nonNull(filterAsBoolean)) {
                result = QueryDslFilter.builder()
                        .key(columnName)
                        .operator(QueryDslOperators.EQUALS)
                        .value(BooleanUtils.toBooleanObject(filter))
                        .build();
            }
        } else if (Temporal.class.isAssignableFrom(columnType) || Date.class.isAssignableFrom(columnType)) {
            // Filter avoid until valid date is introduced into the filter
            // - Contains not supported
            if (Objects.nonNull(DateTimeUtils.toInstantUtc(filter))) {
                result = QueryDslFilter.builder()
                        .key(columnName)
                        .operator(QueryDslOperators.EQUALS)
                        .value(filter)
                        .build();
            }
        } else if (Number.class.isAssignableFrom(columnType)) {
            // Filter avoid until valid number is introduced into the filter
            // - Contains not supported
            if (NumberUtils.isDigits(filter)) {
                result = QueryDslFilter.builder()
                        .key(columnName)
                        .operator(QueryDslOperators.EQUALS)
                        .value(filter)
                        .build();
            }
        } else if (UUID.class.isAssignableFrom(columnType)) {
            try {
                // Filter avoid until valid UUID is introduced into the filter (Exception thrown by UUID.fromString to restrict)
                // - Contains not supported
                result = QueryDslFilter.builder()
                        .key(columnName)
                        .operator(QueryDslOperators.EQUALS)
                        .value(UUID.fromString(filter).toString())
                        .build();
            } catch (final IllegalArgumentException e) {
                // Do nothing
            }
        } else if (String.class.isAssignableFrom(columnType)) {
            result = QueryDslFilter.builder()
                    .key(columnName)
                    .operator(QueryDslOperators.CONTAIN_FUNCTION)
                    .value(filter)
                    .build();
        } else {
            final String errorMsg = String.format("Tipo '%s' de la columna desconocido.", columnType);
            JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
        }

        return result;
    }
}
