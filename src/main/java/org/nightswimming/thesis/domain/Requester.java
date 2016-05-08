package org.nightswimming.thesis.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Requester {

	private String id;
	private String name;
	
	public Requester(@JsonProperty("id") String id, 
					 @JsonProperty("name") String name){
		this.id = id;
		this.name = name;
	}
	
	public String getId()  { return id; }
	public String getName(){ return name; }
}
