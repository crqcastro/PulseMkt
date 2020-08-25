package br.com.cesarcastro.pulsemkt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import br.com.cesarcastro.pulsemkt.enums.Status;
import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.Address;
import br.com.cesarcastro.pulsemkt.model.Delivery;
import br.com.cesarcastro.pulsemkt.model.DeliveryType;
import br.com.cesarcastro.pulsemkt.model.QueryFilter;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class DeliveryDao {

	Connection con;

	public void create(Delivery delivery) throws ServiceBusinessException, SQLException, Exception {

		con = SysConfig.getConnection();

		String insSql = "insert into delivery (deliverytype, deliverydesc, addressid) " + "values (?, ?, ?)";

		PreparedStatement stmt = con.prepareStatement(insSql, Statement.RETURN_GENERATED_KEYS);
		stmt.setInt(1, delivery.getType().getId());
		stmt.setString(2, delivery.getDescription());
		stmt.setInt(3, delivery.getAddress().getAddressId());

		stmt.executeUpdate();

		ResultSet key = stmt.getGeneratedKeys();

		if (key != null && key.next())
			delivery.setId(key.getInt(1));

		key.close();
		stmt.close();
		con.close();

	}

	public void getDeliveryList(Collection<Delivery> deliveries, Collection<QueryFilter> filters, Integer offset,
			Integer limit) throws ServiceBusinessException, SQLException, Exception {

		con = SysConfig.getConnection();

		StringBuilder sql = new StringBuilder(
				"SELECT d.deliveryid, d.deliverytype, d.deliverydesc, d.addressid, d.deliverystatus, "
						+ "a.address, a.addressnumber, a.addresscompl, a.city, a.state, g.description_detail "
						+ "FROM delivery d "
						+ "inner join generic g on g.detailid = d.deliverytype and g.id = 1 "
						+ "left join address a on a.addressid = d.addressid "
						+ "where deliverystatus = ? ");

		filters.forEach(filter -> {
			sql.append(" and").append(filter.getFilter());
		});

		sql.append(String.format("limit %d,%d", offset == null ? 1 : offset,
				limit == null ? 30 : limit.compareTo(30) <= 0 ? limit : 30));

		PreparedStatement stmt = con.prepareStatement(sql.toString());
		stmt.setString(1,  Status.ACTIVE.getValue());
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			Delivery delivery = new Delivery();
			delivery.setId(rs.getInt("deliveryid"));
			delivery.setType(new DeliveryType(rs.getInt("deliverytype"), rs.getString("description_detail")));
			delivery.setDescription(rs.getString("deliverydesc"));
			Integer addressId = rs.getInt("addressid");
			if (addressId != null) {
				Address address = new Address();
				address.setAddressId(addressId);
				address.setAddress(rs.getString("address"));
				address.setNumber(rs.getString("addressnumber"));
				address.setComplement(rs.getString("addresscompl"));
				address.setCity(rs.getString("city"));
				address.setState(rs.getString("state"));
				delivery.setAddress(address);
			}
			deliveries.add(delivery);
		}
		con.close();

	}

	public void getDeliveryTypeList(Collection<DeliveryType> types) 
		throws ServiceBusinessException, SQLException, Exception {

			con = SysConfig.getConnection();

		StringBuilder sql = new StringBuilder(
				"SELECT g.deliverytype, g.description_detail "
						+ "from generic g where g.id = 1 ");
		
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		ResultSet rs = stmt.executeQuery();

		while(rs.next()){
			types.add(new DeliveryType(rs.getInt("typeId"), rs.getString("typedescription")));
		}
		
		rs.close();
		stmt.close();
		con.close();
	}
}
