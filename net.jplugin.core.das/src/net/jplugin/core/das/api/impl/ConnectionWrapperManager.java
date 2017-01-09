package net.jplugin.core.das.api.impl;

import java.sql.Connection;

import net.jplugin.core.das.api.IConnectionWrapperService;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class ConnectionWrapperManager {

	static IConnectionWrapperService[] arr;
	public static void init(){
		arr = PluginEnvirement.getInstance().getExtensionObjects(net.jplugin.core.das.Plugin.EP_CONN_WRAPPER,IConnectionWrapperService.class);
	}

	public static Connection getConnection(String dataSourceName, Connection connection) {
		//����ֱ��ʹ���������connection������
		if (arr!=null && arr.length>0){
			//��ע��ķ�װ��������
			for (IConnectionWrapperService dsw:arr){
				connection = dsw.wrapper(dataSourceName,connection);
			}
			return connection;
		}else
			return connection;
	}

}
