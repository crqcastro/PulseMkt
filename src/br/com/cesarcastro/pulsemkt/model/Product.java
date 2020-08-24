package br.com.cesarcastro.pulsemkt.model;

import java.math.BigDecimal;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

public class Product implements Comparable<Product>{

	@Expose
	private Integer id;
	@Expose
	private String codBar;
	@Expose
	private String description;
	@Expose
	private BigDecimal value;
	@Expose
	private String image;
	
	public Product(Integer id, String codBar, String description, BigDecimal value, String image) {
		this.id = id;
		this.codBar = codBar;
		this.description = description;
		this.value = value;
		this.image = image;
	}

	public String getCodBar() {
		return codBar;
	}

	public void setCodBar(String codBar) {
		this.codBar = codBar;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Product() {
	}

	public BigDecimal getValue() {
		return this.value;
	}
	public static Product fromJson(String json) {
		return new Gson().fromJson(json, Product.class);
	}
	
	public String toJson() {
		return new Gson().toJson(this);
	}

	@Override
	public int compareTo(Product p) {
		if(p.codBar.equals(this.codBar))
			return 0;
		else
			return -1;
	}

	public Integer getId() {
		return this.id;
	}
}
