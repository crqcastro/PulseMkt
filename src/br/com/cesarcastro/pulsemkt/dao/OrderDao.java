package br.com.cesarcastro.pulsemkt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.Address;
import br.com.cesarcastro.pulsemkt.model.Delivery;
import br.com.cesarcastro.pulsemkt.model.DeliveryType;
import br.com.cesarcastro.pulsemkt.model.Order;
import br.com.cesarcastro.pulsemkt.model.PaymentMethod;
import br.com.cesarcastro.pulsemkt.model.Product;
import br.com.cesarcastro.pulsemkt.model.QueryFilter;
import br.com.cesarcastro.pulsemkt.model.User;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class OrderDao {

	private Connection con;

	public void getOrderList(Collection<Order> orders, Collection<QueryFilter> filters, Integer offset, Integer limit)
			throws ServiceBusinessException, SQLException, Exception {
		con = SysConfig.getConnection();

		StringBuilder sql = new StringBuilder("select c.cartid, u.userid, u.useremail, u.username, u.useremail, u.usernumber, u.userstatus, ");
		sql.append("c.enddate, a.address, a.addresscompl, a.addressid, a.addressnumber, a.city, a.state, ");
		sql.append("a.address useraddress, a.addresscompl useraddresscompl, a.addressid useraddressid, a.addressnumber useraddressnumber, a.city usercity, a.state useraddressstate, ");
		sql.append("d.deliverydesc, d.deliveryid, d.deliverystatus, d.deliverytype, g.description_detail ");
		sql.append("from carts c ");
		sql.append("inner join users u on u.userid = c.userid ");
		sql.append("left join delivery d on d.deliveryid = c.deliveryid ");
		sql.append("left join generic g on g.detailid = d.deliverytype and g.id = 1 ");
		sql.append("left join address a on a.addressid = d.addressid ");
		sql.append("left join address ua on ua.addressid = u.addressid ");
		sql.append("where 1=1 ");
		
		filters.forEach(filter -> {
			sql.append(" and").append(filter.getFilter());
		});

		sql.append(String.format("limit %d,%d", offset == null ? 0 : offset,
				limit == null ? 30 : limit.compareTo(30) <= 0 ? limit : 30));

		System.out.println(sql.toString());
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			Order order = new Order();
			order.setId(rs.getInt("cartid"));
			User user = new User();
			user.setId(rs.getInt("userid"));
			user.setEmail(rs.getString("useremail"));
			user.setName(rs.getString("username"));
			user.setNumber(rs.getString("usernumber"));
			Address usrAddress = new Address();
			usrAddress.setAddressId(rs.getInt("useraddressid"));
			usrAddress.setAddress(rs.getString("useraddress"));
			usrAddress.setComplement(rs.getString("useraddresscompl"));
			usrAddress.setNumber(rs.getString("useraddressnumber"));
			usrAddress.setCity(rs.getString("usercity"));
			usrAddress.setState(rs.getString("useraddressstate"));
			user.setAddress(usrAddress);
			Delivery delivery = new Delivery();
			delivery.setDescription(rs.getString("deliverydesc"));
			delivery.setType(new DeliveryType(rs.getInt("deliverytype"), rs.getString("description_detail")));
			delivery.setId(rs.getInt("deliveryid"));
			Address address = new Address();
			address.setAddressId(rs.getInt("addressid"));
			address.setAddress(rs.getString("address"));
			address.setComplement(rs.getString("addresscompl"));
			address.setNumber(rs.getString("addressnumber"));
			address.setCity(rs.getString("city"));
			address.setState(rs.getString("state"));
			delivery.setAddress(address);
			
			String paySql = "select p.paymethoddescription, p.paymethodid, cp.amount, cp.cartpaystatus " + 
							"from cart_paymethods cp " + 
							"inner join paymethods p on p.paymethodid = cp.paymethodid " + 
							"where cp.cartId = ?";
			PreparedStatement stmtPay = con.prepareStatement(paySql);
			stmtPay.setInt(1, order.getId());
			ResultSet rsPay = stmtPay.executeQuery();
			while(rsPay.next()) {
				String className = rsPay.getString("paymethoddescription");
				@SuppressWarnings("rawtypes")
				Class clazz = Class.forName("br.com.cesarcastro.pulsemkt.model." + className + "PaymentMethod");
				PaymentMethod pm = (PaymentMethod) clazz.newInstance();
				pm.setPaymentId(rsPay.getInt("paymethodid"));
				pm.setPaymentDescription(className);
				pm.setConcluded(rsPay.getString("cartpaystatus").contentEquals("A"));
				pm.setValue(rsPay.getBigDecimal("amount"));
				order.getPaymentList().add(pm);
			}
			
			String prodSql = "select p.productId, p.productBarcode, p.productDescription, p.productImg, cp.amount, cp.unitValue " + 
					"from cart_products cp " + 
					"inner join products p on p.productId = cp.productId " +
					" where cp.cartid = ?";
			PreparedStatement stmtProd = con.prepareStatement(prodSql);
			stmtProd.setInt(1, order.getId());
			ResultSet rsProd = stmtProd.executeQuery();
			while(rsProd.next()) {
				Product product = new Product();
				product.setId(rsProd.getInt("productid"));
				product.setDescription(rsProd.getString("productdescription"));
				product.setCodBar(rsProd.getString("productbarcode"));
				product.setImage(rsProd.getString("productimg"));
				product.setValue(rsProd.getBigDecimal("amount").multiply(rsProd.getBigDecimal("unitvalue")));
				order.getProducts().add(product);
			}
			order.update();
			rsProd.close();
			rsPay.close();
			orders.add(order);
		}

		rs.close();
		stmt.close();
		con.close();

	}

}
