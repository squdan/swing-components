package io.github.squdan.swing.components.table;

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

@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
public class GenericTableModel<T> extends AbstractTableModel {

	/** Generated Serial Version UID */
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
		this.columns = new ArrayList<ColumnInfo>(columns);

		if (Objects.isNull(source)) {
			this.values = new ArrayList<>();
		} else {
			this.values = new ArrayList<>(source);
		}
	}

	public void setData(final List<T> source) {
		this.values = new ArrayList<>(source);
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

		try {
			// Recover column value from generic model
			if (Objects.nonNull(rowData) && StringUtils.isNotBlank(columnModelName)) {
				final Field columnField = rowData.getClass().getDeclaredField(columnModelName);
				columnField.setAccessible(true);
				result = columnField.get(rowData);
			}
		} catch (final NoSuchFieldException | SecurityException e) {
			log.error("Error al recuperar la información de la columna '{} - {}'. Error: ", columnIndex,
					columnModelName, e);
		} catch (final IllegalArgumentException | IllegalAccessException e) {
			log.error("Error al recuperar el valor de la columna '{} - {}'. Error: ", columnIndex, columnModelName, e);
		}

		return result;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int mColIndex) {
		return false;
	}
}
