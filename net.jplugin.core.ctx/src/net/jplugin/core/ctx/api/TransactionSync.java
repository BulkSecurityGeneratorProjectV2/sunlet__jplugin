package net.jplugin.core.ctx.api;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-9 ����01:28:40
 **/

public interface TransactionSync {
	
	public void beforeCompletion();
	
	public void afterCompletion(boolean success,Throwable th);

}
