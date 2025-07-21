package com.cowrite.project.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 分页响应封装类，继承自 ApiResponse，用于统一分页格式。
 *
 * @author heathcetide
 */
public class PageResponse<T> extends ApiResponse<List<T>> {

    private Pagination pagination;

    /**
     * 根据 MyBatis-Plus 的分页对象构建响应
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        PageResponse<T> response = new PageResponse<>();
        response.setData(page.getRecords()); // 替换 getContent -> getRecords
        response.setPagination(new Pagination(
                (int) page.getCurrent(),          // 当前页
                (int) page.getSize(),             // 每页条数
                page.getTotal(),                  // 总条数
                (int) page.getPages()             // 总页数
        ));
        return response;
    }

    public static <T> PageResponse<T> of(int current, int size, long total, List<T> records) {
        PageResponse<T> response = new PageResponse<>();
        response.setData(records);
        response.setPagination(new Pagination(current, size, total,
                (int) ((total + size - 1) / size)));
        return response;
    }

    // 内部静态类用于封装分页元信息
    public static class Pagination {
        private int currentPage;
        private int pageSize;
        private long totalItems;
        private int totalPages;

        public Pagination(int currentPage, int pageSize, long totalItems, int totalPages) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalItems = totalItems;
            this.totalPages = totalPages;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public long getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(long totalItems) {
            this.totalItems = totalItems;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
