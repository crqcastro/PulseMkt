package br.com.cesarcastro.pulsemkt.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class Order {

	@Expose
	private Integer id;
	@Expose
	private BigDecimal orderValue = BigDecimal.ZERO;
	@Expose
	private Collection<Product> products = new ArrayList<Product>();
	@Expose
	private User consumer;
	@Expose
	private Collection<PaymentMethod> paymentList = new ArrayList<PaymentMethod>();
	@Expose
	private Delivery deliveryMethod;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(BigDecimal orderValue) {
		this.orderValue = orderValue;
	}

	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}

	public User getConsumer() {
		return consumer;
	}

	public void setConsumer(User consumer) {
		this.consumer = consumer;
	}

	public Collection<PaymentMethod> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(Collection<PaymentMethod> paymentList) {
		this.paymentList = paymentList;
	}

	public Delivery getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(Delivery deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public Order(Integer id, BigDecimal orderValue, Collection<Product> products, User consumer,
			Collection<PaymentMethod> paymentList, Delivery deliveryMethod) {
		super();
		this.id = id;
		this.orderValue = orderValue;
		this.products = products;
		this.consumer = consumer;
		this.paymentList = paymentList;
		this.deliveryMethod = deliveryMethod;
	}

	public Order() {
	}
	
	public void update() {
		this.orderValue = this.products.stream().map(product->product.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public String toJson() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
	}
}
