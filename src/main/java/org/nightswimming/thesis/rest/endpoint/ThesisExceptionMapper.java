package org.nightswimming.thesis.rest.endpoint;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.nightswimming.thesis.rest.api.ErrorMsg;
import org.nightswimming.thesis.rest.exception.ThesisException;
import org.nightswimming.thesis.rest.server.ErrorStatusMapper;
import org.nightswimming.thesis.service.ThesisServiceErrorCode;

public class ThesisExceptionMapper implements ExceptionMapper<Throwable> {
		
    public Response toResponse(Throwable exception) {
    	
    	final int errorCode;
    	final String message;
		final Status httpStatus;
		
    	if (exception instanceof ThesisException){
    		ThesisServiceErrorCode thesisBizErrorCode  = ((ThesisException) exception).getErrorCode();
    		errorCode  = thesisBizErrorCode.getErrorCode();
    		message    = ((ThesisException) exception).getMessage();
    		httpStatus = ErrorStatusMapper.of(thesisBizErrorCode);
    	} else {
    		/* ...TODO (especially both default JacksonMappers)... */
        	errorCode = 0;
        	message = exception.getMessage();
    		httpStatus = Status.INTERNAL_SERVER_ERROR;
    	}
    	
    	ErrorMsg errorMessage = ErrorMsg.of(httpStatus,errorCode,message);
    	return Response.status(httpStatus).entity(errorMessage).type(InfoProvider.VERSION_AS_MEDIA_TYPE).build();
    }
}
