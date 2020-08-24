package br.com.cesarcastro.pulsemkt.util;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.dbcp2.BasicDataSource;

import com.mysql.jdbc.StringUtils;

public class SysConfig {

	public static final BigDecimal MIN_VALUE_CHECKOUT = new BigDecimal(100.0);
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);
	public static final String TOKEN_LOGIN_PREFIX = "Basic ";

	private static final String PROPERTIES_FILE_NAME = "DBConfig.properties";
	private static final String DIR = "resources/";
	private static BasicDataSource dataSource;

	private static String URL;
	private static String DRIVER;
	private static String USER;
	private static String PASSWORD;
	private static String DB_MAX_POOL_SIZE;
	private static String DB_MIN_POOL_SIZE;
	@SuppressWarnings("unused")
	private static String DB_INITIAL_POOL_SIZE;
	@SuppressWarnings("unused")
	private static String DB_MAX_IDLE_TIME;
	private static String DB_MAX_STATEMENTS_PER_CONNECTION;

	public static Map<String, Integer> DB_EXCEPTIONS_2_HTTP_STATUS_CODES = new HashMap<String, Integer>();

	private static void carregar() throws Exception {
		try {
			Properties properties = new Properties();
			InputStream stream;

			ClassLoader classLoader = ClassLoader.getSystemClassLoader();
			stream = classLoader.getResourceAsStream(PROPERTIES_FILE_NAME);

			if (stream == null) {
				stream = SysConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
			}

			if (stream == null) {
				stream = SysConfig.class.getResourceAsStream(PROPERTIES_FILE_NAME);
			}

			if (stream == null) {
				stream = classLoader.getResourceAsStream(DIR + PROPERTIES_FILE_NAME);
			}

			properties.load(stream);

			URL = properties.getProperty("db.url");
			DRIVER = properties.getProperty("db.driver");
			USER = properties.getProperty("db.user");
			PASSWORD = properties.getProperty("db.password");
			DB_MAX_POOL_SIZE = properties.getProperty("db.maxPoolSize");
			DB_MIN_POOL_SIZE = properties.getProperty("db.minPoolSize");
			DB_INITIAL_POOL_SIZE = properties.getProperty("db.initialPoolSize");
			DB_MAX_IDLE_TIME = properties.getProperty("db.maxIdleTime");
			DB_MAX_STATEMENTS_PER_CONNECTION = properties.getProperty("db.maxStatementsPerConnection");

			DB_EXCEPTIONS_2_HTTP_STATUS_CODES.put(properties.getProperty("constraintViolationException"), 422);
			DB_EXCEPTIONS_2_HTTP_STATUS_CODES.put(properties.getProperty("syntaxErrorException"), 500);
			DB_EXCEPTIONS_2_HTTP_STATUS_CODES.put(properties.getProperty("handleNonActivePayments"), 204);
			DB_EXCEPTIONS_2_HTTP_STATUS_CODES.put(properties.getProperty("resourceInactive"), 404);
			stream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex);
		}
	}

	private static BasicDataSource getDataSource() throws Exception {

		if (StringUtils.isNullOrEmpty(URL)) {
			carregar();
		}

		if (dataSource == null) {
			BasicDataSource ds = new BasicDataSource();
			ds.setUrl(URL);
			ds.setDriverClassName(DRIVER);
			ds.setUsername(USER);
			ds.setPassword(PASSWORD);
			ds.setMinIdle(Integer.parseInt(DB_MIN_POOL_SIZE));
			ds.setMaxIdle(Integer.parseInt(DB_MAX_POOL_SIZE));
			ds.setMaxOpenPreparedStatements(Integer.parseInt(DB_MAX_STATEMENTS_PER_CONNECTION));

			dataSource = ds;
		}
		return dataSource;
	}

	public static Connection getConnection() throws Exception {
		return getDataSource().getConnection();
	}

	static {
		System.out.println("Loading configurations...");
		try {
			carregar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
