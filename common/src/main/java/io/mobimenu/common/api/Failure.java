package io.mobimenu.common.api;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.core.Response;

/**
 * A failure represents anything that can go wrong within the smart menu domain
 */
@Getter
@Slf4j
public class Failure extends RuntimeException {

    private final Code error;

    private Failure(Code error, String message) {
        super(message);
        this.error = error;
    }

    public static Failure of(Code error) {
        return new Failure(error, error.getMessage());
    }

    public static Failure of(Code error, String message) {
        return new Failure(error, message);
    }

    public static Failure from(Throwable throwable) {
        if (throwable instanceof Failure failure) {
            return failure;
        }
        log.error("Exception processing request: ", throwable);
        return Failure.of(Code.GENERIC_ERROR, throwable.getMessage());
    }

    public static Response toResponse(Throwable throwable) {
        var error = Failure.from(throwable).getError();
        return Response.status(error.getHttpCode())
                .entity(new ApiErrorResponse(error.getCode(), error.getMessage()))
                .build();
    }

    public static Response toResponse(Throwable throwable, String message) {
        var error = Failure.from(throwable).getError();
        return Response.status(error.getHttpCode())
                .entity(new ApiErrorResponse(error.getCode(), message))
                .build();
    }
    
}
