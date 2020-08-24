
package br.com.cesarcastro.pulsemkt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.NoSuchElementException;

import br.com.cesarcastro.pulsemkt.enums.Status;
import br.com.cesarcastro.pulsemkt.enums.UserRole;
import br.com.cesarcastro.pulsemkt.model.Address;
import br.com.cesarcastro.pulsemkt.model.QueryFilter;
import br.com.cesarcastro.pulsemkt.model.User;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class UserDao {

	private Connection con;

	public void create(User user) throws SQLException, Exception {
		con = SysConfig.getConnection();

		String addSql = "INSERT INTO pulsemkt.address (address, addressnumber, addresscompl, city, state) VALUES(?, ?, ?, ?, ?)";
		
		PreparedStatement stmt = con.prepareStatement(addSql, Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, user.getAddress().getAddress());
		stmt.setString(2,user.getAddress().getNumber());
		stmt.setString(3, user.getAddress().getComplement());
		stmt.setString(4, user.getAddress().getCity());
		stmt.setString(5, user.getAddress().getState());
		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		
		if(rs != null && rs.next()) {
			user.getAddress().setAddressId(rs.getInt(1));
		}
		rs.close();
		stmt.close();
		
		
		String sql = "insert into users (username, useremail, userpwd, usernumber, addressid) values (?,?,?,?,?)";
		
		stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		stmt.setString(1, user.getName());
		stmt.setString(2, user.getEmail());
		stmt.setString(3, user.getPassword());
		stmt.setString(4, user.getNumber());
		stmt.setInt(5, user.getAddress().getAddressId());
		stmt.executeUpdate();
		rs = stmt.getGeneratedKeys();

		if (rs != null && rs.next()) {
			user.setId(rs.getInt(1));
		}

		rs.close();
		stmt.close();
		con.close();
	}

	public void getUserById(User user) throws SQLException, Exception {

		con = SysConfig.getConnection();

		String sql = "select u.userid, u.username, u.userrole, u.useremail, u.usernumber, a.addressid, a.address, a.addressnumber, a.addresscompl, a.city, a.state "
					+ "from users u "
					+ "inner join address a on a.addressid = u.addressid "
					+ "where userStatus = ? and userid = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, Status.ACTIVE.getValue());
		stmt.setInt(2, user.getId());
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			user.setUserRole(UserRole.valueOf(rs.getString("userrole")));
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
		}

		con.close();
	}

	public void getUsers(Collection<User> users, Collection<QueryFilter> filters, Integer limit, Integer offset)
			throws SQLException, Exception {

		con = SysConfig.getConnection();
		StringBuilder sql = new StringBuilder("select u.userid, u.userrole, u.username, u.useremail, u.usernumber, a.addressid, a.address, a.addressnumber, a.addresscompl, a.city, a.state " + 
											  "from users u " + 
											  "inner join address a on a.addressid = u.addressid " + 
											  "where userStatus = ").append(Status.ACTIVE);


		filters.forEach(filter -> {
			sql.append(" and").append(filter.getFilter());
		});

		sql.append(String.format("limit %d,%d", offset == null ? 0 : offset,
				limit == null ? 30 : limit.compareTo(30) <= 0 ? limit : 30));

		PreparedStatement stmt = con.prepareStatement(sql.toString());
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			User user = new User();
			user.setUserRole(UserRole.valueOf(rs.getString("userrole")));
			user.setId(rs.getInt("userid"));
			user.setName(rs.getString("username"));
			user.setEmail(rs.getString("useremail"));
			user.setNumber(rs.getString("usernumber"));
			Address address = new Address();
			address.setAddressId(rs.getInt("addressid"));
			address.setAddress(rs.getString("address"));
			address.setNumber(rs.getString("addressnumber"));
			address.setComplement(rs.getString("addresscompl"));
			address.setCity(rs.getString("city"));
			address.setState(rs.getString("state"));
			user.setAddress(address);

			users.add(user);
		}
		con.close();
	}

	public boolean efetuaLogin(User user) throws SQLException, Exception {
		con = SysConfig.getConnection();
		boolean retorno = false;
		String sql = "select userid, username, useremail, userrole from users where userStatus = ? and  useremail = ? and userpwd = sha1(?)";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, Status.ACTIVE.getValue());
		stmt.setString(2, user.getEmail());
		stmt.setString(3, user.getPassword());
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			user.setUserRole(UserRole.valueOf(rs.getString("userrole")));
			user.setId(rs.getInt("userid"));
			user.setName(rs.getString("username"));
			retorno = true;
		}
		con.close();

		return retorno;
	}

	public void update(User user) throws SQLException, Exception {
		con = SysConfig.getConnection();
		String sql = "update users set username = ?, useremail = ? where userStatus = ? and userid = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, user.getName());
		stmt.setString(2, user.getEmail());
		stmt.setString(3, Status.ACTIVE.getValue());
		stmt.setInt(4, user.getId());
		int upd = stmt.executeUpdate();
		con.close();
		if (upd == 0)
			throw new NoSuchElementException("Element not found");

	}
}
