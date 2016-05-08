package org.nightswimming.thesis.domain;

import java.util.Arrays;
import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Subscription {
	
	private final Frequency frequency;
	private final Channel[] channels;
	
	public enum Frequency{ WEEKLY, DAILY, MONTHLY }
	public enum Channel{ POSTAL, MAIL, API, FTP }
	
	@JsonCreator
	public Subscription(@JsonProperty("frequency") final Frequency frequency, 
						@JsonProperty("channel")   final Channel[] channels){
		this.frequency = frequency;
		this.channels = channels;
	}
	
	public Frequency getFrequency(){ return frequency; }
	
	@JsonProperty("channel")
	public EnumSet<Channel> getChannels() { 
		EnumSet<Channel> temp = EnumSet.noneOf(Channel.class);
		    temp.addAll(Arrays.asList(channels));
		    return temp; 
	}
}
