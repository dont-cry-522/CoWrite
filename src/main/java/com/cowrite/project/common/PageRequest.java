package com.cowrite.project.common;

import java.util.List;
import java.util.Map;

/**
 * 分页参数
 *
 * @author heathcetide
 */
public class PageRequest {
    /**
     * 当前页码（从 1 开始）
     */
    private Integer page = 1;

    /**
     * 每页数量
     */
    private Integer size = 10;

    /**
     * 排序字段，例如：createTime、name
     */
    private String sortBy;

    /**
     * 排序方式：asc / desc
     */
    private String sortOrder = "desc";

    /**
     * 模糊搜索关键字（可选）
     */
    private String keyword;

    /**
     * 精确过滤字段，例如 status=active, role=admin
     */
    private Map<String, Object> filters;

    /**
     * 时间范围筛选（如创建时间）
     * 格式建议：["2024-01-01", "2024-01-31"]
     */
    private List<String> dateRange;

    /**
     * 是否启用分页（有些接口可返回全部）
     */
    private Boolean pagination = true;

    /**
     * 是否统计总数（性能优化时可选择关闭）
     */
    private Boolean countTotal = true;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public List<String> getDateRange() {
        return dateRange;
    }

    public void setDateRange(List<String> dateRange) {
        this.dateRange = dateRange;
    }

    public Boolean getPagination() {
        return pagination;
    }

    public void setPagination(Boolean pagination) {
        this.pagination = pagination;
    }

    public Boolean getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(Boolean countTotal) {
        this.countTotal = countTotal;
    }
}