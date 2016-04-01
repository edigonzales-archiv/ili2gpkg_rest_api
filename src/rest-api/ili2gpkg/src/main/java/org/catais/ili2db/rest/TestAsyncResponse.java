package org.catais.ili2db.rest;

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


@Path("/test/async/")
public class TestAsyncResponse {

	
	@GET
	@Path("get")
	public void asyncGet(@Suspended final AsyncResponse ar) {

		//ar.setTimeout( 5, TimeUnit.SECONDS );
		ar.setTimeoutHandler( new MyTimeoutHandler( ) );
		ar.resume( someWork( ) );
	}
	
	protected String someWork()
	{
		try {
			Thread.sleep(10000);
			return "suuuuper";
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "gag";
		}
	}
}

class MyTimeoutHandler implements TimeoutHandler
{
	@Override
	public void handleTimeout( AsyncResponse response )
	{
		Response r = Response.ok( ).status( HttpURLConnection.HTTP_ACCEPTED ).build( );
		response.resume( r);
	}
}

//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//
//				DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
//
//				System.out.println("#### thread started: " 
//						+ df.format(new Date()) + " ####");
//				String result = veryExpensiveOperation();
//				System.out.println("#### thread finished: " 
//						+ df.format(new Date()) + " ####");
//
//				response.resume(result);
//			}
//
//			private String veryExpensiveOperation() {
//
//				try {
//
//					Thread.sleep(10000);
//				} 
//				catch (InterruptedException e) {
//
//					e.printStackTrace();
//				}
//
//				return "Woke up!";
//			}
//		}).start();
//
//		return Response.status(202).entity("Request accepted. " + "Long running operation started").build();
//	}


