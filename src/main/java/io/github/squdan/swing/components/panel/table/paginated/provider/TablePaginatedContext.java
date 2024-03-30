package io.github.squdan.swing.components.panel.table.paginated.provider;

import io.github.squdan.querydsl.filters.QueryDslFilter;
import io.github.squdan.swing.components.panel.table.common.model.GenericTableModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class TablePaginatedContext<T> {

    // Configuration
    public static Integer DEFAULT_PAGE_SIZE = 50;

    // Data
    private final GenericTableModel<T> tableModel;

    private final TablePaginatedDataProvider<T> provider;

    private final List<QueryDslFilter> baseFilters;

    private List<QueryDslFilter> customFilters;

    private Pageable pageConfiguration;

    private Page<T> currentPage;

    public void find(final List<QueryDslFilter> filters) {
        find(filters, null);
    }

    public void find(final List<QueryDslFilter> filters, final Sort sort) {
        // Requirements
        Assert.notNull(this.provider, "Provider not configured.");

        // Execution
        this.customFilters = filters;

        if (Objects.nonNull(this.pageConfiguration)) {
            this.pageConfiguration = this.pageConfiguration.first();
        } else {
            setDefaultPageable();
        }

        updateSorting(sort);
        final List<QueryDslFilter> joinFilters = new ArrayList<>(filters);
        joinFilters.addAll(baseFilters);
        this.currentPage = this.provider.find(joinFilters, this.pageConfiguration);
        this.tableModel.setData(this.currentPage.getContent());
    }

    public void updatePage(final int pageNumber, final int pageSize) {
        if (Objects.nonNull(this.pageConfiguration)) {
            this.pageConfiguration = PageRequest.of(pageNumber, pageSize, this.pageConfiguration.getSort());
        } else {
            this.pageConfiguration = PageRequest.of(pageNumber, pageSize);
        }
    }

    public void updateSorting(final Sort sort) {
        if (Objects.nonNull(sort)) {
            if (Objects.nonNull(this.pageConfiguration)) {
                this.pageConfiguration = PageRequest.of(this.pageConfiguration.getPageNumber(), this.pageConfiguration.getPageSize(), sort);
            } else {
                setDefaultPageable();
            }
        }
    }

    public void updatePageAndSorting(final int pageNumber, final int pageSize, final Sort sort) {
        this.pageConfiguration = PageRequest.of(pageNumber, pageSize, sort);
    }

    public void nextPage() {
        // Requirements
        Assert.notNull(this.provider, "Provider not configured.");
        Assert.notNull(this.pageConfiguration, "Pageable not configured.");
        Assert.notNull(this.currentPage, "Execute find method first.");

        // Execution
        this.pageConfiguration = this.pageConfiguration.next();
        find(this.customFilters);
    }

    public void previousPage() {
        // Requirements
        Assert.notNull(this.provider, "Provider not configured.");
        Assert.notNull(this.pageConfiguration, "Pageable not configured.");
        Assert.notNull(this.currentPage, "Execute find method first.");

        // Execution
        this.pageConfiguration = this.pageConfiguration.previousOrFirst();
        find(this.customFilters);
    }

    private void setDefaultPageable() {
        updatePage(0, DEFAULT_PAGE_SIZE);
    }
}
