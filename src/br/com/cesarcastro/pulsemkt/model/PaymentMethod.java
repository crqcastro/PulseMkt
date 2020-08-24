package br.com.cesarcastro.pulsemkt.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import br.com.cesarcastro.pulsemkt.util.SensiveDataEstrategy;

public abstract class PaymentMethod {

	@Expose
	protected Integer paymentId;
	@Expose
	protected String paymentDescription;
	@Expose
	protected BigDecimal value = BigDecimal.ZERO;
	@Expose
	protected Boolean concluded = Boolean.FALSE;
	protected Integer maxAttempts = 2;

	public PaymentMethod() {
	}

	public PaymentMethod(Integer paymentId, String paymentDescription) {
		this.paymentId = paymentId;
		this.paymentDescription = paymentDescription;
		this.concluded = Boolean.FALSE;
	}

	public PaymentMethod(Integer paymentId, String paymentDescription, BigDecimal value, Boolean concluded) {
		this.paymentId = paymentId;
		this.paymentDescription = paymentDescription;
		this.value = value;
		this.concluded = concluded;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public void setPaymentDescription(String paymentDescription) {
		this.paymentDescription = paymentDescription;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public void setConcluded(Boolean concluded) {
		this.concluded = concluded;
	}

	public void setMaxAttempts(Integer maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public BigDecimal getValue() {
		return this.value;
	}

	public Boolean pay() {
		this.concluded = paymentExecute() ? true : false;
		return this.concluded;
	}

	protected abstract boolean paymentExecute();

	public Boolean isConcluded() {
		return this.concluded;
	}

	public String toJson() {
		Collection<ExclusionStrategy> exclusions = Arrays
				.asList(new SensiveDataEstrategy("maxAttempts", this.getClass()));
		return new GsonBuilder().setExclusionStrategies((ExclusionStrategy[]) exclusions.toArray()).create()
				.toJson(this);
	}

	public String toEntireJson() {
		return new Gson().toJson(this);
	}

	protected PaymentMethod fromJson(String json) {
		return new Gson().fromJson(json, PaymentMethod.class);
	}

	public int compareTo(PaymentMethod payment) {
		if (this.paymentDescription.equals(payment.paymentDescription) && this.value.equals(payment.value)
				&& this.concluded == payment.concluded) {
			return 0;
		}
		return -1;
	}
}
