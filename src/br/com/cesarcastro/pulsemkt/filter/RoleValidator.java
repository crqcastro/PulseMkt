package br.com.cesarcastro.pulsemkt.filter;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import br.com.cesarcastro.pulsemkt.dao.UserDao;
import br.com.cesarcastro.pulsemkt.enums.UserRole;
import br.com.cesarcastro.pulsemkt.model.User;
import br.com.cesarcastro.pulsemkt.util.AppUtils;

public class RoleValidator extends TokenValidator{

	public RoleValidator(TokenValidator next, HttpServletRequest req) {
		super(next, req);
	}

	@Override
	protected boolean validate(String token) {

		Collection<String> paths = Arrays.asList("/store:POST", "/user:GET","/delivery:POST", "/orders:GET");
		
		
		Collection<String> filtred = paths.stream().filter(item -> {
			String[] obj = item.split(":");
			String pathInfo = this.getRequest().getPathInfo();
			String method = this.getRequest().getMethod();
			if (pathInfo.length() == pathInfo.lastIndexOf("/") + 1)
				pathInfo = pathInfo.substring(0, pathInfo.lastIndexOf("/"));
			if (obj[0].contentEquals(pathInfo) && obj[1].contentEquals(method))
				return true;
			return false;
		}).collect(Collectors.toList());

		User user = AppUtils.getUserFromAuthToken(token);
		UserRole role = user.getUserRole();
		
		if(!filtred.isEmpty() && !role.equals(UserRole.ADMINISTRATOR))
			return false;
		
		UserDao dao = new UserDao();
		try {
			dao.efetuaLogin(user);
		} catch (Exception e) {
			return false;
		}
		
		return user.getUserRole().compareTo(UserRole.ADMINISTRATOR)==0?true:false;
	}

}
