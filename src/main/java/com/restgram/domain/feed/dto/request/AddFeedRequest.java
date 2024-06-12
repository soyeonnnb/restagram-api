package com.restgram.domain.feed.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddFeedRequest(

    @NotNull(message = "피드 작성 가게는 필수 영역입니다.")
    @Min(value = 0, message = "가게 ID는 음수가 될 수 없습니다.")
    Long storeId,

    @Size(max = 2000, message = "피드 내용은 최대 2000자까지 가능합니다.")
    @NotBlank(message = "피드 내용은 필수 영역입니다.")
    String content
    
) {

}