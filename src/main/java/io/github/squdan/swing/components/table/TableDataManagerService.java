package io.github.squdan.swing.components.table;

public interface TableDataManagerService<T> {

	public void see(final String command, final T rowData, final Object columnData);

	public void create(final String command, final T rowData, final Object columnData);

	public void update(final String command, final T rowData, final Object columnData);

	public void delete(final String command, final T rowData, final Object columnData);

}
