package org.nightswimming.thesis.rest.exception;

import org.nightswimming.thesis.service.ThesisServiceErrorCode;

@SuppressWarnings("serial")
public class ThesisException extends Exception{
	
	private static final long serialVersionUID = 1L;
	private final ThesisServiceErrorCode errorCode;
	private final String message;

	public ThesisException(ThesisServiceErrorCode errorCode, Throwable throwable){
		this(errorCode,throwable.getMessage());
	}
	public ThesisException(ThesisServiceErrorCode errorCode, String message){
		this.errorCode = errorCode;
		this.message = message;
	}
	
	public ThesisServiceErrorCode getErrorCode(){ return errorCode; }
	public String    		  	   getMessage()  { return message; }

	
	/** CASE-CUSTOMIZED EXCEPTIONS **/
	
	public static class ThesisNotFoundException extends ThesisException{
    	public ThesisNotFoundException(int id){ super(ThesisServiceErrorCode.SURVEY_NOT_FOUND,"No Thesis was found with id: "+id);}
    	public ThesisNotFoundException(String subject){ super(ThesisServiceErrorCode.SURVEY_NOT_FOUND,"No Thesis was found with subject: "+subject);}
	}
    public static class TargetNotValid extends ThesisException{
    	public TargetNotValid(){ super(ThesisServiceErrorCode.NOT_VALID_TARGET,"No valid provided target in the request.");}
    }
    public static class TargetNotMatching extends ThesisException{
    	public TargetNotMatching(){ super(ThesisServiceErrorCode.NO_MATCHING_TARGET,"Target did not match any thesis.");}
    }
    public static class UserNotAllowed extends ThesisException{
    	public UserNotAllowed(){ super(ThesisServiceErrorCode.USER_NOT_ALLOWED,"Requestor user has no rights to consult thesis.");}
    }
    public static class AuthenticationError extends ThesisException{
    	public AuthenticationError(String message){ super(ThesisServiceErrorCode.AUTHENTICATION_ERROR,"Authentication engine failed: "+message);}
    }
}
