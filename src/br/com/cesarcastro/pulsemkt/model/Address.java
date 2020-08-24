package br.com.cesarcastro.pulsemkt.model;

import com.google.gson.annotations.Expose;

public class Address {
	
	@Expose
	private Integer addressId;
	@Expose
	private String address;
	@Expose
	private String number;
	@Expose
	private String complement;
	@Expose
	private String city;
	@Expose
	private String state;

	public Address() {}
	
	public Address(Integer addressId, String address, String number, String complement, String city, String state) {
		this.addressId = addressId;
		this.address = address;
		this.number = number;
		this.complement = complement;
		this.city = city;
		this.state = state;
	}
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public void setComplement(String complement) {
		this.complement = complement;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setState(String state) {
		this.state = state;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public String getAddress() {
		return address;
	}

	public String getNumber() {
		return number;
	}

	public String getComplement() {
		return complement;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}
	
	
}
