package br.com.cesarcastro.pulsemkt.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import br.com.cesarcastro.pulsemkt.dao.ProductDao;
import br.com.cesarcastro.pulsemkt.enums.Comparator;
import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.Product;
import br.com.cesarcastro.pulsemkt.model.QueryFilter;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class ProductService {

	private ProductDao dao = new ProductDao();

	public void getProducts(Collection<Product> products,String barcode, String description, Integer offset, Integer limit) throws ServiceBusinessException {
		try {
			Collection<QueryFilter> filters = new ArrayList<QueryFilter>();
			if(!StringUtils.isEmpty(barcode))
				filters.add(new QueryFilter("products.productbarcode", Comparator.EQ, barcode));
			if(!StringUtils.isEmpty(description))
				filters.add(new QueryFilter("products.productdescription", Comparator.EQ, description));
			
			dao.getProducts(products, filters, offset, limit);
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
