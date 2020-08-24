package br.com.cesarcastro.pulsemkt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.cesarcastro.pulsemkt.enums.Status;
import br.com.cesarcastro.pulsemkt.enums.UserRole;
import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.User;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class LoginDao {

	private Connection con;

	public void login(User user) throws ServiceBusinessException, SQLException, Exception {
		con = SysConfig.getConnection();

		String sql = "select userid, userrole, username from users where useremail = ? and userpwd = ? and userstatus = ?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, user.getEmail());
		stmt.setString(2, user.getPassword());
		stmt.setString(3, Status.ACTIVE.getValue());
		
		ResultSet rs = stmt.executeQuery();

		if(rs.next()) {
			user.setId(rs.getInt("userid"));
			user.setName(rs.getString("username"));
			user.setUserRole(UserRole.valueOf(rs.getString("userrole")));
		}else {
			throw new ServiceBusinessException("User not found");
		}
		con.close();

	}

}
