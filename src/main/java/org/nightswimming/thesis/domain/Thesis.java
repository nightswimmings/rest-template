package org.nightswimming.thesis.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Thesis {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	private int subject;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="target_id")  
	private ThesisTarget target;
	@Enumerated(EnumType.STRING)
	@Column(length = 3)
	private Country country;
	
	public enum Country {ES, ITA, BRA}
	
	protected Thesis(){}

	@JsonCreator
	public Thesis(@JsonProperty("subject") final int subject, 
				  @JsonProperty("target")  final ThesisTarget target, 
				  @JsonProperty("country") final Country country) {
		this.subject = subject;
		this.target = target;
		this.country = country;
	}

	public long         getId()      { return id; }
	public int          getSubject() { return subject;}
	public ThesisTarget getTarget()  { return target; }
	public Country 		getCountry() { return country; }
}
