package io.github.squdan.swing.components.panel.table.paginated.provider;

import io.github.squdan.querydsl.filters.QueryDslFilter;
import io.github.squdan.swing.components.panel.table.common.model.GenericTableModel;
import io.github.squdan.swing.components.panel.table.paginated.TablePaginatedPanel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class TablePaginatedContext<T> {

    // Data
    private final TablePaginatedPanel<?, T> table;

    private final GenericTableModel<T> tableModel;

    private final TablePaginatedDataProvider<T> provider;

    private final List<QueryDslFilter> baseFilters;

    private final Integer pageElements;

    private List<QueryDslFilter> customFilters;

    private Pageable pageConfiguration;

    private Page<T> currentPage;

    public int getCurrentPage() {
        int result = 0;

        if (Objects.nonNull(this.currentPage)) {
            result = this.currentPage.getNumber();
        }

        return result;
    }

    public int getTotalPages() {
        int result = 0;

        if (Objects.nonNull(this.currentPage)) {
            result = this.currentPage.getTotalPages();
        }

        return result;
    }

    public long getTotalElements() {
        long result = 0;

        if (Objects.nonNull(this.currentPage)) {
            result = this.currentPage.getTotalElements();
        }

        return result;
    }

    public int getPageElements() {
        int result = 0;

        if (Objects.nonNull(this.currentPage)) {
            result = this.currentPage.getPageable().getPageSize();
        }

        return result;
    }

    public void reload() {
        if (Objects.isNull(this.pageConfiguration)) {
            setDefaultPageable();
        }

        find(this.customFilters, this.pageConfiguration.getSort());
    }

    public void find() {
        find(null, null);
    }

    public void find(final List<QueryDslFilter> filters) {
        find(filters, null);
    }

    public void find(final List<QueryDslFilter> filters, final Sort sort) {
        // Requirements
        Assert.notNull(this.provider, "Provider not configured.");

        // Configuring pagination for first execution
        if (Objects.isNull(this.pageConfiguration)) {
            setDefaultPageable();
        }

        // Update filters and pagination configuration
        if (areFiltersChanging(filters)) {
            this.customFilters = filters;

            // If filters has changed, searching starts at first page
            this.pageConfiguration = this.pageConfiguration.first();
        }

        updateSorting(sort);

        // Prepare filters to search
        final List<QueryDslFilter> joinFilters = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(this.customFilters)) {
            joinFilters.addAll(this.customFilters);
        }

        if (CollectionUtils.isNotEmpty(this.baseFilters)) {
            joinFilters.addAll(this.baseFilters);
        }

        // Execute search and save results
        try {
            this.currentPage = this.provider.find(joinFilters, this.pageConfiguration);
            this.tableModel.setData(this.currentPage.getContent());

            // Elements may be deleted, so we navigate to last page
            if ((getCurrentPage() >= getTotalPages()) && this.currentPage.isEmpty()) {
                toPage(getTotalPages() - 1);
            }

            this.table.refresh();
        } catch (final Exception e) {
            final String errorMsg = "Error interno durante la búsqueda.";
            JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void toPage(final int page) {
        // Requirements
        Assert.notNull(this.provider, "Provider not configured.");
        Assert.notNull(this.pageConfiguration, "Pageable not configured.");
        Assert.notNull(this.currentPage, "Execute find method first.");

        // Execution
        updatePage(page, this.pageElements);
        find(this.customFilters);
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
        updatePage(0, this.pageElements);
    }

    private boolean areFiltersChanging(final List<QueryDslFilter> filters) {
        boolean result = true;

        if (Objects.isNull(this.customFilters) && Objects.isNull(filters)) {
            result = false;
        } else if (Objects.nonNull(this.customFilters) && Objects.nonNull(filters)) {
            if (CollectionUtils.isEqualCollection(this.customFilters, filters)) {
                result = false;
            }
        }

        return result;
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
}
