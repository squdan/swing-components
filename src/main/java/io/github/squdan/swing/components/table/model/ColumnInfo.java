package io.github.squdan.swing.components.table.model;

import io.github.squdan.swing.components.table.TablePanel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Model that contains column information to use at {@link TablePanel}.
 */
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