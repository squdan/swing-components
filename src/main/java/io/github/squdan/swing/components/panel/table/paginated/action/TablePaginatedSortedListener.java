package io.github.squdan.swing.components.panel.table.paginated.action;

import io.github.squdan.swing.components.panel.table.paginated.provider.TablePaginatedContext;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Sort;

import javax.swing.*;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import java.util.List;

public class TablePaginatedSortedListener<T> implements RowSorterListener {

    // Configuration
    private static final RowSorterEvent.Type SORTING_EVENT_TO_CAPTURE = RowSorterEvent.Type.SORT_ORDER_CHANGED;

    // Data
    private final TablePaginatedContext<T> paginationContext;

    public TablePaginatedSortedListener(final TablePaginatedContext<T> paginationContext) {
        this.paginationContext = paginationContext;
    }


    @Override
    public void sorterChanged(final RowSorterEvent e) {
        if (SORTING_EVENT_TO_CAPTURE.equals(e.getType())) {
            final List<? extends RowSorter.SortKey> sortings = e.getSource().getSortKeys();

            if (CollectionUtils.isNotEmpty(sortings)) {
                paginationContext.find(null, Sort.by(getSort(sortings.get(0))));
            }
        }
    }

    private Sort.Order getSort(final RowSorter.SortKey sortingKey) {
        return new Sort.Order(getDirection(sortingKey), getColumnModelName(sortingKey));
    }

    private String getColumnModelName(final RowSorter.SortKey sortingKey) {
        return this.paginationContext.getTableModel().getColumnModelName(sortingKey.getColumn());
    }

    private Sort.Direction getDirection(final RowSorter.SortKey sortingKey) {
        Sort.Direction result = Sort.Direction.ASC;

        if (!"ASCENDING".equals(sortingKey.getSortOrder().name())) {
            result = Sort.Direction.DESC;
        }

        return result;
    }
}
