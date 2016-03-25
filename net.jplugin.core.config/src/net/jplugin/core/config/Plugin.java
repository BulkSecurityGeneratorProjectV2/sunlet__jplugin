package net.jplugin.core.config;

import net.jplugin.core.config.api.ConfigChangeManager;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.config.api.IConfigChangeHandler;
import net.jplugin.core.config.impl.ConfigRepository;
import net.jplugin.core.config.impl.PropertyFilter;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.kernel.api.PluginEnvirement;
/**
*
* @author: LiuHang
* @version ����ʱ�䣺2015-10-12 ����01:07:22
**/
@PluginAnnotation(prepareSeq=-1)
public class Plugin extends AbstractPlugin{

	public static final String EP_CONFIG_CHANGE_HANDLER = "EP_CONFIG_CHANGE_HANDLER";

	//���ע�⣺
	//������ǻ�����������ز���Ĺ��캯���ж�����ʹ�ã����˱��֪ͨ��֮�⣩
	//������ΪpropertyFilter��Ҫ��load�׶�ʹ��
	public static void prepare(){
		String cfgdir = PluginEnvirement.getInstance().getConfigDir();
		ConfigRepository repo = new ConfigRepository();
		repo.init(cfgdir);
		ConfigFactory._setLocalConfigProvidor(repo);
		Extension.propertyFilter = new PropertyFilter();
	}

	public Plugin(){
		//add point
		this.addExtensionPoint(ExtensionPoint.create(EP_CONFIG_CHANGE_HANDLER, IConfigChangeHandler.class,true));
	}
	@Override
	public void init() {
		//load config
		ConfigChangeManager.instance.init();
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.CONFIG;
	}
	
	
}
