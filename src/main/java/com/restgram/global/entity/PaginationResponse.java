package com.restgram.global.entity;

import lombok.Builder;

@Builder
public record PaginationResponse<T>(
        Long cursorId,
        T list,
        boolean hasNext
) {
}
