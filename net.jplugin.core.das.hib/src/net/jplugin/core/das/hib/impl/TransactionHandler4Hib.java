package net.jplugin.core.das.hib.impl;

import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import net.jplugin.core.ctx.api.TransactionHandler;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

/**
 * 
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-17 ����11:48:52
 **/

public class TransactionHandler4Hib implements TransactionHandler {

	DASHelper helper = null;

	/**
	 * @param dasHelper
	 */
	public TransactionHandler4Hib(DASHelper h) {
		helper = h;
	}

	/*
	 * <pre>
	 * <li>�������session���򴴽�transaction��
	 * <li>�ڲ���session��ʱ�������tx�У���ͬʱ����tx��
	 * <li>���ȷ���������������״̬��������Ҳ����tx�ġ�
	 * </pre>
	 * @see net.luis.plugin.ctx.api.TransactionHandler#doBegin()
	 */
	public void doBegin() {
		ThreadLocalContext tlc = ThreadLocalContextManager.instance
				.getContext();
		Transaction tx = helper.getTxInCtx(tlc);
		if (tx != null) {
			throw new HibDasException("tx begin a second time");
		}

		// ����Ѿ���session����������
		helper.clearSessionAndTx(tlc);
		
		
//		Session sess = helper.getSessionInCtx(tlc);
//		if (sess != null) {
//			sess.clear();
//			tx = sess.beginTransaction();
//			helper.setTxInCtx(tlc,tx);
//		}
	}

	/*
	 * ������ҵ�transaction�����ύ����session�ع鵽�Զ��ύģʽ
	 * @see net.luis.plugin.ctx.api.TransactionHandler#doCommit()
	 */
	public void doCommit() {
		ThreadLocalContext tlc = ThreadLocalContextManager.instance
				.getContext();
		try {
			Transaction tx = helper.getTxInCtx(tlc);
			if (tx != null) {
				tx.commit();
			}
		} finally {
			helper.clearSessionAndTx(tlc);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.luis.plugin.ctx.api.TransactionHandler#doRollback()
	 */
	public void doRollback() {
		ThreadLocalContext tlc = ThreadLocalContextManager.instance
		.getContext();

		try {
			Transaction tx = helper.getTxInCtx(tlc);
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			helper.clearSessionAndTx(tlc);
		}
	}

}
