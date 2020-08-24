package br.com.cesarcastro.pulsemkt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import br.com.cesarcastro.pulsemkt.enums.Status;
import br.com.cesarcastro.pulsemkt.model.Product;
import br.com.cesarcastro.pulsemkt.model.QueryFilter;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class ProductDao {

	private Connection con;

	public void getProducts(Collection<Product> products, Collection<QueryFilter> filters, Integer offset, Integer limit)
			throws SQLException, Exception {

		con = SysConfig.getConnection();

		StringBuilder sql = new StringBuilder(
				"select productId, productBarcode, productDescription, productValue, productImg from products where productStatus = ? ");

		filters.forEach(filter -> {
			sql.append(" and").append(filter.getFilter());
		});

		sql.append(String.format("limit %d,%d", offset == null ? 1 : offset,
				limit == null ? 30 : limit.compareTo(30) <= 0 ? limit : 30));

		PreparedStatement stmt = con.prepareStatement(sql.toString());
		stmt.setString(1, Status.ACTIVE.getValue());
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			products.add(new Product(rs.getInt("productId"), rs.getString("productBarcode"),
					rs.getString("productDescription"), rs.getBigDecimal("productValue"), rs.getString("productImg")));
		}
		rs.close();
		stmt.close();
		con.close();

	}

	public Product getProductById(Integer id) throws SQLException, Exception{
		
		con = SysConfig.getConnection();
		String sql = "select p.productId, p.productDescription, p.productImg, p.productValue, p.productBarcode from products p where p.productId = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1,  id);
		ResultSet rs = ps.executeQuery();
		Product p = new Product();
		if(rs.next()) {
			p.setId(id);
			p.setDescription(rs.getString("productdescription"));
			p.setCodBar(rs.getString("productbarcode"));
			p.setValue(rs.getBigDecimal("productvalue"));
			p.setImage(rs.getString("productimg"));
		}
		return p;
	}
}
