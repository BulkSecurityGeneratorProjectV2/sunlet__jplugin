package net.jplugin.core.config;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.config.api.ConfigRepository;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.PluginEnvirement;
/**
*
* @author: LiuHang
* @version ����ʱ�䣺2015-10-12 ����01:07:22
**/
public class Plugin extends AbstractPlugin{

	public Plugin(){
		
	}
	@Override
	public void init() {
		//load config
		String cfgdir = PluginEnvirement.getInstance().getConfigDir();
		ConfigRepository repo = new ConfigRepository();
		repo.init(cfgdir);
		ConfigFactory.setRepository(repo);
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.CONFIG;
	}
	
}
