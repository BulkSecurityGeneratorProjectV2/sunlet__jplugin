package net.jplugin.core.das.route.impl.conn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteResult {
	private Statement statement = null;
	boolean closeCalled = true; //��ʾ�ϴ����ù�statement�Ժ���û�е��ù�clear/close��������ʼtrue
	
	public ResultSet getResult() throws SQLException{
		if (statement!=null) return statement.getResultSet();
		else return null;
	}
	public int getUpdateCount() throws SQLException{
		if (statement!=null) return statement.getUpdateCount();
		return -1;
	}
	
	public void clear() {
		if (statement!=null){ //��������null��Ҳֻ���ǵ��ù�clear����һ����ɵ�
			if (!closeCalled){
				try{
					statement.close();
				}catch(Throwable e){
				}
			}
			statement = null;
			closeCalled = true;
		}
	}
	
	public void set(Statement stmt) throws SQLException{
		clear();
		statement = stmt;
		closeCalled = false;
	}
	public boolean getMoreResults() throws SQLException {
		if (this.statement!=null) 
			return statement.getMoreResults();
		else return false;
	}
}