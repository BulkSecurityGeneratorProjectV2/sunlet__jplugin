package net.jplugin.core.mtenant.impl.kit;

import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class SqlMultiTenantHanlderKit {
	/**
	 * <pre>
	 * #���⻧�����б�
	 * #��ʾ�Ƿ����ö��⻧ģʽ��Ĭ��FALSE
	 * mtenant.enable=FALSE|TRUE
	 * 
	 * #ָ�����ֶ��⻧�ֶε�����.���mtenant.enable=TRUE,�������������
	 * mtenant.field=field1
	 * 
	 * #���ö��⻧������Դ��Ĭ��ALL����ѡ���á�
	 * mtenant.datasource=ALL|ds1,ds2,ds3
	 * 
	 * #ָ����������Դ�в�֧�ֶ��⻧�ı����������ѡ����
	 * mtenant.datasource.ds1.exclude=table1,table2
	 * mtenant.datasource.ds2.exclude=table1,table2
	 * 
	 * #˵��һ��
	 * #����������Զ��⻧�����ע�ͣ�����Ը�SQL�Ĵ���\/* IGNORE-TANANT *\/
	 * </pre>
	 * @param dataSourceName
	 * @param sql
	 * @return
	 */

	public static String handle(String dataSourceName, String sql) {
		ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		return sql;
	}

}
