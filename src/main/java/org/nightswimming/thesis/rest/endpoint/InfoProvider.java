package org.nightswimming.thesis.rest.endpoint;

import static org.nightswimming.thesis.rest.endpoint.InfoProvider.INFO_PROVIDER_BASEPATH;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.nightswimming.thesis.domain.Provider;
import org.nightswimming.thesis.domain.Requester;
import org.nightswimming.thesis.domain.Thesis;
import org.nightswimming.thesis.domain.Thesis.Country;
import org.nightswimming.thesis.rest.api.RequesMsg;
import org.nightswimming.thesis.rest.api.ResponseMsg;
import org.nightswimming.thesis.rest.exception.ThesisException;
import org.nightswimming.thesis.rest.exception.ThesisException.AuthenticationError;
import org.nightswimming.thesis.rest.exception.ThesisException.ThesisNotFoundException;
import org.nightswimming.thesis.rest.exception.ThesisException.UserNotAllowed;
import org.nightswimming.thesis.service.ThesisService;
import org.nightswimming.thesis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

@Controller
@Path(INFO_PROVIDER_BASEPATH)
public class InfoProvider {
	
	@Value("${rest.emptyEntitiesAsServerErrors}")
	private boolean emptyErrors;  
	public final static String INFO_PROVIDER_BASEPATH = "/api"; 
	public final static String REQUEST_SURVEYS_PATH = "/thesis"; 
	public final static String VERSION_AS_MEDIA_TYPE = "application/vnd.nightswimming.v1+json";
	private final static Sort  DEFAULT_SORTER = new Sort(new Order(Direction.ASC,"subject"),
													    new Order(Direction.DESC,"id"));

	@Autowired private ThesisService thesisService;
	@Autowired private UserService userService;
	
	//This is not typical RESTful arquitecture
	//Read: http://stackoverflow.com/questions/5020704/how-to-design-restful-search-filtering
	@POST
    @Path(REQUEST_SURVEYS_PATH+"/subject")
    @Consumes(VERSION_AS_MEDIA_TYPE)
    @Produces(VERSION_AS_MEDIA_TYPE)
	public Response requestInfoBySubject(RequesMsg infoRequest,
							             @QueryParam("page") int page) throws ThesisException {
		featuresToBeDone(infoRequest);
		Thesis thesisFilter = infoRequest.getThesis();
		List<Thesis> thesis = thesisService.findThesisBySubject(thesisFilter.getSubject(),Optional.ofNullable(thesisFilter.getCountry()), page, DEFAULT_SORTER);
		//Optional Exception
		if (emptyErrors && thesis.isEmpty()) throw new ThesisException.ThesisNotFoundException("'"+thesisFilter.getSubject()+"'");
		return buildResponse(null, thesis);
    }	
	
	@POST
    @Path(REQUEST_SURVEYS_PATH+"/target")
    @Consumes(VERSION_AS_MEDIA_TYPE)
    @Produces(VERSION_AS_MEDIA_TYPE)
	public Response requestInfoByTarget(RequesMsg infoRequest,
								        @QueryParam("page") int page) throws ThesisException {
		featuresToBeDone(infoRequest);
		Thesis thesisFilter = infoRequest.getThesis();
		if (emptyErrors && thesisFilter.getTarget() == null) throw new ThesisException.TargetNotValid();
		
		List<Thesis> thesis = thesisService.findThesisByTarget(thesisFilter.getTarget(), Optional.ofNullable(thesisFilter.getCountry()), page, DEFAULT_SORTER);		
		if (emptyErrors && thesis.isEmpty()) throw new ThesisException.TargetNotMatching();
		
		
		return buildResponse(null, thesis);
    }	
	
	
	/************************************************************ 
	 * RESTful Public Endpoints (Just for UI/Repository Testing)*
	 ************************************************************/
	
	@GET
	@Path(REQUEST_SURVEYS_PATH+"/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findThesis(@NotNull @PathParam("id") int id) throws ThesisNotFoundException{
		List<Thesis> thesis = Arrays.asList(thesisService.findThesisById(id).orElseThrow(() -> new ThesisException.ThesisNotFoundException(id)));		
		return buildResponse(null, thesis);
	}
	
	@GET
	@Path(REQUEST_SURVEYS_PATH+"{noop: (/)?}{country: ((?<=/)[A-Z]+)?}/subject/{subject}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response requestInfoBySubject(@NotNull @PathParam("subject") int subject,
												  @PathParam("country") Country country,
												  @QueryParam("page")   int page) throws ThesisNotFoundException{	
		List<Thesis> thesis = thesisService.findThesisBySubject(subject, Optional.ofNullable(country), page, DEFAULT_SORTER);
		//Optional Exception
		if (emptyErrors && thesis.isEmpty()) throw new ThesisException.ThesisNotFoundException("'"+subject+"'");	
		return buildResponse(null, thesis);
	}

	private Response buildResponse(Provider provider,List<Thesis> results){
		ResponseMsg infoResponse = new ResponseMsg(provider,results);
		return Response.ok(infoResponse).build();
	}
	
	//Authorization > Spring-Security
	//Subscription > QuartzScheduling
	private void featuresToBeDone(RequesMsg infoRequest) throws UserNotAllowed, AuthenticationError{
		simpleTestAuth(infoRequest.getProvider(), infoRequest.getRequester());
		//subscribe(infoRequest.getSubscription());	
	}

	//This is only for fancier demonstrations
	private void simpleTestAuth(Provider provider, Requester requester) throws UserNotAllowed, AuthenticationError {
		//checkIsCurrentProvider(provider); //In our example, we would be represented by {"LCL","Local"}
		boolean authenticated = true;
		try{
			UserDetails user = userService.loadUserByUsername(requester.getName());
			if (user.getAuthorities().isEmpty() || !user.getPassword().equalsIgnoreCase(requester.getId())) authenticated = false;
		} 
		catch(UsernameNotFoundException e){ authenticated = false; }
		catch(Exception e){
			throw new ThesisException.AuthenticationError(e.getMessage());
		}
		finally{
			if (emptyErrors && !authenticated) throw new ThesisException.UserNotAllowed();
		}
	}
}

