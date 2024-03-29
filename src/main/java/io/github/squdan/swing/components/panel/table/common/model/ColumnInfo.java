package io.github.squdan.swing.components.panel.table.common.model;

import io.github.squdan.swing.components.panel.table.normal.TablePanel;
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