package com.industrieinformatik.taiga.start;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;


public class Broker {

  public static void main(String[] args) throws Exception {
    final Server server = new Server(8080);

    
    WebAppContext context = new WebAppContext();
    context.setDescriptor("WebContent/WEB-INF/web.xml");
    context.setResourceBase("src/main/webapp");
    context.setContextPath("/");
    context.setParentLoaderPriority(true);
    
    server.setHandler(context);
    
    server.start();
  }

}
