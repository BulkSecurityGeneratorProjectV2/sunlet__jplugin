package net.jplugin.core.ctx.api;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-17 ����08:41:16
 **/

public interface TransactionHandler {

	/**
	 * 
	 */
	void doBegin();

	/**
	 * 
	 */
	void doRollback();

	/**
	 * 
	 */
	void doCommit();

}
