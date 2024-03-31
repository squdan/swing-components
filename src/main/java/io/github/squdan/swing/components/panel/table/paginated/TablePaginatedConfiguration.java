package io.github.squdan.swing.components.panel.table.paginated;

import io.github.squdan.querydsl.filters.QueryDslFilter;
import io.github.squdan.swing.components.panel.table.normal.TableConfiguration;
import io.github.squdan.swing.components.panel.table.paginated.provider.TablePaginatedDataProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TablePaginatedConfiguration<T, K> extends TableConfiguration<T, K> {

    private final TablePaginatedDataProvider<K> provider;

    private List<QueryDslFilter> baseFilters;

    private Integer pageElements;
}
