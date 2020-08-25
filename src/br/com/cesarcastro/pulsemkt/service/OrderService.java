package br.com.cesarcastro.pulsemkt.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import br.com.cesarcastro.pulsemkt.dao.OrderDao;
import br.com.cesarcastro.pulsemkt.enums.Comparator;
import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.Order;
import br.com.cesarcastro.pulsemkt.model.QueryFilter;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class OrderService {

	private OrderDao dao = new OrderDao();

	public void getOrderList(Collection<Order> orders, String strInitDate, String strEndDate, String status,
			Integer orderid, Integer offset, Integer limit) throws ServiceBusinessException {
		try {
			Collection<QueryFilter> filters = new ArrayList<QueryFilter>();
			if (!StringUtils.isEmpty(strInitDate))
				filters.add(new QueryFilter("c.enddate", Comparator.GE, "str_to_date('" + strInitDate + "', '%m-%d-%Y')"));
			if (!StringUtils.isEmpty(strEndDate))
				filters.add(new QueryFilter("c.enddate", Comparator.LE, "str_to_date('" + strEndDate + "', '%m-%d-%Y')"));
			if (!StringUtils.isEmpty(status))
				filters.add(new QueryFilter("c.cartstatus", Comparator.EQ, status));
			if (orderid!=null && !StringUtils.isEmpty(orderid.toString()))
				filters.add(new QueryFilter("c.cartid", Comparator.EQ, orderid.toString()));

			dao.getOrderList(orders, filters, offset, limit);
		} catch (ServiceBusinessException e) {
			throw new ServiceBusinessException(String.valueOf(Response.Status.UNAUTHORIZED.getStatusCode()), e);
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
