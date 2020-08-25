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
import br.com.cesarcastro.pulsemkt.model.DeliveryType;
import br.com.cesarcastro.pulsemkt.service.DeliveryService;
import br.com.cesarcastro.pulsemkt.util.AppUtils;

@Path("delivery")
public class DeliveryEndPoint {

	DeliveryService service = new DeliveryService();

	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listDeliveryMethods(@Context HttpServletRequest req, @QueryParam("limit") Integer limit,
			@QueryParam("offset") Integer offset, @QueryParam("type") Integer type, @QueryParam("description") String description) {
		
		ResponseBuilder response = Response.ok();

		try {
			Collection<Delivery> deliveries = new ArrayList<Delivery>();
			service.getDeliveryList(deliveries, type, description, offset, limit);

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

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("types")
	public Response listDeliveryMethods(@Context HttpServletRequest req) {
		
		ResponseBuilder response = Response.ok();

		try {
			Collection<DeliveryType> types = new ArrayList<DeliveryType>();
			service.getDeliveryTypeList(types);

			response = Response.ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(types));
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
