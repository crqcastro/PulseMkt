package br.com.cesarcastro.pulsemkt.model;

import com.google.gson.annotations.Expose;

public final class PaymentModel {

	@Expose
	private Integer id;
	@Expose
	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
