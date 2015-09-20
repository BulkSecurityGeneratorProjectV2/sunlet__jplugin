package net.jplugin.core.das.hib.api;

import net.jplugin.core.das.hib.Plugin;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-1 ����09:01:41
 **/

public class ExtensionDasHelper {
	public static void addDataMappingExtension(AbstractPlugin plugin,Class entityCls){
		plugin.addExtension(Extension.create(Plugin.EP_DATAMAPPING_SINGLE, SinglePoDefine.class,new String[][]{{"poClass",entityCls.getName()}}));
	}
}
