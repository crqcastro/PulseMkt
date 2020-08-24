package br.com.cesarcastro.pulsemkt.model;

import java.math.BigDecimal;

public class GenericPaymentMethod extends PaymentMethod{

	public GenericPaymentMethod() {
		super();
	}
	
	public GenericPaymentMethod(Integer paymentId, String paymentDescription) {
		super(paymentId, paymentDescription);
	}

	public GenericPaymentMethod(Integer paymentId, String paymentDescription, BigDecimal value, Boolean concluded) {
		super(paymentId, paymentDescription, value, concluded);
	}
	
	@Override
	protected boolean paymentExecute() {
		return true;
	}

	

}
