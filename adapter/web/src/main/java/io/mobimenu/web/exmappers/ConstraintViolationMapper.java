package io.mobimenu.web.exmappers;

import io.mobimenu.common.api.ApiErrorResponse;
import io.mobimenu.common.api.Code;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Provider
public class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        var violation = exception.getConstraintViolations().stream().findFirst().get();
         var field = StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                 .filter(node -> ElementKind.PROPERTY.equals(node.getKind()))
                 .map(Path.Node::getName)
                 .collect(Collectors.joining("."));

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ApiErrorResponse(Code.BAD_REQUEST_CLIENT_ERROR.getCode(), "%s %s".formatted(field, violation.getMessage())))
                .build();
    }

}
