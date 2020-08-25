package br.com.cesarcastro.pulsemkt.endpoint;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.GsonBuilder;

import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.Authorization;
import br.com.cesarcastro.pulsemkt.model.Order;
import br.com.cesarcastro.pulsemkt.service.OrderService;

@Path("orders")
public class OrderEndPoint {

	private OrderService service = new OrderService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listOrders(@Context HttpServletRequest req, @QueryParam("limit") Integer limit,
			@QueryParam("offset") Integer offset, @QueryParam("init") String strInitDate,
			@QueryParam("end") String strEndDate, @QueryParam("status") String status, @QueryParam("orderid") Integer orderid) {

		ResponseBuilder response = Response.ok();
		
		try {
			Collection<Order> orders = new ArrayList<Order>();
			service.getOrderList(orders, strInitDate, strEndDate, status, orderid, offset, limit);

			response = Response.ok().entity(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(orders));
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
	@Path("{id}")
	public Response getOrder(@Context HttpServletRequest req, @PathParam("id") Integer orderid) {

		ResponseBuilder response = Response.ok();
		try {
			Collection<Order> orders = new ArrayList<Order>();
			service.getOrderList(orders, null, null, null, orderid, null, null);

			response = Response.ok().entity(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(orders.toArray()[0]));
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
