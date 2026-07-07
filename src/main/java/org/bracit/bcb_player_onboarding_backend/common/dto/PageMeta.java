package org.bracit.bcb_player_onboarding_backend.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageMeta {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
