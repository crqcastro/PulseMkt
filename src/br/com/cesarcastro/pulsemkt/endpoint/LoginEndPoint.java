package br.com.cesarcastro.pulsemkt.endpoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.internal.util.Base64;

import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.Authorization;
import br.com.cesarcastro.pulsemkt.model.User;
import br.com.cesarcastro.pulsemkt.service.LoginService;

@Path("/login")
public class LoginEndPoint {

	private LoginService service = new LoginService();

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@HeaderParam(HttpHeaders.AUTHORIZATION) String token) {

		ResponseBuilder response = Response.ok();

		try {

			User user = service.getUserFromToken(token);
			service.login(user);

			String encodedUser = Base64.encodeAsString(new Authorization(user).toJson());
			return Response.ok(encodedUser).build();

		} catch (ServiceBusinessException e) {
			response = Response.status(Integer.parseInt(e.getMessage())).entity(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response = Response.serverError();
		}

		return response.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("validate")
	public Response validateToken(@HeaderParam(HttpHeaders.AUTHORIZATION) String token) {

		try {

			String json = Base64.decodeAsString(token);
			@SuppressWarnings("static-access")
			Authorization auth = new Authorization().fromJson(json);

			return auth.isValid() ? Response.ok(auth.toJson()).build() : Response.status(Status.UNAUTHORIZED).build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
