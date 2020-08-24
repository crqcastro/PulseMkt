package br.com.cesarcastro.pulsemkt.service;

import java.sql.SQLException;
import java.util.Collection;

import javax.ws.rs.core.Response;

import br.com.cesarcastro.pulsemkt.dao.PaymentMethodDao;
import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.PaymentMethod;
import br.com.cesarcastro.pulsemkt.model.PaymentModel;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class PaymentMethodService {

	PaymentMethodDao dao = new PaymentMethodDao();

	public void getPaymentMethodList(Collection<PaymentMethod> paymentMethodList, Integer id)
			throws ServiceBusinessException {

		try {
			dao.getPaymentMethodList(paymentMethodList, id);
		} catch (SQLException e) {
			if (SysConfig.DB_EXCEPTIONS_2_HTTP_STATUS_CODES.containsKey(e.getClass().getCanonicalName())) {
				throw new ServiceBusinessException(
						SysConfig.DB_EXCEPTIONS_2_HTTP_STATUS_CODES.get(e.getClass().getCanonicalName()).toString(), e);
			} else {
				throw new ServiceBusinessException(Response.Status.INTERNAL_SERVER_ERROR.toString(), e);
			}
		} catch (Exception e) {
			throw new ServiceBusinessException(Response.Status.INTERNAL_SERVER_ERROR.toString(), e);

		}

	}

	public void getPaymentModelList(Collection<PaymentModel> paymentMethodList) throws ServiceBusinessException {

		try {
			dao.getPaymentModelList(paymentMethodList);
		} catch (SQLException e) {
			if (SysConfig.DB_EXCEPTIONS_2_HTTP_STATUS_CODES.containsKey(e.getClass().getCanonicalName())) {
				throw new ServiceBusinessException(
						SysConfig.DB_EXCEPTIONS_2_HTTP_STATUS_CODES.get(e.getClass().getCanonicalName()).toString(), e);
			} else {
				throw new ServiceBusinessException(Response.Status.INTERNAL_SERVER_ERROR.toString(), e);
			}
		} catch (Exception e) {
			throw new ServiceBusinessException(Response.Status.INTERNAL_SERVER_ERROR.toString(), e);

		}

	}

	public PaymentMethod getPaymentById(Integer paymentId) throws ServiceBusinessException {
		try {
			return dao.getPaymentById(paymentId);
		} catch (SQLException e) {
			if (SysConfig.DB_EXCEPTIONS_2_HTTP_STATUS_CODES.containsKey(e.getClass().getCanonicalName())) {
				throw new ServiceBusinessException(
						SysConfig.DB_EXCEPTIONS_2_HTTP_STATUS_CODES.get(e.getClass().getCanonicalName()).toString(), e);
			} else {
				throw new ServiceBusinessException(Response.Status.INTERNAL_SERVER_ERROR.toString(), e);
			}
		} catch (Exception e) {
			throw new ServiceBusinessException(Response.Status.INTERNAL_SERVER_ERROR.toString(), e);

		}

	}
}
