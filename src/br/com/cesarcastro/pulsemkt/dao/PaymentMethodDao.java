package br.com.cesarcastro.pulsemkt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import br.com.cesarcastro.pulsemkt.enums.Status;
import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.PaymentMethod;
import br.com.cesarcastro.pulsemkt.model.PaymentModel;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class PaymentMethodDao {

	Connection con;

	public void getPaymentMethodList(Collection<PaymentMethod> paymentMethodList, Integer id)
			throws ServiceBusinessException, SQLException, Exception {
		con = SysConfig.getConnection();
		String qry = "select paymethodid, paymethoddescription from paymethods where paymethodstatus = ? and paymethodid = ?";
		PreparedStatement stmt = con.prepareStatement(qry);
		stmt.setString(1, Status.ACTIVE.getValue());
		stmt.setInt(2, id);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			try {
				String className = rs.getString("paymethoddescription");
				@SuppressWarnings("rawtypes")
				Class clazz = Class.forName("br.com.cesarcastro.pulsemkt.model." + className + "PaymentMethod");
				PaymentMethod pm = (PaymentMethod) clazz.newInstance();
				pm.setPaymentId(rs.getInt("paymethodid"));
				pm.setPaymentDescription(className);
				paymentMethodList.add(pm);
			} catch (Exception e) {
				e.printStackTrace();
				// nao conseguiu criar a clase, nao deve fazer nada, apenas ignorar
			}
		}

		rs.close();
		stmt.close();
		con.close();

	}

	public void getPaymentModelList(Collection<PaymentModel> paymentModelList)
			throws ServiceBusinessException, SQLException, Exception {
		con = SysConfig.getConnection();
		String qry = "select paymethodid, paymethoddescription from paymethods where paymethodstatus = ?";
		PreparedStatement stmt = con.prepareStatement(qry);
		stmt.setString(1, Status.ACTIVE.getValue());
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			PaymentModel paymentModel = new PaymentModel();
			paymentModel.setId(rs.getInt("paymethodid"));
			paymentModel.setDescription(rs.getString("paymethoddescription"));
			paymentModelList.add(paymentModel);
		}

		rs.close();
		stmt.close();
		con.close();

	}

	public PaymentMethod getPaymentById(Integer paymentId) throws ServiceBusinessException, SQLException, Exception {
		con = SysConfig.getConnection();
		String qry = "select paymethodid, paymethoddescription from paymethods where paymethodstatus = ? and paymethodid = ?";
		PreparedStatement stmt = con.prepareStatement(qry);
		stmt.setString(1, Status.ACTIVE.getValue());
		stmt.setInt(2, paymentId);
		ResultSet rs = stmt.executeQuery();
		PaymentMethod pm = null;
		if(rs.next()) {
			try {
				String className = rs.getString("paymethoddescription");
				@SuppressWarnings("rawtypes")
				Class clazz = Class.forName("br.com.cesarcastro.pulsemkt.model." + className + "PaymentMethod");
				pm = (PaymentMethod) clazz.newInstance();
				pm.setPaymentId(rs.getInt("paymethodid"));
				pm.setPaymentDescription(className);
			} catch (Exception e) {
				e.printStackTrace();
				// nao conseguiu criar a clase, nao deve fazer nada, apenas ignorar
			}
		}

		rs.close();
		stmt.close();
		con.close();
		
		return pm;
	}

}
