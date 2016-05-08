package org.nightswimming.thesis;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.ws.rs.core.UriBuilder;

import org.nightswimming.thesis.domain.Provider;
import org.nightswimming.thesis.domain.Requester;
import org.nightswimming.thesis.domain.Subscription;
import org.nightswimming.thesis.domain.ThesisTarget;
import org.nightswimming.thesis.domain.Subscription.Channel;
import org.nightswimming.thesis.domain.Subscription.Frequency;
import org.nightswimming.thesis.domain.Thesis.Country;
import org.nightswimming.thesis.rest.api.ResponseMsg;
import org.nightswimming.thesis.rest.client.Client;
import org.nightswimming.thesis.rest.client.Result;
import org.nightswimming.thesis.rest.util.JsonPrettyParser;
import org.nightswimming.thesis.rest.util.ResultPrettyParser;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ClientLauncher {
	
	private final static Provider provider = new Provider("LCL","Local");
	private final static Subscription subscription = new Subscription(Frequency.WEEKLY, new Channel[]{Channel.POSTAL, Channel.MAIL, Channel.API, Channel.FTP});	 
	
	//TODO: Beautify with JParameter
	public static void main(String[] args) throws URISyntaxException, JsonProcessingException {
		System.getProperties().setProperty("java.net.preferIPv4Stack","true");
		
		final JsonPrettyParser jsonParser = new JsonPrettyParser();
		final Requester defRequester = new Requester("NSM","Nightswimming");
				
		try{	
			if (args.length>4) throw new IllegalArgumentException("Too many arguments.");
			else if (args.length<3) throw new IllegalArgumentException("Too few arguments.");
			else {
				URI target = UriBuilder.fromUri(args[0]).build();
				Requester requester = (args.length==4)?jsonParser.deserialize(args[3],Requester.class):defRequester;
				Optional<Country> optCountry = Optional.ofNullable(args[2].equalsIgnoreCase("ANY")?null:Country.valueOf(args[2]));
				
				int subject = -1;
				ThesisTarget thesisTarget = null;
				if (args[1].startsWith("{")){
					thesisTarget = new JsonPrettyParser().deserialize(args[1],ThesisTarget.class);
				} else {
					subject = Integer.parseInt(args[1]);
				}
				
				Client client = null;
				try{
					System.out.println("\n>>> Connecting as "+requester.getName()+":"+requester.getId()+"...\n");
					client = new Client(requester);
					Result<ResponseMsg> result = (thesisTarget == null)?
							client.requestForInfoBySubject(target,provider, subject, optCountry, subscription)
						  :	client.requestForInfoByTarget(target,provider,thesisTarget,optCountry,subscription);
					System.out.println(ResultPrettyParser.prettify(result));
				} catch(Exception e){
					System.out.println("\n>>> Error Running Client: "+e.getMessage()+"\n\n");
				} finally {
					try{client.close();}catch(Exception e){}
				}
			}
		} 
		catch(Exception  e){
			System.out.println("\n>>> USAGE: ClientLauncher 	[URL] [subjectNum|targetJson] [CountryCode|ANY] (Requester)");
			System.out.println(">>>        ClientLauncher 	http://localhost:8080/api/thesis/subject 00000000 ES");
			System.out.println(">>>        ClientLauncher 	http://localhost:8080/api/thesis/subject 00000000 ANY {\\\"id\\\":\\\"NSM\\\",\\\"name\\\":\\\"Nightswimming\\\"}");
			System.out.println(">>>        ClientLauncher 	http://localhost:8080/api/thesis/target {\\\"gender\\\":\\\"M\\\",\\\"age\\\":[20,40],\\\"income\\\":{\\\"currency\\\":\\\"EUR\\\",\\\"range\\\":[25000,40000]}} ITA\n\n");
			System.out.println("!!! "+e.getMessage()+" !!!");
		}
		System.err.flush();
		System.out.println("\n>>> EXITING...");
	}
}
