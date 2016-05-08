package org.nightswimming.thesis.rest.client;

import java.net.URI;
import java.util.Optional;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.nightswimming.thesis.domain.Provider;
import org.nightswimming.thesis.domain.Requester;
import org.nightswimming.thesis.domain.Subscription;
import org.nightswimming.thesis.domain.Thesis;
import org.nightswimming.thesis.domain.ThesisTarget;
import org.nightswimming.thesis.domain.Thesis.Country;
import org.nightswimming.thesis.rest.api.RequesMsg;
import org.nightswimming.thesis.rest.api.ResponseMsg;
import org.nightswimming.thesis.rest.endpoint.InfoProvider;

public class Client {

	private final Requester requester;
	private final javax.ws.rs.client.Client client;
	
	public Client(Requester requester) {
		this.requester = requester;
		client = ClientBuilder.newClient().register(JacksonFeature.class).register(new LoggingFilter());
	}

	public Result<ResponseMsg> requestForInfoBySubject(URI endpoint, Provider provider, int subject, Optional<Country> country, Subscription subscription){
		Thesis thesis = new Thesis(subject, null, country.orElse(null)); 
		return requestForInformation(endpoint,provider,thesis,subscription);
	}
	
	public Result<ResponseMsg> requestForInfoByTarget(URI endpoint, Provider provider, ThesisTarget thesisTarget, Optional<Country> country, Subscription subscription){
		Thesis thesis = new Thesis(-1, thesisTarget, country.orElse(null)); 
		return requestForInformation(endpoint,provider,thesis,subscription);
	}
	
	private Result<ResponseMsg> requestForInformation(URI target, Provider provider, Thesis thesis, Subscription subscription){
		RequesMsg request = new RequesMsg(requester,provider,thesis,subscription);
		Response response = client.target(target).request(InfoProvider.VERSION_AS_MEDIA_TYPE).post(Entity.entity(request,InfoProvider.VERSION_AS_MEDIA_TYPE));
		return Result.build(response, ResponseMsg.class); 
	}
	
	public void close(){
		client.close();
	}
}