package org.catais.ili2db.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;


@Path("/test/async/")
public class TestAsyncResponse {

	@GET
	@Path("get")
	public Response asyncGet(@Suspended final AsyncResponse response) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

				System.out.println("#### thread started: " 
						+ df.format(new Date()) + " ####");
				String result = veryExpensiveOperation();
				System.out.println("#### thread finished: " 
						+ df.format(new Date()) + " ####");

				response.resume(result);
			}

			private String veryExpensiveOperation() {

				try {

					Thread.sleep(10000);
				} 
				catch (InterruptedException e) {

					e.printStackTrace();
				}

				return "Woke up!";
			}
		}).start();

		return Response.status(202).entity("Request accepted. " + 
				"Long running operation started")
				.build();
	}
}

