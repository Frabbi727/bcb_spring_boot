package org.bracit.bcb_player_onboarding_backend.common.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private PageMeta meta;

    public static <T> PagedResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        boolean last = page >= totalPages - 1;
        
        PageMeta meta = PageMeta.builder()
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .last(last)
                .build();
                
        return PagedResponse.<T>builder()
                .content(content)
                .meta(meta)
                .build();
    }
}
