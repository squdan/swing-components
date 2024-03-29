package io.github.squdan.swing.components.panel.table.paginated.provider;

import io.github.squdan.querydsl.filters.QueryDslFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TablePaginatedDataProvider<T> {

    Page<T> find(List<QueryDslFilter> filters, Pageable pageable);

}
