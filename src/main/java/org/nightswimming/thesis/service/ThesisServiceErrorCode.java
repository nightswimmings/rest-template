package org.nightswimming.thesis.service;

public enum ThesisServiceErrorCode {
	
	THESIS_NOT_FOUND     (666),
	NOT_VALID_TARGET     (667),
	NO_MATCHING_TARGET   (668),
	USER_NOT_ALLOWED     (669),
	AUTHENTICATION_ERROR (670);
	
	private final int errorCode;
	
	private ThesisServiceErrorCode(final int errorCode){
		this.errorCode = errorCode;
	}
	public int getErrorCode(){
		return errorCode;
	}
}
