package br.com.cesarcastro.pulsemkt.filter;

import javax.servlet.http.HttpServletRequest;

public abstract class TokenValidator {

	private TokenValidator next;
	private HttpServletRequest req;
	
	public TokenValidator(TokenValidator next, HttpServletRequest req) {
		this.next = next;
		this.req = req;
	}
	protected boolean executeNext(String token) {
		if (this.next == null)
			return false;
		return next.isValid(token);
	}
	public boolean isValid(String token) {
		boolean valid = validate(token);
		System.out.println(valid);
		if(!valid)
			return executeNext(token);
		return true;
	}
	protected HttpServletRequest getRequest() {
		return this.req;
	}
	
	protected abstract boolean validate(String token);
}
