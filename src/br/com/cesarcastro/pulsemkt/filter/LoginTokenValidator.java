package br.com.cesarcastro.pulsemkt.filter;

import java.util.StringTokenizer;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;

import org.glassfish.jersey.internal.util.Base64;

import br.com.cesarcastro.pulsemkt.dao.UserDao;
import br.com.cesarcastro.pulsemkt.model.User;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class LoginTokenValidator extends TokenValidator {

	public LoginTokenValidator(TokenValidator next, HttpServletRequest req) {
		super(next, req);
	}

	@Override
	protected boolean validate(String token) {

		try {
			String decodedString = Base64.decodeAsString(token.replace(SysConfig.TOKEN_LOGIN_PREFIX, ""));

			StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
			String userEmail = tokenizer.nextToken();
			String userPassword = tokenizer.nextToken();

			Matcher matcher = SysConfig.VALID_EMAIL_ADDRESS_REGEX.matcher(userEmail);

			if(!matcher.find())
				return false;
			
			UserDao dao = new UserDao();
			return dao.efetuaLogin(new User(userEmail, userPassword));
		} catch (Exception e) {
			return false;
		}

	}

}
