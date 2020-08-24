package br.com.cesarcastro.pulsemkt.endpoint;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.GsonBuilder;

import br.com.cesarcastro.pulsemkt.enums.UserRole;
import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.Authorization;
import br.com.cesarcastro.pulsemkt.model.User;
import br.com.cesarcastro.pulsemkt.service.UserService;
import br.com.cesarcastro.pulsemkt.util.AppUtils;

@Path("/user")
public class UserEndPoint {

	private UserService service = new UserService();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(User user, @Context HttpServletRequest req) {

		ResponseBuilder response = Response.ok();
		if (!user.isEnrollable()) {
			response.status(Status.BAD_REQUEST);
		} else {
			try {
				service.create(user);
				response = Response.created(URI.create(req.getRequestURI() + user.getId()));
			} catch (ServiceBusinessException e) {
				response = Response.status(Integer.parseInt(e.getMessage())).entity(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				response = Response.serverError();
			}
		}
		return response == null ? Response.serverError().build() : response.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response getUserById(@PathParam("id") Integer id, @Context HttpServletRequest req) {

		User user = new User(id);
		ResponseBuilder response = Response.ok();

		try {
			service.getUserById(user);

			if (!user.compareUser(AppUtils.getUserFromAuthToken(req.getHeader(HttpHeaders.AUTHORIZATION)))) {
				response = Response.status(Status.UNAUTHORIZED);
			} else {
				if (StringUtils.isEmpty(user.getName()))
					response = Response.status(Status.NOT_FOUND);
				else
					response.entity(user.toJson());
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserList(@Context HttpServletRequest req, @QueryParam("limit") Integer limit,
			@QueryParam("offset") Integer offset, @QueryParam("emails") String emails,
			@QueryParam("name") String name) {


		ResponseBuilder response = Response.ok();
		Collection<User> users = new ArrayList<User>();
		try {
			if (AppUtils.getUserFromAuthToken(req.getHeader(HttpHeaders.AUTHORIZATION))
					.getUserRole() != UserRole.ADMINISTRATOR) {
				response = Response.status(Status.FORBIDDEN);
			} else {
				service.getUsers(users, emails, name, limit, offset);
				response.entity(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(users));
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(User user, @Context HttpServletRequest req) {
		ResponseBuilder response = Response.accepted();
		try {

			User userFromAuthToken = AppUtils.getUserFromAuthToken(req.getHeader(HttpHeaders.AUTHORIZATION));
			if (!user.compareUser(userFromAuthToken)
					|| !userFromAuthToken.getUserRole().equals(UserRole.ADMINISTRATOR)) {
				response = Response.status(Status.UNAUTHORIZED);
			} else {
				service.update(user);
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