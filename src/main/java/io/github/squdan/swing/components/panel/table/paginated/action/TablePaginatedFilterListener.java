package io.github.squdan.swing.components.panel.table.paginated.action;

import io.github.squdan.querydsl.filters.QueryDslFilter;
import io.github.squdan.querydsl.filters.QueryDslOperators;
import io.github.squdan.swing.components.panel.table.normal.TablePanel;
import io.github.squdan.swing.components.panel.table.paginated.model.FilterPaginatedTextField;
import io.github.squdan.swing.components.panel.table.paginated.provider.TablePaginatedContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.util.ArrayList;
import java.util.List;

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
            //final Class<?> columnType = this.paginationContext.getTableModel().getColumnType(column);
            // TODO: Comprobar si es necesario filtrar la operación por tipo
            final List<QueryDslFilter> filters = List.of(
                    QueryDslFilter.builder()
                            .key(this.paginationContext.getTableModel().getColumnName(column))
                            .operator(QueryDslOperators.CONTAIN_FUNCTION)
                            .value(filter)
                            .build());
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
}
