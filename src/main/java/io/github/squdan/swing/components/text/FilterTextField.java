package io.github.squdan.swing.components.text;

import io.github.squdan.swing.components.table.action.TableFilterListener;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.io.Serial;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class FilterTextField<T extends AbstractTableModel> extends PlaceholderTextField {

    /**
     * Generated Serial Version UID
     */
    @Serial
    private static final long serialVersionUID = -4142544664689561171L;

    // Data
    private TableFilterListener<T> filterListener;

    public FilterTextField(final String placeholder, final TableRowSorter<T> tableSorter, final int column) {
        super(placeholder, null);

        this.filterListener = new TableFilterListener<T>(tableSorter, column);
        this.getDocument().addDocumentListener(this.filterListener);
    }

    public void addFilterToClean(final FilterTextField<T> filter) {
        this.filterListener.addFilterToClean(filter);
    }

    public void addFiltersToClean(final List<FilterTextField<T>> filter) {
        this.filterListener.addFiltersToClean(filter);
    }

}