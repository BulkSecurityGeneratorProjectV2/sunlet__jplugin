package net.jplugin.core.kernel.api.ctx;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-16 ����08:53:31
 **/

public interface ThreadLocalContextListener {
	/**
	 * �˷�����Ҫ���쳣
	 * @param rc
	 */
	public void released(ThreadLocalContext rc);
}
