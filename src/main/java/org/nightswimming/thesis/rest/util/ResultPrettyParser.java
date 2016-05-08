package org.nightswimming.thesis.rest.util;

import javax.ws.rs.core.Response.Status;

import org.nightswimming.thesis.rest.api.ErrorMsg;
import org.nightswimming.thesis.rest.client.Result;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ResultPrettyParser {
	
	public static String prettify(Result<?> result) throws JsonProcessingException{
		StringBuilder output = new StringBuilder();
		output.append(">>> Response HTTP Status: "+result.status()+"\n");
		
		if (result.isError() && result.getErrorMsg().isPresent()){
			ErrorMsg error = result.getErrorMsg().get();
			Status status = error.getStatus();
			output.append("status: "     +   status.getStatusCode()+" "+status.getReasonPhrase() +"\n" 
		                  + "errorCode: "  +   error.getErrorCode() +"\n" 
		                  + "message: "    +   error.getMessage()   +"\n\n");
		} else if(result.get().isPresent()){
			output.append(new JsonPrettyParser().prettify(result.get().get())+"\n");
		} else {
			output.append("(No actual Payload)\n\n");
		}
		return output.toString();
	}
}
