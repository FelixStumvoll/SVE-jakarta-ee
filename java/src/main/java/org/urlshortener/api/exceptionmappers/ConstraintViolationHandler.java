package org.urlshortener.api.exceptionmappers;

import org.urlshortener.api.ErrorResponse;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationHandler implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(e.getConstraintViolations()
                                                  .stream()
                                                  .map(constraintViolation -> {
                                                      String lastElem = "";
                                                      for (Path.Node node : constraintViolation.getPropertyPath())
                                                          lastElem = node.getName();
                                                      return lastElem + " " + constraintViolation.getMessage();
                                                  })
                                                  .collect(Collectors.toList())))
                .build();
    }
}