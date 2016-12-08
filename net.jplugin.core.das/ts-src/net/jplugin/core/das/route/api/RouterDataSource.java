package net.jplugin.core.das.route.api;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import net.jplugin.core.das.route.impl.conn.RouterConnection;

/**
 * ������һ�����������Դ������setAutoCommit������commit������roolback������close������Ч
 * @author Administrator
 *
 */
public class RouterDataSource implements DataSource{
	PrintWriter logWriter;
	RouterDataSourceConfig config = new RouterDataSourceConfig();
	
	public void config(Map<String,String> cfg){
		config.fromProperties(cfg);
	}
	
	public RouterDataSourceConfig getConfig() {
		return config;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return new RouterConnection(this);
	}
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return logWriter;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		this.logWriter = out;
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new RuntimeException("not support");
	}



	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		throw new RuntimeException("not support");
	}

}
