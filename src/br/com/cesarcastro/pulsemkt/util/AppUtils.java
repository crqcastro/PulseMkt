package br.com.cesarcastro.pulsemkt.util;

import org.glassfish.jersey.internal.util.Base64;

import br.com.cesarcastro.pulsemkt.model.Authorization;
import br.com.cesarcastro.pulsemkt.model.User;

public class AppUtils {

	@SuppressWarnings("static-access")
	public static User getUserFromAuthToken(String token) {
		String decodedString = Base64.decodeAsString(token);
		return new Authorization().fromJson(decodedString).getUser();
		
	}
}
