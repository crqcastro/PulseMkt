package br.com.cesarcastro.pulsemkt.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import br.com.cesarcastro.pulsemkt.dao.UserDao;
import br.com.cesarcastro.pulsemkt.enums.Comparator;
import br.com.cesarcastro.pulsemkt.exception.ServiceBusinessException;
import br.com.cesarcastro.pulsemkt.model.QueryFilter;
import br.com.cesarcastro.pulsemkt.model.User;
import br.com.cesarcastro.pulsemkt.util.SysConfig;

public class UserService {

	private UserDao dao = new UserDao();

	public void create(User user) throws ServiceBusinessException {
		try {
			dao.create(user);
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

	public void getUserById(User user) throws ServiceBusinessException {
		try {
			dao.getUserById(user);
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

	public void getUsers(Collection<User> users, String emails, String name, Integer limit, Integer offset) throws ServiceBusinessException {
		try {
			Collection<QueryFilter> filters = new ArrayList<QueryFilter>();
			
			if (!StringUtils.isEmpty(name))
				filters.add(new QueryFilter("users.username", Comparator.LIKE, name));
			if (!StringUtils.isEmpty(emails))
				filters.add(new QueryFilter("users.username", Comparator.IN, emails));
			
			dao.getUsers(users, filters, limit, offset);
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

	public void update(User user) throws ServiceBusinessException {
		try {
			dao.update(user);
		}catch (SQLException e) {
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
