package br.com.cesarcastro.pulsemkt.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

public class AuthFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		Collection<String> byPassList = new ArrayList<String>();
		byPassList.add("/login:POST");
		byPassList.add("/user:POST");

		Collection<String> filtredByPass = byPassList.stream().filter(item -> {
			String[] obj = item.split(":");
			String pathInfo = req.getPathInfo();
			if (pathInfo.length() == pathInfo.lastIndexOf("/") + 1)
				pathInfo = pathInfo.substring(0, pathInfo.lastIndexOf("/"));
			if (obj[0].contentEquals(pathInfo) && obj[1].contentEquals(req.getMethod()))
				return true;
			return false;
		}).collect(Collectors.toList());

		if (!filtredByPass.isEmpty()) {
			chain.doFilter(request, response);
			return;
		} else {

			String authorizationHeader = req.getHeader(HttpHeaders.AUTHORIZATION);

			if (!StringUtils.isEmpty(authorizationHeader)) {

				TokenValidator validator = new RoleValidator(new SecureAccessTokenValidator(new LoginTokenValidator(null, req), req), req);

				if (validator.isValid(authorizationHeader)) {
					chain.doFilter(request, response);
					return;
				} else {
					resp.sendError(Status.UNAUTHORIZED.getStatusCode(), "Invalid token");
				}
			} else {
				resp.sendError(Status.UNAUTHORIZED.getStatusCode(), "Token not informed");
			}
		}

	}

	public void init(FilterConfig arg) throws ServletException {
	}

}
