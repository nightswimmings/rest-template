package org.nightswimming.thesis;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.nightswimming.thesis.rest.endpoint.InfoProvider;
import org.nightswimming.thesis.rest.server.Server;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;

public class ServerLauncher {
	private final static int defaultPort = 8080;
	private final static String shutdownSecret = "YouShouldStop";
	
	//TODO: Beautify with JParameter
	public static void main(String[] args) throws Exception {		
		System.getProperties().setProperty("java.net.preferIPv4Stack","true");
		
		boolean console = (System.console()!=null);
		if(!console){
			final String logFileName = "server.log";
			PrintStream outputLog = new PrintStream(logFileName, StandardCharsets.UTF_8.name());
			System.setOut(outputLog);
			System.setErr(outputLog);	
		}

		int firstArgOpt = IntStream.range(0,args.length)
		        				    .filter(i->args[i].startsWith("--"))
		        				    .findFirst().orElse(args.length);
		String[] preOptionVars = Arrays.stream(args).limit(firstArgOpt).toArray(String[]::new);
		
		
		try{	
			if (preOptionVars.length>2) throw new IllegalArgumentException("Too many arguments.");
			int port = (preOptionVars.length == 2)?Integer.parseInt(preOptionVars[1]):defaultPort;
			
			if (preOptionVars.length == 0 || preOptionVars[0].equalsIgnoreCase("Start")){
				
				@SuppressWarnings("resource")
				AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:META-INF/spring/applicationContext.xml");
				context.getEnvironment().getPropertySources().addFirst(new SimpleCommandLinePropertySource(args));
				context.refresh();
				InfoProvider endpoint = context.getBean(InfoProvider.class);
				try{
					Server restServer = new Server(port, endpoint);
					restServer.start(true,shutdownSecret);
					restServer.join();
					
				} catch(Exception e){
					System.out.println("\n>>> Error Running Server\n\n");
					e.printStackTrace();
				} finally {
					context.registerShutdownHook();
				}
			} 
			else if (preOptionVars[0].equalsIgnoreCase("Stop")){
				try{
					Server.fireBackgroundShutdownRequest(port,shutdownSecret);
				} catch(Exception e){
					System.out.println("\n>>> Error Stopping Server on port "+port+". Is the server actually up?\n\n");
					e.printStackTrace();
				}
			}
			else throw new IllegalArgumentException("Start|Stop was expected as the first argument.");
		} 
		catch(IllegalArgumentException e){
			System.out.println("\n>>> USAGE: ServerLauncher [start|stop [port]] [--devOption=devValue,...]\n\n");
			System.out.println("!!! "+e.getMessage()+" !!!\n");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.err.flush();
		System.out.println("\n>>> EXITING...");
	}
}
