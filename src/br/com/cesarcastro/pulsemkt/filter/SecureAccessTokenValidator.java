package br.com.cesarcastro.pulsemkt.filter;

import javax.servlet.http.HttpServletRequest;

import org.glassfish.jersey.internal.util.Base64;

import br.com.cesarcastro.pulsemkt.model.Authorization;

public class SecureAccessTokenValidator extends TokenValidator {

	public SecureAccessTokenValidator(TokenValidator next, HttpServletRequest req) {
		super(next, req);
	}

	@SuppressWarnings("static-access")
	@Override
	protected boolean validate(String token) {
		
		String decodedString = Base64.decodeAsString(token);
		if(decodedString.contains("expiration")){
			return new Authorization().fromJson(decodedString).isValid();
		}
		return false;
	}

}
