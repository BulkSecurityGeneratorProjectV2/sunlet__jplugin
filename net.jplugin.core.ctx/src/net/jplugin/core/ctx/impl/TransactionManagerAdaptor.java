package net.jplugin.core.ctx.impl;

import net.jplugin.core.ctx.api.TransactionHandler;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.ctx.api.TransactionSync;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-17 ����11:17:32
 **/

public class TransactionManagerAdaptor implements TransactionManager{
	private TransactionManager inner;

	public TransactionManagerAdaptor(){
		this.inner = new TransactionManagerImpl();
	}

	public void begin(){
		this.begin("");
	}
	public void begin(String desc) {
		try{
			inner.begin();
			RuleLoggerHelper.dolog("tx begin success -"+desc);
		}catch(Exception e){
			RuleLoggerHelper.dolog("tx begin error. -"+desc,e);
			rethrow(e);
		}
	}
	public void commit() {
		this.commit("");
	}
	public void commit(String desc) {
		try{
			inner.commit();
			RuleLoggerHelper.dolog("tx commit success. -"+desc);
		}catch(Exception e){
			RuleLoggerHelper.dolog("tx commit error. -"+desc,e);
			rethrow(e);
		}
	}

	public Status getStatus() {
		return inner.getStatus();
	}

	public void rollback() {
		this.rollback("");
	}
	public void rollback(String desc) {
		try{
			inner.rollback();
			RuleLoggerHelper.dolog("tx rollback success. -"+desc);
		}catch(Exception e){
			RuleLoggerHelper.dolog("tx rollback error. -"+desc,e);
			rethrow(e);
		}
	}

	public void setRollbackOnly() {
		try{
			inner.setRollbackOnly();
			RuleLoggerHelper.dolog("tx rollbackonly success. ");
		}catch(Exception e){
			RuleLoggerHelper.dolog("tx rollbackonly error. ",e);
			rethrow(e);
		}
	}
	/**
	 * @param e
	 */
	private void rethrow(Exception e) {
		if (e instanceof RuntimeException)
			throw (RuntimeException)e;
		else 
			throw new RuntimeException(e);
	}

	public void addTransactionHandler(TransactionHandler txHandler) {
		inner.addTransactionHandler(txHandler);
	}

	public void addTransactionSync(TransactionSync s) {
		inner.addTransactionSync(s);
	}

	public boolean containTransactionSync(TransactionSync s) {
		return inner.containTransactionSync(s);
	}

	public void removeTransactionSync(TransactionSync s) {
		inner.removeTransactionSync(s);
	}
}