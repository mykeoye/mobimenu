package io.mobimenu.web.response;

import io.mobimenu.common.api.Code;

public record PagedResponse <T>(int code, T data, long totalItems) {
    public static <T> PagedResponse <T> of(T data, long totalItems) {
        return new PagedResponse<>(Code.GENERIC_SUCCESS.getCode(), data, totalItems);
    }
}
