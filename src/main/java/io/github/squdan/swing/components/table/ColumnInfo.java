package io.github.squdan.swing.components.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ColumnInfo {

	// Column name to show
	private String name;
	
	// Column number to show
	private int number;
	
	// Column field name from model
	private String modelName;

}