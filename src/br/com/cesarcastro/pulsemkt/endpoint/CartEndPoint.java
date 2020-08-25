package br.com.cesarcastro.pulsemkt.endpoint;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.Authorization;
import br.com.cesarcastro.pulsemkt.model.Cart;
import br.com.cesarcastro.pulsemkt.model.Product;
import br.com.cesarcastro.pulsemkt.model.User;
import br.com.cesarcastro.pulsemkt.service.CartService;
import br.com.cesarcastro.pulsemkt.service.PaymentMethodService;
import br.com.cesarcastro.pulsemkt.util.AppUtils;

@Path("/cart")
public class CartEndPoint {

	CartService service = new CartService();

	@POST
	public Response createCart(@Context HttpServletRequest req) {

		ResponseBuilder response = Response.ok();

		try {
			User user = Authorization.fromToken(req.getHeader(HttpHeaders.AUTHORIZATION)).getUser();
			Cart cart = new Cart(null, user, new ArrayList<Product>());
			service.create(cart);
			response = Response.created(URI.create(req.getRequestURI() + cart.getId()));
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

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{cartid}/product/{productid}")
	public Response addProduct(@PathParam("cartid") Integer cartid, @PathParam("productid") Integer productid,
			@Context HttpServletRequest req) {

		ResponseBuilder response = Response.accepted();

		try {
			Cart cart = service.getCartById(cartid);
			if (cart.compareUser(AppUtils.getUserFromAuthToken(req.getHeader(HttpHeaders.AUTHORIZATION)))) {
				response = Response.status(Status.CONFLICT)
						.entity("{\"message\":\"The requisition user is not the same as the shopping cart\"}");
			} else {
				service.insertProduct(cartid, productid);
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

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{cartid}/product/{productid}")
	public Response remProduct(@PathParam("cartid") Integer cartid, @PathParam("productid") Integer productid,
			@Context HttpServletRequest req) {

		ResponseBuilder response = Response.accepted();

		try {
			Cart cart = service.getCartById(cartid);
			if (cart.compareUser(AppUtils.getUserFromAuthToken(req.getHeader(HttpHeaders.AUTHORIZATION)))) {
				response = Response.status(Status.CONFLICT)
						.entity("{\"message\":\"The requisition user is not the same as the shopping cart\"}");
			} else {
				service.deleteProduct(cartid, productid);
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

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{cartid}/checkout")
	public Response checkout(@PathParam("cartid") Integer cartid, @Context HttpServletRequest req) {

		ResponseBuilder response = Response.accepted();

		try {

			Cart cart = service.getCartById(cartid);
			if (cart.compareUser(AppUtils.getUserFromAuthToken(req.getHeader(HttpHeaders.AUTHORIZATION)))) {
				response = Response.status(Status.CONFLICT)
						.entity("{\"message\":\"The requisition user is not the same as the shopping cart\"}");
			}

			if (cart.isClosable()) {
				service.finalize(cart);
			} else {
				response = Response.notModified(cart.toJson());
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

	@POST
	@Path("/{cartid}/payment/{paymentid}/{value}")
	public Response addPaymentMthod(@Context HttpServletRequest req, @PathParam("cartid") Integer cartId,
			@PathParam("paymentid") Integer paymentId, @PathParam("value") BigDecimal value) {

		ResponseBuilder response = Response.accepted();
		try {
			Cart cart = service.getCartById(cartId);
			if (cart.compareUser(AppUtils.getUserFromAuthToken(req.getHeader(HttpHeaders.AUTHORIZATION)))) {
				response = Response.status(Status.CONFLICT)
						.entity("{\"message\":\"The requisition user is not the same as the shopping cart\"}");
			} else {
				service.addPaymentMethod(cartId, paymentId, value);
				cart.getPaymentList().add(new PaymentMethodService().getPaymentById(paymentId));
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

	@DELETE
	@Path("/{cartid}/payment/{paymentid}")
	public Response remPaymentMthod(@Context HttpServletRequest req, @PathParam("cartid") Integer cartId,
			@PathParam("paymentid") Integer paymentId) {

		ResponseBuilder response = Response.accepted();
		try {
			Cart cart = service.getCartById(cartId);
			if (cart.compareUser(AppUtils.getUserFromAuthToken(req.getHeader(HttpHeaders.AUTHORIZATION)))) {
				response = Response.status(Status.CONFLICT)
						.entity("{\"message\":\"The requisition user is not the same as the shopping cart\"}");
			} else {
				service.remPaymentMethod(cartId, paymentId);
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

	@PUT
	@Path("/{cartid}/payment/{paymentid}/{value}")
	public Response updatePaymentMthod(@Context HttpServletRequest req, @PathParam("cartid") Integer cartId,
			@PathParam("paymentid") Integer paymentId, @PathParam("value") BigDecimal value) {

		ResponseBuilder response = Response.accepted();
		try {
			Cart cart = service.getCartById(cartId);
			if (cart.compareUser(AppUtils.getUserFromAuthToken(req.getHeader(HttpHeaders.AUTHORIZATION)))) {
				response = Response.status(Status.CONFLICT)
						.entity("{\"message\":\"The requisition user is not the same as the shopping cart\"}");
			} else {
				service.updatePaymentMethod(cartId, paymentId, value);
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

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCartById(@Context HttpServletRequest req, @PathParam("id") Integer id) {
		ResponseBuilder response = Response.ok();

		try {
			Cart cart = service.getCartById(id);
			if (cart.compareUser(AppUtils.getUserFromAuthToken(req.getHeader(HttpHeaders.AUTHORIZATION)))) {
				response = Response.status(Status.CONFLICT)
						.entity("{\"message\":\"The requisition user is not the same as the shopping cart\"}");
			} else {
				response.entity(cart.toJson());
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

	@PUT
	@Path("/{cartid}/delivery/{deliveryid}")
	public Response setDeliveryMethod(@Context HttpServletRequest req, @PathParam("cartid") Integer cartId,
			@PathParam("deliveryid") Integer deliveryId) {

		ResponseBuilder response = Response.accepted();
		try {
			Cart cart = service.getCartById(cartId);
			if (cart.compareUser(AppUtils.getUserFromAuthToken(req.getHeader(HttpHeaders.AUTHORIZATION)))) {
				response = Response.status(Status.CONFLICT)
						.entity("{\"message\":\"The requisition user is not the same as the shopping cart\"}");
			} else {
				service.alterDelivery(cartId, deliveryId);
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
