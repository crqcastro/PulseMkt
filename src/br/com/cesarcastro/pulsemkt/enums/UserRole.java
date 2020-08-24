package br.com.cesarcastro.pulsemkt.enums;

public enum UserRole {
	USER("USER"), ADMINISTRATOR("ADMINISTRATOR");

	private String desc;

	UserRole(String str) {
		this.desc = str;
	}

	public String getValue() {
		return desc;
	}
}
