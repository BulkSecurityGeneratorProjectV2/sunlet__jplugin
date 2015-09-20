package net.jplugin.core.service;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.service.api.Constants;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-6 ����03:31:14
 **/

public class ExtensionServiceHelper {
	public static void addServiceExtension(AbstractPlugin plugin,String name,Class impl){
		plugin.addExtension(Extension.create(Constants.EP_SERVICE, name,impl));
	}
}
