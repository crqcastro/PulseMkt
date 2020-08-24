package br.com.cesarcastro.pulsemkt.endpoint;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
import br.com.cesarcastro.pulsemkt.model.Product;
import br.com.cesarcastro.pulsemkt.service.ProductService;

@Path("products")
public class ProductsEndPoint {

	private ProductService service = new ProductService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listProducts(@Context HttpServletRequest req, @QueryParam("limit") Integer limit,
			@QueryParam("offset") Integer offset, @QueryParam("barcode") String barCode, @QueryParam("description") String description) {

		ResponseBuilder response = Response.ok();
		
		try {
			Collection<Product> products = new ArrayList<Product>();
			service.getProducts(products, barCode, description, offset, limit);

			response = Response.ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(products));
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
