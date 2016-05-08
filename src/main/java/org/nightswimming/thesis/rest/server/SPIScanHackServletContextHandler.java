package org.nightswimming.thesis.rest.server;

//import java.util.HashSet;
//
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.springframework.web.SpringServletContainerInitializer;
//
//import org.nightswimming.thesis.security.SecurityWebApplicationInitializer;
//
//public class SPIScanHackServletContextHandler extends ServletContextHandler {
//
//   public SPIScanHackServletContextHandler(int options){
//	   super(options);
//   }
//   
//   @Override
//   protected void startContext() throws Exception {
//	   this.getServletContext().setExtendedListenerTypes(true);
//	   SecurityWebApplicationInitializer initializer = new SecurityWebApplicationInitializer();
//	   initializer.onStartup(this.getServletContext());
//	   super.startContext();
//	}
//}
