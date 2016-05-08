package org.nightswimming.thesis.rest.api;

import org.nightswimming.thesis.domain.Provider;
import org.nightswimming.thesis.domain.Requester;
import org.nightswimming.thesis.domain.Subscription;
import org.nightswimming.thesis.domain.Thesis;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RequesMsg {
	
	private final Requester requester;
	private final Provider provider;
	private final Thesis thesis;
	private final Subscription subscription;
	
	@JsonCreator
	public RequesMsg(@JsonProperty("requester")    final Requester requester, 
							 @JsonProperty("provider")     final Provider provider, 
							 @JsonProperty("thesis")       final Thesis thesis, 
							 @JsonProperty("subscription") final Subscription subscription){
		this.requester = requester;
		this.provider = provider;
		this.thesis = thesis;
		this.subscription = subscription;
	}
	
	public Requester    getRequester()    { return requester; }
	public Provider     getProvider()     { return provider; }
	public Thesis       getThesis()       { return thesis; }
	public Subscription getSubscription() {	return subscription; }
}
