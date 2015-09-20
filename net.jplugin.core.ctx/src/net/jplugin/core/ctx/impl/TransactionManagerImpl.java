package net.jplugin.core.ctx.impl;

import net.jplugin.core.ctx.api.RollBackException;
import net.jplugin.core.ctx.api.TransactionHandler;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.ctx.api.TransactionSync;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-17 ����08:39:26
 **/
/**
 * @author Luis
 *
 */
public class TransactionManagerImpl implements TransactionManager {
	TransactionHandler[] handlers = new TransactionHandler[0]; 

	ThreadLocal<TransactionObject> txObject= new ThreadLocal<TransactionObject>(){
		@Override
		protected TransactionObject initialValue() {
			return new TransactionObject();
		}
	};

//	
//	ThreadLocal<Status> state= new ThreadLocal<Status>(){
//		@Override
//		protected Status initialValue() {
//			return Status.NOTX;
//		}
//	};
	
	private Logger logger;
	
	public void begin() {
		 //�ȼ��״̬
		 if (txObject.get().getStatus()!=Status.NOTX){
			 throw new RuntimeException("tx state not right");
		 }
		 
		 int pos = 0;
		 try{
			 for (;pos<handlers.length;pos++){
				 handlers[pos].doBegin();
			 }
		 }catch(Throwable e){
			 rollbackPreviousSilently(pos-1);
			 throw new TxRuntimeException("�������﷢���쳣",e);
		 }
		 
		 //�ɹ������趨״̬
		 txObject.get().setStatus(Status.INTX);
		 txObject.get().notifyTxBegin();
	}
	


	public void commit() {
		Status currnetState = txObject.get().getStatus();
		if (currnetState != Status.INTX && currnetState !=Status.MARKED_ROLLBACK){
			throw new RuntimeException("tx state not right");
		}
		if (currnetState == Status.MARKED_ROLLBACK){
			rollbackAllSilently();
			throw new RollBackException("tx marked roll back");
		}
		
		//ִ���ύǰ������ͬ������������쳣�����׳��쳣
		txObject.get().notifyBeforeCompletion();
		
		
		//ִ���ύ
		TxRuntimeException theException = null;
		int pos = 0;
		try{
			for (;pos<handlers.length;pos++){
				handlers[pos].doCommit();
			}
		}catch(Throwable e){
			commitFollowingsSilently(pos+1);
			theException = new TxRuntimeException(handlers.length>1? "�����ύ�쳣����Ϊ�ж�����ﴦ���������ܲ���ȫ�ύ":"�����ύ�쳣",e);
			throw theException;
		}finally{
			//������ζ��趨״̬
			txObject.get().setStatus(Status.NOTX);
			txObject.get().notifyAfterCommit(theException);
		}
	}

	public void rollback() {
		Status currnetState = txObject.get().getStatus();
		
		//������ύʱ��ʧ�ܲ���Ҫ����rollback��������д�����ʱ���޷����⣬��ʱӦ����ɹ�
		if (currnetState == Status.NOTX){
			return;
		}
		
		if (currnetState != Status.INTX
				&& currnetState != Status.MARKED_ROLLBACK) {
			throw new RuntimeException("tx state not right");
		}

		TxRuntimeException theException = null;
		int pos = 0;
		try {
			for (; pos < handlers.length; pos++) {
				handlers[pos].doRollback();
			}
		} catch (Throwable e) {
			rollbackFollowingsSilently(pos);
			theException = new TxRuntimeException(handlers.length>1? "����ع��쳣����Ϊ�ж�����ﴦ���������ܲ���ȫ�ع�":"����ع��쳣", e);
			throw theException;
		} finally {
			//������ζ��趨״̬
			txObject.get().setStatus(Status.NOTX);
			txObject.get().notifyAfterRollback(theException);
		}
	}
	
	public void setRollbackOnly() {
		txObject.get().setStatus(Status.MARKED_ROLLBACK);
	}
	
	/**
	 * @param index
	 */
	private void commitFollowingsSilently(int index) {
		 for (int i=index;i<handlers.length;i++){
			 try{
				 handlers[i].doCommit();
			 }catch(Throwable th){
				 getLogger().error("�ύ�������ﴦ����ʱ�쳣",th);
			 }
		 }
	}

	/**
	 * 
	 */
	private void rollbackAllSilently() {
		rollbackPreviousSilently(handlers.length-1);
	}

	/**
	 * @param length
	 */
	private void rollbackPreviousSilently(int pos) {
		 for (int i=pos;i>=0;i--){
			 try{
				 handlers[i].doRollback();
			 }catch(Throwable th){
				 getLogger().error("�ع�ǰ�����ﴦ����ʱ�쳣",th);
			 }
		 }
	}

	public Status getStatus() {
		return txObject.get().getStatus();
	}
	


	/**
	 * @param pos
	 */
	private void rollbackFollowingsSilently(int index) {
		for (int i=index;i<handlers.length;i++){
			 try{
				 handlers[i].doRollback();
			 }catch(Throwable th){
				 getLogger().error("�ع��������ﴦ����ʱ�쳣",th);
			 }
		 }
	}
	/**
	 * @return 
	 * @return
	 */
	private  Logger getLogger() {
		if (logger==null){
			synchronized (this) {
				if (logger==null){
					logger = ServiceFactory.getService(ILogService.class).getLogger(this.getClass().getName());
				}
			}
		}
		return logger;
	}



	/* (non-Javadoc)
	 * @see net.luis.plugin.ctx.api.TransactionManager#addTransactionHandler(net.luis.plugin.ctx.api.TransactionHandler)
	 */
	public void addTransactionHandler(TransactionHandler txHandler) {
		TransactionHandler[] newhandlers = new TransactionHandler[ handlers.length  +1];
		for (int i=0;i<handlers.length;i++){
			newhandlers[i]=handlers[i];
		}
		newhandlers[newhandlers.length-1] = txHandler;
		handlers = newhandlers;
	}



	/* (non-Javadoc)
	 * @see net.luis.plugin.ctx.api.TransactionManager#addTransactionSync(net.luis.plugin.ctx.api.TransactionSync)
	 */
	public void addTransactionSync(TransactionSync s) {
		txObject.get().addSync(s);
	}



	/* (non-Javadoc)
	 * @see net.luis.plugin.ctx.api.TransactionManager#containTransactionSync(net.luis.plugin.ctx.api.TransactionSync)
	 */
	public boolean containTransactionSync(TransactionSync s) {
		return txObject.get().containsSync(s);
	}



	/* (non-Javadoc)
	 * @see net.luis.plugin.ctx.api.TransactionManager#removeTransactionSync(net.luis.plugin.ctx.api.TransactionSync)
	 */
	public void removeTransactionSync(TransactionSync s) {
		txObject.get().removeSync(s);
	}

}