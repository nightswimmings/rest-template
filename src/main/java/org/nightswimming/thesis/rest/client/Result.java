package org.nightswimming.thesis.rest.client;

import java.io.IOException;
import java.util.Optional;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.nightswimming.thesis.rest.api.ErrorMsg;
import org.nightswimming.thesis.rest.util.JsonPrettyParser;

public class Result<T> {
	private final int status;
	private final boolean isError;
	private final Optional<T> optEntity;
	private final Optional<ErrorMsg> optError;
	
	private Result(int status, T entity, ErrorMsg errorMsg, boolean hasError){
		this.status    = status;
		this.isError   = hasError;
		this.optEntity = Optional.ofNullable(entity);
		this.optError  = Optional.ofNullable(errorMsg);
	}
	protected static <T> Result<T> build(Response unreadedResponse, Class<T> expected){
		boolean success = (unreadedResponse.getStatus()>= 200 && unreadedResponse.getStatus()<300);
		String literalResponse = unreadedResponse.readEntity(String.class);
		
		try {
			//Can't help but dealing with low-level serialization instead of readEntity(type), since stream can only be consumed once
			//If we need to parse non-expected HTML errors from server that is the only way
			JsonPrettyParser parser = new JsonPrettyParser();
			if (success){
				T result = parser.deserialize(literalResponse, expected);
				return new Result<T>(unreadedResponse.getStatus(),result,null,!success);
			} else {
				ErrorMsg result = parser.deserialize(literalResponse, ErrorMsg.class);
				return new Result<T>(unreadedResponse.getStatus(),null,result,!success);
			}		
		} catch (IOException serializationException) {
			//Not valid JSON (usually because server returned a top-most level error). We mask errorResponse for consistency
			ErrorMsg errorMsg = ErrorMsg.of(Status.fromStatusCode(unreadedResponse.getStatus()),-1,literalResponse);
			return new Result<T>(unreadedResponse.getStatus(),null,errorMsg,true);
		}
	}
	
	public int                status()     { return status; }
	public boolean            isError()    { return isError; }  
	public Optional<T>        get()        { return optEntity;}
	public Optional<ErrorMsg> getErrorMsg(){ return optError; }
}