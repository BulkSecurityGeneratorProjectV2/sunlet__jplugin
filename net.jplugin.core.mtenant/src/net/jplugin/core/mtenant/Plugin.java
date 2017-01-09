package net.jplugin.core.mtenant;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.mtenant.impl.MtDataSourceWrapperService;
import net.jplugin.core.mtenant.impl.filter.MtInvocationFilter;
import net.jplugin.ext.webasic.ExtensionWebHelper;

public class Plugin extends AbstractPlugin{
	/**
	 * <pre>
	 * #���⻧�����б�
	 * #��ʾ�Ƿ����ö��⻧ģʽ��Ĭ��FALSE
	 * mtenant.enable=FALSE|TRUE
	 * 
	 * #HTTP�������⻧����λ��
	 *  mtenant.req-param-at=BOTH|SESSION|COOKIE
	 *  
	 * #HTTP�������⻧�������ƣ������е����ơ�cookie�е����ƶ���������ã���������ͬ��
	 * mtenant.req-param-name=xxxxx
	 * 
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
	 */
	public Plugin(){
		if ("true".equalsIgnoreCase(ConfigFactory.getStringConfig("mtenant.enable"))){
			ExtensionDasHelper.addConnWrapperExtension(this, MtDataSourceWrapperService.class);
			ExtensionWebHelper.addServiceFilterExtension(this, MtInvocationFilter.class);
			ExtensionWebHelper.addWebCtrlFilterExtension(this, MtInvocationFilter.class);
		}else{
			System.out.println("@@@ mtenant ignore!");
		}
	}
	
	@Override
	public void init() {
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.MULTI_TANANT;
	}
	
}
