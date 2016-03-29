package net.jplugin.core.das.api.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import net.jplugin.core.ctx.api.TransactionHandler;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextListener;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.service.api.ServiceFactory;

public class TxManagedDataSource implements DataSource, TransactionHandler {
	private String DBCONN_IN_CTX;
	private static final String DBCONN_IN_CTX_PREFIX = "DBCONN_IN_CTX";
	DataSource inner;
	
	public TxManagedDataSource(String ds, DataSource in){
		this.inner = in;
		this.DBCONN_IN_CTX = DBCONN_IN_CTX_PREFIX +"#"+ds;
	}
	
	//������datasource��صĴ�������ֻʵ����getConnectionһ������
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return inner.getLogWriter();
	}

	/**
	 * ��ȡ���ӣ���������releaseʱ�ͷ�����
	 */
	public Connection getConnection() throws SQLException {
		//�������ctx���ҵ����򷵻�ctx�е�conn
		ThreadLocalContext ctx = ThreadLocalContextManager.instance
				.getContext();
		Connection conn = (Connection) ctx.getAttribute(DBCONN_IN_CTX);
		if (conn != null)
			return conn;
		
		//��ctx�л�ȡ�������򴴽�һ����������ctx�У�������ReleaseListenerȷ���ͷ�
		conn = inner.getConnection();
		ctx.setAttribute(DBCONN_IN_CTX, conn);
		ctx.addContextListener(new ThreadLocalContextListener() {
			@Override
			public void released(ThreadLocalContext rc) {
				Connection temp = (Connection) ctx.getAttribute(DBCONN_IN_CTX);
				if (temp != null) {
					try {
						if (!temp.isClosed())
							temp.close();
					} catch (SQLException e) {
						net.jplugin.core.log.api.Logger logger = ServiceFactory
								.getService(ILogService.class).getLogger(
										this.getClass().getName());
						logger.error(e);
					}
				}
			}
		});
		//�����´��������ӣ����������ӵ���������
		TransactionManager txm = ServiceFactory.getService(TransactionManager.class);
		if (txm.getStatus() != TransactionManager.Status.NOTX){
			conn.setAutoCommit(false);
		}else{
			conn.setAutoCommit(true);
		}
		return conn;
	}
	


	public Connection getConnection(String username, String password)
			throws SQLException {
		throw new RuntimeException("not support");
	}

	public int getLoginTimeout() throws SQLException {
		return inner.getLoginTimeout();
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return inner.getParentLogger();
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return inner.isWrapperFor(iface);
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		inner.setLogWriter(out);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		inner.setLoginTimeout(seconds);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return inner.unwrap(iface);
	}
	

	//����ʵ��tx��ط�����Ŀǰ��ʵ�ַ����ǣ����Ӿ��������ﵱ�С�
	//���beginǰ�������ر���;���ύʱ�ͷ����ӡ�
	
	/**
	 * ��ȡctx�е�conn���ص����Ա��Ժ��ȡ����������������С�
	 */
	@Override
	public void doBegin() {
		//����������ӣ����������������»�ȡ�����ӣ����ڻ�ȡʱ����
		ThreadLocalContext ctx = ThreadLocalContextManager.instance
				.getContext();
		Connection conn = (Connection) ctx.getAttribute(DBCONN_IN_CTX);
		if (conn!=null){
			try{conn.close();}catch(Exception e){}
			ctx.setAttribute(DBCONN_IN_CTX,null);
		}
	}
	
	/**
	 * ���ctx�д��ڣ���rollback
	 */
	@Override
	public void doRollback() {
		ThreadLocalContext ctx = ThreadLocalContextManager.instance
				.getContext();
		Connection conn = (Connection) ctx.getAttribute(DBCONN_IN_CTX);
		if (conn!=null){
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally{
				try{conn.close();}catch(Exception e){}
				ctx.setAttribute(DBCONN_IN_CTX,null);
			}
		}
	}

	/**
	 * ���ctx�д��ڣ���commit
	 */
	@Override
	public void doCommit() {
		ThreadLocalContext ctx = ThreadLocalContextManager.instance
				.getContext();
		Connection conn = (Connection) ctx.getAttribute(DBCONN_IN_CTX);
		if (conn!=null){
			try {
				conn.commit();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally{
				try{conn.close();}catch(Exception e){}
				ctx.setAttribute(DBCONN_IN_CTX,null);
			}
		}
	}
}
