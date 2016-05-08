package org.nightswimming.thesis.rest.api;

import java.net.URL;

import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMsg {
	private final Status httpStatus;
	private final int errorCode;
	private final String message;
	@JsonIgnore 
	private URL linkToAPIRef; //Placeholder
 
	@JsonCreator
	private ErrorMsg(@JsonProperty("status")     Status httpStatus,
					 @JsonProperty("errorCode")  int errorCode,
					 @JsonProperty("message")    String message) {
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.message = message;
	}
	
	public static ErrorMsg of(Status httpStatus, int errorCode, String message){
		return new ErrorMsg(httpStatus,errorCode,message);
	}
	
	public Status getStatus()    { return httpStatus; }
	public int    getErrorCode() { return errorCode; }
	public String getMessage()   { return message; }
}
