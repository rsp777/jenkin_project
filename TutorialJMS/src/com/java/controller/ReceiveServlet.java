package com.java.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReceiveServlet
 */
public class ReceiveServlet extends HttpServlet 
{

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
res.setContentType("text/html");
		
		PrintWriter out = res.getWriter();
		
		out.println("<html><body>");
//		String option = req.getParameter("ac");
//		if(option.equals("message")) {
		
			Connection connection=null;
			try 
			{
					Context initctx = new InitialContext();
					Queue que = (Queue) initctx.lookup("java:/zensarqueue");
					Destination dest = que;
				
					QueueConnectionFactory qcf = (QueueConnectionFactory) initctx.lookup("java:/ConnectionFactory");//Step 1
					//creating factory
					connection = qcf.createConnection();//Step 2//connection is ready
					
					Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);//Step 3//create a session
					
					//auto acknowledge will there or not 
					
					MessageConsumer consumer = (MessageConsumer) session.createConsumer(dest);
					connection.start();
					
					while(true) 
					{
						Message m = consumer.receive(1);// until message is available you receivem
						if(m != null) 
						{
							if(m instanceof TextMessage) 
							{
								TextMessage message = (TextMessage) m;
								out.println("Reading Message: <h1 style=>"+message.getText()+"</h1>   ");
							}
							else 
							{
								break;
							}
						}
					}
				
					out.println(" To send message please <a href=home.html>Click Here!</a>");
			}
			catch(Exception e) 
			{
				System.err.println("Exception Occured: "+e.toString());
			}
			
			finally {
				try {
					connection.close();
				}
				catch(JMSException e) {
					e.printStackTrace();
				}
			}
//		}
		out.println("</body></html>");
	}
	
}
