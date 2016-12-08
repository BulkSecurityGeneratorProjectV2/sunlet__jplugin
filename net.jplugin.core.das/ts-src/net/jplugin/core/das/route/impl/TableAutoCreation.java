package net.jplugin.core.das.route.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.route.api.ITsAlgorithm.Result;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.das.route.api.TablesplitException;

public class TableAutoCreation {

	static ConcurrentHashMap<String, Integer> tableMapping=new ConcurrentHashMap<>();
	
	public static void tryCreate(TableConfig tc, Result result, String tbBaseName) {
		String key = result.getDataSource()+"#"+result.getTableName();
		//����û����ͬ�����ƣ���Ϊcreatetable��ʱ����ܱȽϳ���
		String sql = tc.getCreationSql();

		//��sql����û���жϹ��ñ�Ŵ���
		if (StringKit.isNotNull(sql) && !tableMapping.containsKey(key)){
			sql = StringKit.repaceFirstIgnoreCase(sql, tbBaseName, result.getTableName());
			tryCreateTable(result.getDataSource(),result.getTableName(),sql);
			//��������ظ�������
			tableMapping.put(key, 1);
		}
	}
	
	private static void tryCreateTable(String dataSource, String tableName, String sql) {
		DataSource ds = DataSourceFactory.getDataSource(dataSource);
		try{
			Connection conn = ds.getConnection();
			try{
				//select �ɹ�ִ�в��ٴ���
				SQLTemplate.executeSelect(conn, "select * from "+tableName +" where 1=2", new IResultDisposer() {
					@Override
					public void readRow(ResultSet rs) throws SQLException {
					}
				}, null);
			}catch(Exception e1){
				//selectʧ�ܣ��Ŵ���
				SQLTemplate.executeCreateSql(conn , sql);
			}
		}catch(Exception e){
			throw new TablesplitException(e);
		}
	}
}
