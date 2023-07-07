package io.mobimenu.web.response;

import io.mobimenu.common.api.Code;

public record GenericResponse<T>(int code, T data) {
    public static <T> GenericResponse<T> from(T data) {
        return new GenericResponse<>(Code.GENERIC_SUCCESS.getCode(), data);
    }
}
