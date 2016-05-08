package org.nightswimming.thesis.domain;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonMappingException;

@Embeddable
public class Range {
	private int minValue;
	private int maxValue;
	
	protected Range(){}
	
	public Range(int minValue, int maxValue){
		this.minValue=minValue;
		this.maxValue=maxValue;
	}
	
	@JsonCreator
	public static Range of(int[] range) throws JsonMappingException{
		//TODO: Check how to convert it to JsonParseExc, without implementing a whole Deserializer (howto get curLocation)
		if(range.length!=2) throw new JsonMappingException("Expected a 2-element array representing a range but found "+range.length+" elements");
		return new Range(range[0],range[1]);
	}
	
	public Range(int[] range){
		this.minValue=range[0];
		this.maxValue=range[1];
	}
	
	@JsonValue
	public int[] getValues(){
		return new int[]{minValue,maxValue};
	}
	public int getMinValue(){ return minValue;}
	public int getMaxValue(){ return maxValue;}
}
