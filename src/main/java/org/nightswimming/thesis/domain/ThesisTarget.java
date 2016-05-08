package org.nightswimming.thesis.domain;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class ThesisTarget {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private Gender gender;
	@Embedded
	@AttributeOverrides({
	    @AttributeOverride(name="minValue",column=@Column(name="min_age")),
	    @AttributeOverride(name="maxValue",column=@Column(name="max_age"))
	})
	@JsonProperty("age")
	private Range ageRange;
	@Embedded
	private Income income;
	
	public enum Gender {M, F}
	
	protected ThesisTarget(){}
	
	@JsonCreator
	public ThesisTarget(@JsonProperty("gender") final Gender gender, 
						@JsonProperty("age")    final Range ageRange, 
						@JsonProperty("income") final Income income){
		this.gender = gender;
		this.ageRange = ageRange;
		this.income = income;
	}
	
	public Gender   getGender()   { return gender; }
	public Range    getAgeRange() { return ageRange; }
	public Income   getIncome()   { return income; }
}
