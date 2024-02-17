package io.github.squdan.swing.components.panel.table.model;

import io.github.squdan.swing.components.panel.table.TablePanel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Defines how {@link TablePanel} should store and recover information from the table.
 *
 * @param <T> Data type.
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class GenericTableModel<T> extends AbstractTableModel {

    @Serial
    private static final long serialVersionUID = 1661124786644146937L;

    // Configuration
    public static final String UNKNOWN = "??";

    // Data
    private final List<ColumnInfo> columns;
    private List<T> values;

    public GenericTableModel(final List<ColumnInfo> columns) {
        this(columns, null);
    }

    public GenericTableModel(final List<ColumnInfo> columns, final List<T> source) {
        this.columns = new ArrayList<>(columns);

        if (Objects.isNull(source)) {
            this.values = new ArrayList<>();
        } else {
            this.values = new ArrayList<>(source);
        }
    }

    public void setData(final List<T> source) {
        if (Objects.isNull(source)) {
            this.values = new ArrayList<>();
        } else {
            this.values = new ArrayList<>(source);
        }
    }

    public void addData(final T source) {
        this.values.add(source);
    }

    public void addData(final List<T> source) {
        this.values.addAll(source);
    }

    @Override
    public int getRowCount() {
        return values.size();
    }

    @Override
    public int getColumnCount() {
        return this.columns.size();
    }

    @Override
    public String getColumnName(final int columnIndex) {
        return this.columns.get(columnIndex).getName();
    }

    public Object getValueAt(int rowIndex) {
        Object result = null;

        if (CollectionUtils.isNotEmpty(values)) {
            result = values.get(rowIndex);
        }

        return result;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        Object result = UNKNOWN;

        // Select value field
        final Object rowData = getValueAt(rowIndex);
        final String columnModelName = this.columns.get(columnIndex).getModelName();
        final Object mayObjectValue = getFieldValueRecursive(rowData, columnModelName);

        if (Objects.nonNull(mayObjectValue)) {
            result = mayObjectValue;
        } else {
            log.error("Error al recuperar el valor de la columna '{} - {}'.", columnIndex, columnModelName);
        }

        return result;
    }

    private Object getFieldValueRecursive(final Object source, final String paramName) {
        Object result = null;

        if (Objects.nonNull(source) && StringUtils.isNotBlank(paramName)) {
            final String[] paramNameComposed = paramName.split("\\.", 2);

            if (StringUtils.isNotBlank(paramNameComposed[0])) {
                result = getFieldValueWithReflection(source, paramNameComposed[0]);

                if (paramNameComposed.length == 2) {
                    final Object mayFieldSubobject = getFieldValueRecursive(result, paramNameComposed[1]);

                    if (Objects.nonNull(mayFieldSubobject)) {
                        result = mayFieldSubobject;
                    }
                }
            }
        }

        return result;
    }

    private Object getFieldValueWithReflection(final Object source, final String paramName) {
        Object result = null;

        try {
            // Recover column value from generic model
            if (Objects.nonNull(source) && StringUtils.isNotBlank(paramName)) {
                final Field columnField = source.getClass().getDeclaredField(paramName);
                columnField.setAccessible(true);
                result = columnField.get(source);
            }
        } catch (final NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            log.error("Error al recuperar la información del parámetro '{}'. Error: ", paramName, e);
        }

        return result;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int mColIndex) {
        return false;
    }
}
