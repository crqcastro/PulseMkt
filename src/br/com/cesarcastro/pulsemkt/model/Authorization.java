package br.com.cesarcastro.pulsemkt.model;

import java.time.LocalDateTime;

import org.glassfish.jersey.internal.util.Base64;

import com.google.gson.Gson;

import br.com.cesarcastro.pulsemkt.dao.LoginDao;

public class Authorization {

	private User user;
	private LocalDateTime expiration;
	
	public Authorization(User user) {
		this.user = user;
		this.expiration = LocalDateTime.now().plusMinutes(15L);
	}
	
	public Authorization() {
	}

	public String toJson() {
		return new Gson().toJson(this);
	}
	
	public User getUser() {
		return this.user;
	}
	
	public static Authorization fromJson(String json) {
		return new Gson().fromJson(json, Authorization.class);
	}
	
	public boolean isValid() {
		LoginDao dao = new LoginDao();
		try {
			dao.login(this.user);
		} catch (Exception e) {
			return false;
		}
		return !LocalDateTime.now().isAfter(this.expiration);
	}
	
	public Authorization updateExpiration() {
		this.expiration = LocalDateTime.now().plusMinutes(15L);
		return this;
	}
	
	@SuppressWarnings("static-access")
	public static Authorization fromToken(String token) {
		String json = Base64.decodeAsString(token);
		return new Authorization().fromJson(json);
	}
	
	public static String toToken(Authorization auth) {
		return Base64.encodeAsString(auth.toJson());
	}
}
