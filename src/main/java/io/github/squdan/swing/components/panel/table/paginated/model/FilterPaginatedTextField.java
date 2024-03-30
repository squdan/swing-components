package io.github.squdan.swing.components.panel.table.paginated.model;

import io.github.squdan.swing.components.panel.table.paginated.action.TablePaginatedFilterListener;
import io.github.squdan.swing.components.panel.table.paginated.provider.TablePaginatedContext;
import io.github.squdan.swing.components.text.PlaceholderTextField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.io.Serial;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class FilterPaginatedTextField<T> extends PlaceholderTextField {

    /**
     * Generated Serial Version UID
     */
    @Serial
    private static final long serialVersionUID = -4142544664689561171L;

    // Data
    private TablePaginatedFilterListener<T> filterListener;

    public FilterPaginatedTextField(final String placeholder, final TablePaginatedContext<T> paginationContext, final int column) {
        super(placeholder, null);

        this.filterListener = new TablePaginatedFilterListener<>(paginationContext, column);
        this.getDocument().addDocumentListener(this.filterListener);
    }

    public void addFilterToClean(final FilterPaginatedTextField<T> filter) {
        this.filterListener.addFilterToClean(filter);
    }

    public void addFiltersToClean(final List<FilterPaginatedTextField<T>> filter) {
        this.filterListener.addFiltersToClean(filter);
    }

}