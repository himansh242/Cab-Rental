package com.cab.rental.service.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericResponse<T> {

    @Builder.Default
    private boolean success = true;
    private T data;

    public static <T> GenericResponse<T> ok(T data) {
        return GenericResponse.<T> builder()
                .data(data)
                .build();
    }
}
