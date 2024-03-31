package io.github.squdan.swing.components.panel.table.common.model;

import io.github.squdan.swing.components.panel.table.normal.TablePanel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Model that contains column information to use at {@link TablePanel}.
 */
@Getter
@Builder
@AllArgsConstructor
public class ColumnInfo {

    // Column name to show
    private String name;

    // Column number to show
    private int number;

    // Column field name from model
    private String modelName;

    // Required configuration to filter with TablePaginatedPanel
    private TablePaginatedConfiguration paginatedConfiguration;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TablePaginatedConfiguration {
        // Column field value type
        // If you want to filter by this column in a TablePaginatedPanel it's necessary to specify field type to filter
        private Class<?> type;

        // If current column is a SwingComponentsItem<?> and you want to filter by this column in a TablePaginatedPanel
        // it's necessary to specify internal fields to filter
        private List<SwingComponentsItemField> searchBy;

        @Getter
        @Builder
        @AllArgsConstructor
        public static class SwingComponentsItemField {
            private String modelName;
            private Class<?> type;
        }
    }
}