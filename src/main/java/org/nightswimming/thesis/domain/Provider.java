package org.nightswimming.thesis.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Provider {

	private final String id;
	private final String name;
	 
	@JsonCreator
	public Provider (@JsonProperty("id")   final String id, 
					 @JsonProperty("name") final String name){
		this.id = id;
		this.name = name;
	}
	
	public String getId()  { return id; }
	public String getName(){ return name; }
}
