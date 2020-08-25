package br.com.cesarcastro.pulsemkt.endpoint;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.google.gson.GsonBuilder;

import br.com.cesarcastro.pulsemkt.enums.UserRole;
import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.Authorization;
import br.com.cesarcastro.pulsemkt.model.Delivery;
import br.com.cesarcastro.pulsemkt.service.StoreService;
import br.com.cesarcastro.pulsemkt.util.AppUtils;

@Path("stores")
public class StoreEndPoint {

	private StoreService service = new StoreService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStoreList(@Context HttpServletRequest req, @QueryParam("limit") Integer limit,
			@QueryParam("offset") Integer offset, @QueryParam("description") String description) {
		
		ResponseBuilder response = Response.ok();


		try {
			Collection<Delivery> deliveries = new ArrayList<Delivery>();
			service.getDeliveryList(deliveries, description, offset, limit);

			response = Response.ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(deliveries));
		} catch (ServiceBusinessException e) {
			response = Response.status(Integer.parseInt(e.getMessage())).entity(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response = Response.serverError();
		}
		return response
				.header(HttpHeaders.AUTHORIZATION,
						Authorization.toToken(Authorization.fromToken(req.getHeader(HttpHeaders.AUTHORIZATION))))
				.build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createDelivery(@Context HttpServletRequest req, Delivery delivery) {
		ResponseBuilder response = Response.ok();
		try {
			if (AppUtils.getUserFromAuthToken(req.getHeader(HttpHeaders.AUTHORIZATION))
					.getUserRole() != UserRole.ADMINISTRATOR) {
				response = Response.status(Status.FORBIDDEN);
			} else {
				service.create(delivery);
				response = Response.created(URI.create(req.getRequestURI() + delivery.getId()));
			}
		} catch (ServiceBusinessException e) {
			response = Response.status(Integer.parseInt(e.getMessage())).entity(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response = Response.serverError();
		}
		return response
				.header(HttpHeaders.AUTHORIZATION,
						Authorization.toToken(Authorization.fromToken(req.getHeader(HttpHeaders.AUTHORIZATION))))
				.build();
	}
}
