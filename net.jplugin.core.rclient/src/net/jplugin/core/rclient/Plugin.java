package net.jplugin.core.rclient;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-14 ����09:35:53
 **/

public class Plugin extends AbstractPlugin{

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		// TODO Auto-generated method stub
		return CoreServicePriority.REMOTECLIENT;
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IPlugin#init()
	 */
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
