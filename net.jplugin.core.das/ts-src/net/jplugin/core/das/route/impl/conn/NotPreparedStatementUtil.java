package net.jplugin.core.das.route.impl.conn;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.route.api.SqlHandleService;
import net.jplugin.core.das.route.api.TablesplitException;

public class NotPreparedStatementUtil {

	/**
	 * �����������ִ�д�sql���������
	 * @return
	 * @throws SQLException
	 */
	public static PreparedStatement genTargetNotPreparedStatement(RouterConnection conn,String sql) throws SQLException {
		if (sql==null) throw new TablesplitException("No sql found");
		SqlHandleResult shr = SqlHandleService.INSTANCE.handle(conn,sql);
		
		DataSource tds = DataSourceFactory.getDataSource(shr.getTargetDataSourceName());
		if (tds==null) 
			throw new TablesplitException("Can't find target datasource."+shr.getTargetDataSourceName());
		PreparedStatement stmt = tds.getConnection().prepareStatement("");//���ֵõ�preparedstatement������֪����ȷ��
		return stmt;
	}
}
