package br.com.cesarcastro.pulsemkt.enums;

public enum Status {

	ACTIVE("A"), CANCELED("C"), FINISHED("F"), PENDING_PAYMENT("P"), PENDING_DELIVERY("D");

	private String desc;

	Status(String str) {
		this.desc = str;
	}

	public String getValue() {
		return desc;
	}

	public static Status getStatusByDesc(String desc) {
		  for(Status e: Status.values()) {
		    if(e.desc.contentEquals(desc)) {
		      return e;
		    }
		  }
		  return null;// not found
		}

}
