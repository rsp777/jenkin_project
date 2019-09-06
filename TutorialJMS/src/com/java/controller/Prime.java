package com.java.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
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

import com.sun.mail.iap.Response;

/**
 * Servlet implementation class Prime
 */
public class Prime extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
		
		PrintWriter out = res.getWriter();
		
		out.println("<html><body>");
//		String option = req.getParameter("ac");
//		if(option.equals("message")) {
			String msg = req.getParameter("msg");
			Connection connection=null;
			try {
					Context initctx = new InitialContext();//Queue can be accessed by intialcontext
					Queue que = (Queue) initctx.lookup("java:/zensarqueue");
					Destination dest = (Destination)que;// 
				
					QueueConnectionFactory qcf = (QueueConnectionFactory) initctx.lookup("java:/ConnectionFactory");//Step 1
					//creating factory
					connection = qcf.createConnection();//Step 2//connection is ready
					
					Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);//Step 3//create a session
					
					//auto acknowledge will there or not 
					
					MessageProducer producer = session.createProducer(dest);//Step 4// Iwant to creating message producer based on the session
					//the dest is the queue message willl go there.
				
					TextMessage message = session.createTextMessage(msg);//Step 5
					//ready top create the mesage for every msg there should be one class.
				
					System.out.println("Sending Message:  "+message.getText());
					producer.send(message);//Step 6//ready to send the message using producer
					
					out.println("Message "+message.getText()+"sent successfully.");
					out.println("To receive message please <a href=ReceiveServlet>Click Here</a>");
			}
			catch(Exception e) {
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
