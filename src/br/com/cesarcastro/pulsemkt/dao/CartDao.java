package br.com.cesarcastro.pulsemkt.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import br.com.cesarcastro.pulsemkt.enums.Status;
import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.Address;
import br.com.cesarcastro.pulsemkt.model.Cart;
import br.com.cesarcastro.pulsemkt.model.PaymentMethod;
import br.com.cesarcastro.pulsemkt.model.Product;
import br.com.cesarcastro.pulsemkt.model.User;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class CartDao {

	Connection con;

	public void create(Cart cart) throws SQLException, Exception {

		con = SysConfig.getConnection();

		String sql = "insert into carts (userId) values (?)";

		PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		stmt.setInt(1, cart.getUserId());
		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();

		if (rs != null && rs.next()) {
			cart.setId(rs.getInt(1));
		}

		rs.close();
		stmt.close();
		con.close();
	}

	public void insertProduct(Integer id, Integer productid, BigDecimal quantity) throws SQLException, ServiceBusinessException, Exception {

		if (!isActive(getCartById(id)))
			throw new ServiceBusinessException("Resource is inactive");

		con = SysConfig.getConnection();

		Product product = new ProductDao().getProductById(productid);
		
		String units = "select amount from cart_products where cartid = ? and productid = ?";
		PreparedStatement pt = con.prepareStatement(units);
		pt.setInt(1, id);
		pt.setInt(2, productid);
		
		ResultSet rs = pt.executeQuery();
		BigDecimal qtyUnits = BigDecimal.ZERO;
		String sql = null;
		if(rs.next()) {
			qtyUnits = rs.getBigDecimal("amount");
			if(qtyUnits.compareTo(BigDecimal.ZERO)>0)
				sql = "Update cart_products set amount = ? where cartid = ? and productid = ?";
		}else {
		 sql = "insert into cart_products (cartid, productid, amount, unitValue) values (?, ?, ?, ?)";
		}

		PreparedStatement stmt = con.prepareStatement(sql);

		if(qtyUnits.compareTo(BigDecimal.ZERO)==0) {
			stmt.setInt(1, id);
			stmt.setInt(2, productid);
			stmt.setBigDecimal(3, quantity);
			stmt.setBigDecimal(4, product.getValue());
		}else {
			stmt.setBigDecimal(1, qtyUnits.add(quantity));
			stmt.setInt(2, id);
			stmt.setInt(3, productid);
		}
		
		int qty = stmt.executeUpdate();

		stmt.close();
		con.close();

		if (qty < 1) {
			throw new ServiceBusinessException("No cart or product was found for this data");
		}
	}

	public void deleteProduct(Integer cartid, Integer productid, BigDecimal quantity)
			throws SQLException, ServiceBusinessException, Exception {

		if (!isActive(getCartById(cartid)))
			throw new ServiceBusinessException("Resource is inactive");

		con = SysConfig.getConnection();

		String units = "select amount from cart_products where cartid = ? and productid = ?";
		PreparedStatement pt = con.prepareStatement(units);
		pt.setInt(1, cartid);
		pt.setInt(2, productid);
		
		ResultSet rs = pt.executeQuery();
		BigDecimal qtyUnits = BigDecimal.ZERO;
		String sqlUpd = null;
		String sqlDel = null;
		if(rs.next()) {
			qtyUnits = rs.getBigDecimal("amount");
			if(qtyUnits.compareTo(BigDecimal.ZERO)>0)
				sqlUpd = "Update cart_products set amount = ? where cartid = ? and productid = ?";
		}else {
			sqlDel = "delete from cart_products where cartid = ? and productid = ?";
		}
		
		PreparedStatement stmt;
		if(qtyUnits.compareTo(BigDecimal.ZERO)==0) {
			stmt = con.prepareStatement(sqlDel);
			stmt.setInt(1, cartid);
			stmt.setInt(2, productid);
		}else if(qtyUnits.compareTo(quantity)<=0) {
			stmt = con.prepareStatement(sqlDel);
			stmt.setInt(1, cartid);
			stmt.setInt(2, productid);
		}
		else {
			stmt = con.prepareStatement(sqlUpd);
			stmt.setBigDecimal(1, qtyUnits.subtract(quantity));
			stmt.setInt(2, cartid);
			stmt.setInt(3, productid);
		}
		
		int qty = stmt.executeUpdate();

		stmt.close();
		con.close();

		if (qty < 1) {
			throw new ServiceBusinessException("No cart or product was found for this data");
		}

	}

	public Cart getCartById(Integer cartid) throws SQLException, ServiceBusinessException, Exception {

		Cart cart = null;
		Collection<Product> products = new ArrayList<Product>();

		con = SysConfig.getConnection();

		String sql = "select c.cartstatus, c.cartid, u.userid, u.username, u.useremail, u.usernumber, a.addressid, a.address, a.addressnumber, a.addresscompl, a.city, a.state "
				+ "from carts c "
				+ "inner join users u on u.userid = c.userid and u.userstatus = ? "
				+ "inner join address a on a.addressid = u.addressid " + "where c.cartid = ?";

		PreparedStatement stmt = con.prepareStatement(sql);

		stmt.setString(1, Status.ACTIVE.getValue());
		stmt.setInt(2, cartid);

		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			
			String itemsQry = "select p.productid, p.productbarcode, p.productdescription, cp.unitvalue productvalue, p.productimg,"
					+ " cp.amount"
					+ " from products p inner join cart_products cp on cp.productid = p.productid where cp.cartid = ?";

			PreparedStatement itemsStmt = con.prepareStatement(itemsQry);
			itemsStmt.setInt(1, cartid);

			ResultSet rsItems = itemsStmt.executeQuery();

			while (rsItems.next()) {
				products.add(new Product(rsItems.getInt("productid"), rsItems.getString("productbarcode"),
						rsItems.getString("productdescription"), rsItems.getBigDecimal("productValue"),
						rsItems.getString("productimg"), rsItems.getBigDecimal("amount")));
			}

			User user = new User();
			user.setId(rs.getInt("userid"));
			user.setEmail(rs.getString("useremail"));
			user.setName(rs.getString("username"));
			user.setNumber(rs.getString("usernumber"));
			Address address = new Address();
			address.setAddressId(rs.getInt("addressid"));
			address.setAddress(rs.getString("address"));
			address.setNumber(rs.getString("addressnumber"));
			address.setComplement(rs.getString("addresscompl"));
			address.setCity(rs.getString("city"));
			address.setState(rs.getString("state"));
			user.setAddress(address);

			cart = new Cart(rs.getInt("cartid"), user, products);
			cart.setStatus(Status.getStatusByDesc(rs.getString("cartstatus")));
			String pay = "select p.paymethodid, p.paymethoddescription, m.amount from cart_paymethods m "
					+ "inner join paymethods p on p.paymethodid = m.paymethodid " + "where m.cartid = ?";

			PreparedStatement stmtPay = con.prepareStatement(pay);
			stmtPay.setInt(1, cartid);

			ResultSet payRs = stmtPay.executeQuery();

			while (payRs.next()) {
				String className = payRs.getString("paymethoddescription");
				@SuppressWarnings("rawtypes")
				Class clazz = Class.forName("br.com.cesarcastro.pulsemkt.model." + className + "PaymentMethod");
				PaymentMethod pm = (PaymentMethod) clazz.newInstance();
				pm.setPaymentId(payRs.getInt("paymethodid"));
				pm.setPaymentDescription(className);
				pm.setValue(payRs.getBigDecimal("amount"));

				cart.getPaymentList().add(pm);
			}

			rsItems.close();
			itemsStmt.close();

			payRs.close();
			stmtPay.close();

		}
		rs.close();

		stmt.close();
		con.close();
		return cart;
	}

	public void addPaymentMethod(Integer cartId, Integer paymentId, BigDecimal value)
			throws SQLException, ServiceBusinessException, Exception {

		if (!isActive(getCartById(cartId)))
			throw new ServiceBusinessException("Resource is inactive");

		con = SysConfig.getConnection();
		String sql = "insert into cart_paymethods (cartid, paymethodid, amount) values (?,?,?)";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, cartId);
		stmt.setInt(2, paymentId);
		stmt.setBigDecimal(3, value);
		int qty = stmt.executeUpdate();

		stmt.close();
		con.close();

		if (qty < 1) {
			throw new ServiceBusinessException("No cart or product was found for this data");
		}

	}

	public void remPaymentMethod(Integer cartId, Integer paymentId)
			throws SQLException, ServiceBusinessException, Exception {

		if (isActive(getCartById(cartId)))
			throw new ServiceBusinessException("Resource is inactive");

		con = SysConfig.getConnection();
		String sql = "delete from cart_paymethods where cartid = ? and paymethodid = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, cartId);
		stmt.setInt(2, paymentId);
		int qty = stmt.executeUpdate();

		stmt.close();
		con.close();

		if (qty < 1) {
			throw new ServiceBusinessException("No cart or product was found for this data");
		}

	}

	public void updatePaymentMethod(Integer cartId, Integer paymentId, BigDecimal value)
			throws SQLException, ServiceBusinessException, Exception {

		if (isActive(getCartById(cartId)))
			throw new ServiceBusinessException("Resource is inactive");

		con = SysConfig.getConnection();
		String sql = "update cart_paymethods set amount = ? where cartid = ? and paymethodid = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setBigDecimal(1, value);
		stmt.setInt(2, cartId);
		stmt.setInt(3, paymentId);
		int qty = stmt.executeUpdate();

		stmt.close();
		con.close();

		if (qty < 1) {
			throw new ServiceBusinessException("No cart or product was found for this data");
		}

	}

	public void finalize(Cart cart) throws ServiceBusinessException, SQLException, Exception {

		if (!isActive(cart))
			throw new ServiceBusinessException("Resource is inactive");

		con = SysConfig.getConnection();
		String sql = "update carts set cartstatus = ?, enddate = now() where cartid = ?";
		PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, cart.getStatus().getValue());
		stmt.setInt(2, cart.getId());
		int qty = stmt.executeUpdate();
		cart.setOrderid(cart.getId());

		stmt.close();

		cart.getPaymentList().stream().filter(payment -> payment.isConcluded()).forEach(payment -> {
			// String ins = "insert into
		});

		con.close();

		if (qty < 1) {
			throw new ServiceBusinessException("No cart or product was found for this data");
		}

	}

	public void alterDelivery(Integer cartId, Integer deliveryId)
			throws ServiceBusinessException, SQLException, Exception {

		if (isActive(getCartById(cartId)))
			throw new ServiceBusinessException("Resource is inactive");
		
		con = SysConfig.getConnection();
		String altSql = "update carts set deliveryid = ? where cartid =? ";
		PreparedStatement stmt = con.prepareStatement(altSql);
		stmt.setInt(1, deliveryId);
		stmt.setInt(2, cartId);

		int qty = stmt.executeUpdate();

		stmt.close();
		con.close();

		if (qty < 1)
			throw new ServiceBusinessException("No cart was found for this data");
	}

	public boolean isActive(Cart cart) throws ServiceBusinessException, SQLException, Exception {
		con = SysConfig.getConnection();
		boolean isActive = false;
		String qry = "select 0 from carts c where c.cartid = ? and c.cartStatus = ?";
		PreparedStatement st = con.prepareStatement(qry);
		st.setInt(1, cart.getId());
		st.setString(2, Status.ACTIVE.getValue());
		ResultSet rs = st.executeQuery();
		if (rs.next())
			isActive = true;

		rs.close();
		st.close();
		con.close();
		return isActive;
	}
}
