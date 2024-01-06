package io.github.squdan.swing.components.table.action;

import io.github.squdan.swing.components.text.FilterTextField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines how filters should work at {@link io.github.squdan.swing.components.table.TablePanel}.
 *
 * @param <T> AbstractTableModel implementation.
 */
@Slf4j
public class TableFilterListener<T extends AbstractTableModel> implements DocumentListener {

    // Data
    private final List<FilterTextField<T>> filtersToClean = new ArrayList<>();
    private final TableRowSorter<T> tableSorter;
    private final int column;

    public TableFilterListener(final TableRowSorter<T> tableSorter, final int column) {
        this.tableSorter = tableSorter;
        this.column = column;
    }

    public void addFilterToClean(final FilterTextField<T> filter) {
        this.filtersToClean.add(filter);
    }

    public void addFiltersToClean(final List<FilterTextField<T>> filter) {
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
            tableSorter.setRowFilter(null);

        }

        // Apply current filter
        else {
            RowFilter<T, Object> rf = RowFilter.regexFilter(filter, column);
            tableSorter.setRowFilter(rf);

            // Clean other filters
            if (CollectionUtils.isNotEmpty(filtersToClean)) {
                filtersToClean.stream().forEach(of -> clearOrderToFilter(of));
            }
        }
    }

    private void clearOrderToFilter(final FilterTextField<T> filter) {
        final Runnable clearFilter = new Runnable() {
            @Override
            public void run() {
                filter.getDocument().removeDocumentListener(filter.getFilterListener());
                filter.setText(StringUtils.EMPTY);
                filter.getDocument().addDocumentListener(filter.getFilterListener());
            }
        };

        SwingUtilities.invokeLater(clearFilter);
    }
}
