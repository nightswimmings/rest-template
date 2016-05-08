package org.nightswimming.thesis.rest.server;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

import javax.ws.rs.core.Response.Status;

import org.nightswimming.thesis.service.ThesisServiceErrorCode;

//Use http://i.stack.imgur.com/0pDZI.png as a guideline
public class ErrorStatusMapper {

	public static Status of(ThesisServiceErrorCode errorCode){
		switch(errorCode){
			case SURVEY_NOT_FOUND:     return NOT_FOUND;
			case NOT_VALID_TARGET:     return BAD_REQUEST;
			case NO_MATCHING_TARGET:   return NOT_FOUND; 
			case USER_NOT_ALLOWED:     return FORBIDDEN;
			case AUTHENTICATION_ERROR: return INTERNAL_SERVER_ERROR;
			/* ... */
			default: 				 return INTERNAL_SERVER_ERROR;
		}
	}
}
