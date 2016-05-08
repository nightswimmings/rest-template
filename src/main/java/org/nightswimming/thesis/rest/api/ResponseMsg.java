package org.nightswimming.thesis.rest.api;

import java.util.List;

import org.nightswimming.thesis.domain.Provider;
import org.nightswimming.thesis.domain.Thesis;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseMsg {
	private final Provider provider;
	private final List<Thesis> thesis;
	
	@JsonCreator
	public ResponseMsg(@JsonProperty("provider") Provider provider, 
							  @JsonProperty("thesis")List<Thesis> thesis){
		this.provider = provider;
		this.thesis = thesis;
	}
	
	public Provider     getProvider() { return provider; }
	public List<Thesis> getThesis()  { return thesis; }
}
