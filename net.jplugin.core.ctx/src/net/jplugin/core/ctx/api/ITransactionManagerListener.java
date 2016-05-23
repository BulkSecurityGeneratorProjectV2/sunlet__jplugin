package net.jplugin.core.ctx.api;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-17 ����11:13:23
 **/

public interface ITransactionManagerListener {
	public void beforeBegin();
	public void afterBegin();
	public void beforeCommit();
	public void afterCommit(boolean success);
	public void beforeRollback();
}
