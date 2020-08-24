package br.com.cesarcastro.pulsemkt.enums;

public enum Comparator {

	EQ("="),
	GT(">"),
	GE(">="),
	NE("!="),
	LIKE("like"),
	IN("in"),
	LT("<"),
	LE("<=");
	
	private String desc;
	
	Comparator(String str) {
		this.desc = str;
	}

	public String getValue() {
		return desc;
	}
	
}
