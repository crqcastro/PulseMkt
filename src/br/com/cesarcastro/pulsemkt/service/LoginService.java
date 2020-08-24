package br.com.cesarcastro.pulsemkt.service;

import java.sql.SQLException;
import java.util.StringTokenizer;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.Base64;

import br.com.cesarcastro.pulsemkt.dao.LoginDao;
import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.User;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class LoginService {

	private LoginDao dao = new LoginDao();
	
	public User getUserFromToken(String token) {
		String decodedString = Base64.decodeAsString(token.replace(SysConfig.TOKEN_LOGIN_PREFIX, ""));

		StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
		String userEmail = tokenizer.nextToken();
		String userPassword = tokenizer.nextToken();

		return new User(userEmail, userPassword);
	}
	
	public void login(User user) throws ServiceBusinessException {
		try {
			dao.login(user);
		} catch (ServiceBusinessException e) {
			throw new ServiceBusinessException(String.valueOf(Response.Status.UNAUTHORIZED.getStatusCode()), e);
		} catch (SQLException e) {
			if(SysConfig.DB_EXCEPTIONS_2_HTTP_STATUS_CODES.containsKey(e.getClass().getCanonicalName())) {
				throw new ServiceBusinessException(SysConfig.DB_EXCEPTIONS_2_HTTP_STATUS_CODES.get(e.getClass().getCanonicalName()).toString(), e);
			}else {
				throw new ServiceBusinessException(Response.Status.INTERNAL_SERVER_ERROR.toString(), e);
			}
		} catch (Exception e) {
			throw new ServiceBusinessException(Response.Status.INTERNAL_SERVER_ERROR.toString(), e);
		}
	}
}
