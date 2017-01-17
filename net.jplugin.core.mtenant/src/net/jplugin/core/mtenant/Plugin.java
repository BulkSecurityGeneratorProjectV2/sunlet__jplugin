package net.jplugin.core.mtenant;

import net.jplugin.common.kits.http.filter.HttpFilterManager;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.mtenant.impl.MtDataSourceWrapperService;

public class Plugin extends AbstractPlugin{
	/**
	 * <pre>
	 * #���⻧�����б�
	 * #��ʾ�Ƿ����ö��⻧ģʽ��Ĭ��FALSE
	 * mtenant.enable=FALSE|TRUE
	 * 
	 * #HTTP�������⻧����λ��
	 *  mtenant.req-param-at=BOTH|COOKIE|REQUEST
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
	 * #����������Զ��⻧�����ע�ͣ�����Ը�SQL�Ĵ���\/* IGNORE-TANENT *\/
	 * </pre>
	 */
	public Plugin(){
		if ("true".equalsIgnoreCase(ConfigFactory.getStringConfig("mtenant.enable"))){
			ExtensionDasHelper.addConnWrapperExtension(this, MtDataSourceWrapperService.class);
//			ExtensionWebHelper.addServiceFilterExtension(this, MtInvocationFilter.class);
//			ExtensionWebHelper.addWebCtrlFilterExtension(this, MtInvocationFilter.class);
			System.out.println("@@@ mtenant ENABLED! req-param="+ConfigFactory.getStringConfig("mtenant.req-param-name")+" dbfield="+ConfigFactory.getStringConfig("mtenant.field"));
		}else{
			System.out.println("@@@ mtenant ignore!");
		}
	}
	
	@Override
	public void init() {
		if ("true".equalsIgnoreCase(ConfigFactory.getStringConfig("mtenant.enable"))){
			HttpFilterManager.addFilter(new MTenantChain());
		}
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.MULTI_TANANT;
	}
	
}
