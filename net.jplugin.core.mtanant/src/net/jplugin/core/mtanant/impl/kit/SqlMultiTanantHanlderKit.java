package net.jplugin.core.mtanant.impl.kit;

import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class SqlMultiTanantHanlderKit {
	/**
	 * <pre>
	 * #���⻧�����б�
	 * #��ʾ�Ƿ����ö��⻧ģʽ��Ĭ��FALSE
	 * mtanant.enable=FALSE|TRUE
	 * 
	 * #ָ�����ֶ��⻧�ֶε�����.���mtanant.enable=TRUE,�������������
	 * mtanant.field=field1
	 * 
	 * #���ö��⻧������Դ��Ĭ��ALL����ѡ���á�
	 * mtanant.datasource=ALL|ds1,ds2,ds3
	 * 
	 * #ָ����������Դ�в�֧�ֶ��⻧�ı����������ѡ����
	 * mtanant.datasource.ds1.exclude=table1,table2
	 * mtanant.datasource.ds2.exclude=table1,table2
	 * 
	 * </pre>
	 * @param dataSourceName
	 * @param sql
	 * @return
	 */

	public static String handle(String dataSourceName, String sql) {
		return sql+"/*aaaaaaa*/";
	}

}
