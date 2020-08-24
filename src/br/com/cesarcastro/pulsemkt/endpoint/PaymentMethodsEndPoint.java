package br.com.cesarcastro.pulsemkt.endpoint;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.GsonBuilder;

import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.Authorization;
import br.com.cesarcastro.pulsemkt.model.PaymentModel;
import br.com.cesarcastro.pulsemkt.service.PaymentMethodService;

@Path("paymentmethod")
public class PaymentMethodsEndPoint {

	PaymentMethodService service = new PaymentMethodService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPaymentList(@Context HttpServletRequest req) {
		
		Collection<PaymentModel> paymentMethodList = new ArrayList<PaymentModel>();
		ResponseBuilder response = Response.ok();
		
		try {
			service.getPaymentModelList(paymentMethodList);
			response.entity(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(paymentMethodList));
			
		} catch (ServiceBusinessException e) {
			response  = Response.status(Integer.parseInt(e.getMessage())).entity(e.getMessage());
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
