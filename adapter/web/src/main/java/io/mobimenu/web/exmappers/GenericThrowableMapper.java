package io.mobimenu.web.exmappers;

import io.mobimenu.common.api.Code;
import io.mobimenu.common.api.Failure;
import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * This class maps mostly runtime exceptions and serves as a generic exception handler. It
 * converts a throwable to a http response, which api clients can understand
 */
@Slf4j
public class GenericThrowableMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        log.error("Exception processing request: ", exception);
        return Failure.toResponse(exception, Code.GENERIC_ERROR.getMessage());
    }

}
