package net.jplugin.core.scheduler;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-15 ����02:39:52
 **/

public class Plugin extends AbstractPlugin{

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		return CoreServicePriority.SCHEDULER;
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IPlugin#init()
	 */
	public void init() {
		
	}

}
