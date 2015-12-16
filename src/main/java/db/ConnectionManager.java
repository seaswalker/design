package db;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 数据库连接管理
 * @author skywalker
 *
 */
public class ConnectionManager {

	private static final ComboPooledDataSource dataSource;
	private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
	
	static {
		Properties properties = new Properties();
		try {
			properties.load(ConnectionManager.class.getResourceAsStream("/jdbc.properties"));
		} catch (IOException e) {
			logger.error("load jdbc.properties failed.");
			System.exit(1);
		}
		dataSource = new ComboPooledDataSource();
		dataSource.setUser(properties.getProperty("jdbc.username"));
		dataSource.setPassword(properties.getProperty("jdbc.password"));
		try {
			dataSource.setDriverClass(properties.getProperty("jdbc.driverClassName"));
		} catch (PropertyVetoException e) {
			logger.error("driver class加载失败.");
			System.exit(1);
		}
		dataSource.setJdbcUrl(properties.getProperty("jdbc.url"));
		dataSource.setMaxPoolSize(Integer.parseInt(properties.getProperty("c3p0.maxPoolSize")));
		dataSource.setMinPoolSize(Integer.parseInt(properties.getProperty("c3p0.minPoolSize")));
		dataSource.setInitialPoolSize(Integer.parseInt(properties.getProperty("c3p0.initialPoolSize")));
		logger.debug("数据库连接池初始化完成");
	}
	
	public static Connection getConnection() {
		Connection con = null;
		try {
			con = dataSource.getConnection();
		} catch (SQLException e) {
			logger.warn("建立数据库连接失败");
		}
		return con;
	}
	
}
