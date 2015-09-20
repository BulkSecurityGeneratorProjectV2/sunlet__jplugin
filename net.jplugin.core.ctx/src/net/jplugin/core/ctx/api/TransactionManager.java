package net.jplugin.core.ctx.api;


/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-12 ����10:39:12
 **/

public interface TransactionManager {
	enum Status{NOTX,INTX,MARKED_ROLLBACK}
	/**
	 * �����������ظ����������쳣
	 */
	public void begin();

	/**
	 * �ύ����
	 */
	public void commit();

	/**
	 * �ع�����
	 */
	public void rollback();
	
	/**
	 * ���ûع�
	 */
	public void setRollbackOnly();
	
	public Status getStatus();

	/**
	 * @param txHandler
	 */
	public void addTransactionHandler(TransactionHandler txHandler);
	
	/**
	 * �����ﵱ�вſ��Ե��ô˷���
	 * @param s
	 */
	public boolean containTransactionSync(TransactionSync s);
	
	/**
	 * �����ﵱ�вſ��Ե��ô˷���
	 * @param s
	 */
	public void addTransactionSync(TransactionSync s);
	
	/**
	 * �����ﵱ�вſ��Ե��ô˷���
	 * @param s
	 */
	public void removeTransactionSync(TransactionSync s);

}
