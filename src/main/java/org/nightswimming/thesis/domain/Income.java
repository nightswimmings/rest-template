package org.nightswimming.thesis.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable 
public class Income {
	@Enumerated(EnumType.STRING)
	@Column(length = 3)
	private Currency currency;
	
	@Embedded
	@AttributeOverrides({
	    @AttributeOverride(name="minValue",column=@Column(name="min_income")),
	    @AttributeOverride(name="maxValue",column=@Column(name="max_income"))
	})
	private Range range;
	public enum Currency{ EUR, USD, GBP}
	
	protected Income(){}
	
	@JsonCreator
	public Income(@JsonProperty("currency") Currency currency, 
				  @JsonProperty("range")    Range range){
		this.currency = currency;
		this.range = range;
	}
	
	public Currency getCurrency() { return currency; }
	public Range    getRange()    { return range; }
}
