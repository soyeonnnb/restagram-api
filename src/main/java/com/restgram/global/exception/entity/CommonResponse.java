package com.restgram.global.exception.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T>  {

    private boolean success;
    private int code;
    private String message;
    private T data;

}
