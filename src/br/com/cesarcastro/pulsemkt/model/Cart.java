package br.com.cesarcastro.pulsemkt.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import br.com.cesarcastro.pulsemkt.enums.Status;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class Cart {

	@Expose
	private Integer id;
	@Expose
	private User user;
	@Expose
	private Collection<Product> products = new ArrayList<Product>();
	@Expose
	private BigDecimal amount = BigDecimal.ZERO;
	@Expose
	private Integer qtyProducts;
	@Expose
	private BigDecimal paymentAmount = BigDecimal.ZERO;
	@Expose
	private BigDecimal expectedPayment = BigDecimal.ZERO;
	@Expose
	private BigDecimal diference;
	@Expose
	private Boolean finalized;
	@Expose
	private Integer orderid;
	@Expose
	private Collection<PaymentMethod> payments = new ArrayList<PaymentMethod>();
	@Expose
	private Delivery deliveryMethod;
	@Expose
	private Status status;
	
	public Cart() {
	}

	public Cart(Integer id, User user, Collection<Product> products) {
		this.id = id;
		this.user = user;
		this.products = products;
		update();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return BigDecimal.valueOf(this.amount.doubleValue());
	}

	public void setId(Integer id) {
		this.id = id;

	}

	public Integer getId() {
		return this.id;
	}

	public Delivery getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(Delivery deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	public Address getUserAddress() {
		return this.user.getAddress();
	}

	public Integer getUserId() {
		int userId = this.user.getId();
		return userId;
	}

	public Collection<PaymentMethod> getPaymentList() {
		return payments;
	}

	private void updatePaymentAmount() {
		this.paymentAmount = this.payments.stream().filter(paymentMethod -> paymentMethod.isConcluded())
				.collect(Collectors.toList()).stream().map(item -> item.getValue())
				.reduce(this.paymentAmount, BigDecimal::add);
	}

	private void updateExpectedPayment() {
		this.expectedPayment = this.payments.stream().map(item -> item.getValue()).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		this.diference = this.amount.subtract(expectedPayment);
	}

	public Boolean isClosable() {
		return this.amount.compareTo(SysConfig.MIN_VALUE_CHECKOUT) >= 0;
	}

	public void addPaymentMethod(PaymentMethod paymentMethod) {
		this.payments.add(paymentMethod);
		update();
	}

	public void remPaymentMethod(PaymentMethod paymentMethod) {
		this.payments = this.payments.stream().filter(payment -> payment.compareTo(paymentMethod) != 0)
				.collect(Collectors.toList());
		update();
	}

	public static Cart fromJson(String json) {
		return new Gson().fromJson(json, Cart.class);
	}

	public String toJson() {
		update();
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
	}

	public void update() {
		calculateAmount();
		calculateQty();
		updatePaymentAmount();
		updateExpectedPayment();
	}

	private void calculateAmount() {
		this.amount = products.stream().map(x -> x.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private void calculateQty() {
		this.qtyProducts = products.size();
	}

	public void addProduct(Product product) {
		this.products.add(product);
		update();
	}

	public void delProduct(Product product) {
		this.products = this.products.stream().filter(prod -> prod.compareTo(product) != 0)
				.collect(Collectors.toList());

		update();

	}

	public boolean compareUser(User user) {
		return (this.user.getId().equals(user.getId()) && this.user.getName().contentEquals(user.getName())
				&& this.user.getUserRole() == user.getUserRole());
	}
}
