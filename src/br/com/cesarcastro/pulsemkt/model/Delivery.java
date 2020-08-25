package br.com.cesarcastro.pulsemkt.model;

import com.google.gson.annotations.Expose;

public class Delivery {

	@Expose
	private Integer id;
	@Expose
	private DeliveryType type;
	@Expose
	private String description;
	@Expose
	private Address address;

	public Delivery(Integer id, DeliveryType type, String description, Address address) {
		this.id = id;
		this.type = type;
		this.description = description;
		this.address = address;
	}

	public Integer getId() {
		return id;
	}

	public Delivery() {

	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DeliveryType getType() {
		return type;
	}

	public void setType(DeliveryType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
