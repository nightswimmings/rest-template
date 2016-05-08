package org.nightswimming.thesis.rest.server;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SECURITY;
import static org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ShutdownHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.nightswimming.thesis.rest.endpoint.ThesisExceptionMapper;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class Server {

	private final int port;
	private final Object[] endpoints;
	private org.eclipse.jetty.server.Server jettyServer;

	public Server(final int port, final Object... endpoints) {
		this.port = port;
		this.endpoints = endpoints;
	}

	public void start(boolean enableLogging) throws Exception{
		start(enableLogging,null);
	}
	
	public void start(boolean enableLogging, String shutdownToken) throws Exception{	
		//Check jersey.config.server.provider.packages prop for importing all endpoints in a subpackage
		ResourceConfig config = new ResourceConfig()
				.register(JacksonJaxbJsonProvider.class) //Autoregitering of JacksonFeature seems to fail, 
				.register(ThesisExceptionMapper.class)  //and besides, it maps some shadowing ExceptionMappers
				.registerInstances(endpoints);
		if (enableLogging) config.register(LoggingFilter.class);	
		
		ServletContextHandler context = new ServletContextHandler(SESSIONS | NO_SECURITY); 
		context.addServlet(new ServletHolder(new ServletContainer(config)),"/*");
		context.addFilter(Server.createCORSFilter("*", "GET,POST"), "/*", EnumSet.of(DispatcherType.REQUEST));
		//Spring-Sec Fix: context.addFilter(new FilterHolder(new DelegatingFilterProxy(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)),"/*", EnumSet.allOf(DispatcherType.class)); //Spring-Security-Oauth2 

		//Order is important
		HandlerList handlers = new HandlerList();
		if(shutdownToken!=null) handlers.addHandler(new ShutdownHandler(shutdownToken,true,false));
		handlers.addHandler(context);
		
		jettyServer = new org.eclipse.jetty.server.Server(port);
		jettyServer.setHandler(handlers);
		jettyServer.start();
	}
	public void stop() throws Exception{ 
		if(jettyServer!=null) jettyServer.stop();
	}
	
	//TODO: Deal with joins() on stopped servers
	public void join() throws InterruptedException{ 
		if(jettyServer!=null) jettyServer.join();
	}

	//Not guaranteed if the server was not run with a shutdownHandler
	public static void fireBackgroundShutdownRequest(int port, String shutdownSecret) throws Exception{
        URL url = new URL("http","localhost", port,"/shutdown?token="+shutdownSecret);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.getResponseCode();
	}
	
	private static FilterHolder createCORSFilter(String allowedOrigins, String allowedMethods) {
		FilterHolder filter = new FilterHolder();
		filter.setInitParameter("allowedOrigins", allowedOrigins);
		filter.setInitParameter("allowedMethods", allowedMethods);
		filter.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
		filter.setFilter(new CrossOriginFilter());
		// allowedHeaders,preflightMaxAge,allowCredentials, exposeHeaders,
		// chainPreflight
		return filter;
	}
}