package net.jplugin.core.das.api.impl;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.route.api.RouterDataSource;

public class ConfigedDataSource {
	/**
	 *  <bean id="dataSource" destroy-method="close" class="org.apache.commons.dbcp.BasicDataSource">
      <property name="driverClassName" value="${driverClassName}"/>
      <property name="url" value="${url}"/>
      <property name="username"><value>${dbuser}</value></property>
      <property name="password"><value>${dbpassword}</value></property>
      <!-- ��󼤻�����������ʾͬʱ����ж��ٸ����ݿ����� -->
      <property name="maxActive"><value>${maxActive}</value></property> 
      <!-- ���Ŀ�������������ʾ��ʹû�����ݿ�����ʱ��Ȼ���Ա��ֶ��ٿ��е����ӣ��������������ʱ���ڴ���״̬ -->
      <property name="maxIdle"><value>${maxIdle}</value></property>
      <!-- �����ȴ���������ȡֵ-1����ʾ���޵ȴ���ֱ����ʱΪֹ��ȡֵ9000����ʾ9���ʱ -->
      <property name="maxWait"><value>${maxWait}</value></property>
      <!-- �Ƿ�����removeAbandonedTimeout��û��ʹ�õĻ����,�����û�зŻ����ӳ�.��Ĭ����false�� -->
      <property name="removeAbandoned"><value>true</value></property>
      <!-- �趨�����ڶ������ڱ���Ϊ�Ƿ��������ӣ����ɽ��лָ����� ,���δ��close�Ļ����-->
      <property name="removeAbandonedTimeout"><value>600</value></property>
      <!-- �Ƿ�������յ���־��������ϸ��ӡ���쳣�Ӷ������������﷢����й© -->
      <property name="logAbandoned"><value>false</value></property>
    </bean>
	 * @param groupName
	 * @return
	 */
	public static DataSource getDataSource(String group){
		
		Map<String, String> map = ConfigFactory.getStringConfigInGroup(group);
		String routeFlag = map.get("route-datasource-flag");
		if (routeFlag!=null) routeFlag.trim();
		
		if ("true".equalsIgnoreCase(routeFlag)){
			RouterDataSource ds = new RouterDataSource();
			ds.config(map);
			return ds;
		}else{
			DataSource ds = createJdbcDataSource(group, map);
			return ds;
		}
	}

	private static DataSource createJdbcDataSource(String group, Map<String, String> map) {
		org.apache.commons.dbcp.BasicDataSource ds = new BasicDataSource();
		//Ϊ�˼�����ǰ�������ļ���֧��dbuser��dbpassword��������
		if (map.containsKey("dbuser")){
			map.put("username", map.get("dbuser"));
			map.remove("dbuser");
		}
		if (map.containsKey("dbpassword")){
			map.put("password", map.get("dbpassword"));
			map.remove("dbpassword");
		}
		//����Ϊ�˸��õ�Ǩ����ǰ�������ļ���ֻ�޸��ļ�������
		
		map.remove(DataSourceFactory.IS_TX_MANAGED);
		
		if (map.isEmpty()){
			throw new RuntimeException("Can't find config for database:"+group);
		}
		for (String k:map.keySet()){
			ReflactKit.setPropertyFromString(ds, k,map.get(k));
		}
		return ds;
	}
}
