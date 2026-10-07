package com.example.starterapi.common.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Standard generic wrapper for paginated endpoints.
 * Decouples Spring's internal Page metadata from the external API contract.
 *
 * @param <T> Item type
 */
@Getter
public class PagedResponse<T> {

    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean isLast;

    public PagedResponse(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages, boolean isLast) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isLast = isLast;
    }

    public static <T> PagedResponse<T> from(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
