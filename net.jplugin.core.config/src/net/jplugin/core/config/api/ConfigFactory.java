package net.jplugin.core.config.api;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.impl.ConfigRepository;
/**
*
* @author: LiuHang
* @version ����ʱ�䣺2015-10-12 ����01:07:22
**/
public class ConfigFactory {

	private static IConfigProvidor localConfigProvidor;
	private static IConfigProvidor remoteConfigProvidor=null;

	public static String getStringConfig(String path,String def){
		String val = _getStringConfig(path);
		if (StringKit.isNull(val)) return def;
		else return val;
	}
	public static String getStringConfig(String path){
		return getStringConfig(path,null);
	}
	public static Long getLongConfig(String path,Long def){
		String val = _getStringConfig(path);
		if (StringKit.isNull(val)) return def;
		else return Long.parseLong(val);
	}
	
	public static Long getLongConfig(String path){
		return getLongConfig(path,null);
	}

	
	public static Integer getIntConfig(String path,Integer def){
		String val = _getStringConfig(path);
		if (StringKit.isNull(val)) return def;
		return Integer.parseInt(val);
	}

	public static Integer getIntConfig(String path){
		return getIntConfig(path,null);
	}

	/**
	 * <pre>
	 * ������������˸�key���򷵻ر���
	 * �������Զ�̳�ʼ�����ˣ��򷵻�Զ�̣����򷵻�null��
	 * ע�⣺���ڳ�ʼ��˳��ԭ�򣬷��ʸ÷�����ʱ�������Զ����û�г�ʼ���õ������
	 * </pre>
	 * @param path
	 * @return
	 */
	private static String _getStringConfig(String path){
		if (localConfigProvidor.containsConfig(path))
			return localConfigProvidor.getConfigValue(path);
		
		if (remoteConfigProvidor!=null) 
			return remoteConfigProvidor.getConfigValue(path);
		
		return null;
	}

	public static void _setLocalConfigProvidor(IConfigProvidor repo) {
		localConfigProvidor = repo;
	}
	
	public static void _setRemoteConfigProvidor(IConfigProvidor repo) {
		remoteConfigProvidor = repo;
	}
}
