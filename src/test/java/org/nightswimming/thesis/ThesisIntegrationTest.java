package org.nightswimming.thesis;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.nightswimming.thesis.rest.endpoint.InfoProvider.INFO_PROVIDER_BASEPATH;
import static org.nightswimming.thesis.rest.endpoint.InfoProvider.REQUEST_SURVEYS_PATH;

import java.net.URI;
import java.util.Optional;

import javax.ws.rs.core.UriBuilder;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.nightswimming.thesis.domain.Income;
import org.nightswimming.thesis.domain.Provider;
import org.nightswimming.thesis.domain.Range;
import org.nightswimming.thesis.domain.Requester;
import org.nightswimming.thesis.domain.Subscription;
import org.nightswimming.thesis.domain.ThesisTarget;
import org.nightswimming.thesis.domain.Income.Currency;
import org.nightswimming.thesis.domain.Subscription.Channel;
import org.nightswimming.thesis.domain.Subscription.Frequency;
import org.nightswimming.thesis.domain.Thesis.Country;
import org.nightswimming.thesis.domain.ThesisTarget.Gender;
import org.nightswimming.thesis.rest.api.ResponseMsg;
import org.nightswimming.thesis.rest.client.Client;
import org.nightswimming.thesis.rest.client.Result;
import org.nightswimming.thesis.rest.endpoint.InfoProvider;
import org.nightswimming.thesis.rest.server.Server;
import org.nightswimming.thesis.rest.util.ResultPrettyParser;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;

//This should be unitary, and using mocks and stubs for integration and performance, 
//but for the application test, some integration tests are enough

public class ThesisIntegrationTest {
	
	private static final int testPort = 9090; 
	private static Client client;
	private static Server restServer;
	private static AbstractApplicationContext context;
	private static URI infoProviderbasePath;
	private final static Provider provider = new Provider("LCL","Local");
	private final static Requester requester = new Requester("NSM","Nightswimming");
	private final static Subscription subscription = new Subscription(Frequency.WEEKLY, new Channel[]{Channel.POSTAL, Channel.MAIL, Channel.API, Channel.FTP});	 
		
	@Rule
	public ExpectedException thrown= ExpectedException.none();
	
	@BeforeClass
	public static void setUp() throws Exception{
		context = new ClassPathXmlApplicationContext("classpath:META-INF/spring/applicationContext.xml");
		InfoProvider endpoint = context.getBean(InfoProvider.class);
		restServer = new Server(testPort, endpoint);
		restServer.start(false,null);
		client = new Client(requester);
		infoProviderbasePath = UriBuilder.fromUri("")
				 .scheme("http")
				 .host("localhost")
				 .port(testPort)
				 .path(INFO_PROVIDER_BASEPATH)
				 .path(REQUEST_SURVEYS_PATH).build();
	}
	
	/** HAPPY PATHS **/
	
	@Test
	public void testRequestForInfoBySubject() throws JsonProcessingException {
		Optional<Country> optCountry = Optional.empty();
		Result<ResponseMsg> result = testRequestForInfoBySubject(optCountry);
		System.out.println(ResultPrettyParser.prettify(result));
		assertThat(result.get().get().getThesis().size(), equalTo(2));
	}
	
	@Test
	public void testRequestForInfoByTarget() throws JsonProcessingException {
		Optional<Country> optCountry = Optional.empty();
		Result<ResponseMsg> result = testRequestForInfoByTarget(optCountry);
		//System.out.println(ResultPrettyParser.prettify(result));
		assertThat(result.get().get().getThesis().size(), equalTo(2));	
	}
	
	@Test
	public void testRequestForInfoBySubjectAndCountry() throws JsonProcessingException {
		Optional<Country> optCountry = Optional.of(Country.ES);
		Result<ResponseMsg> result = testRequestForInfoBySubject(optCountry);
		//System.out.println(ResultPrettyParser.prettify(result));
		assertThat(result.get().get().getThesis().size(), equalTo(1));
	}
	
	@Test
	public void testRequestForInfoByTargetAndCountry() throws JsonProcessingException {
		Optional<Country> optCountry = Optional.of(Country.ES);
		Result<ResponseMsg> result = testRequestForInfoByTarget(optCountry);
		//System.out.println(ResultPrettyParser.prettify(result));
		assertThat(result.get().get().getThesis().size(), equalTo(1));
	}
	
	/** CORNER CASES **/
	//NOT ENOUGH TIME - Target is null, returned errorMessages, etc..
	
	
	/** PRIVATE AUXILIAR METHODS **/
	
	public Result<ResponseMsg> testRequestForInfoBySubject(Optional<Country> optCountry) {
		int subject = 00000000;
		URI target = UriBuilder.fromUri(infoProviderbasePath).path("subject").build();
		return client.requestForInfoBySubject(target,provider, subject, optCountry, subscription);
	}
	
	private Result<ResponseMsg> testRequestForInfoByTarget(Optional<Country> optCountry) {
		ThesisTarget thesisTarget = new ThesisTarget(Gender.M, new Range(20,40), new Income(Currency.EUR, new Range(25000,40000)));	
		URI target = UriBuilder.fromUri(infoProviderbasePath).path("target").build();
		return client.requestForInfoByTarget(target,provider,thesisTarget,optCountry,subscription);
	}
	
	@AfterClass
	public static void shutDown() throws Exception{
		restServer.stop();
		restServer.join();
		context.registerShutdownHook();
	}
}
