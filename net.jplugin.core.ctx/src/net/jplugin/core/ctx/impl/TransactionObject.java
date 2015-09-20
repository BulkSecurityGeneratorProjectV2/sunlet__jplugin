package net.jplugin.core.ctx.impl;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.ctx.api.TransactionSync;
import net.jplugin.core.ctx.api.TransactionManager.Status;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-9 ����12:03:21
 **/

public class TransactionObject {
	TransactionManager.Status status;
	List<TransactionSync> syncs = null;
	
	public TransactionObject(){
		this.status = Status.NOTX;
	}
	
	public void setStatus(TransactionManager.Status status) {
		this.status = status;
	}

	public TransactionManager.Status getStatus(){
		return this.status;
	}
	
	public boolean containsSync(TransactionSync sync){
		if (this.status == Status.NOTX){
			throw new TxRuntimeException("not in transaction");
		}
		if (syncs == null){
			return false;
		}
		return syncs.contains(sync);
	}
	
	public void addSync(TransactionSync sync){
		if (this.status == Status.NOTX){
			throw new TxRuntimeException("not in transaction");
		}
		
		if (syncs == null){
			syncs = new ArrayList<TransactionSync>(3);
		}
		if (syncs.contains(sync)){
			throw new RuntimeException("the sync already exists");
		}
		syncs.add(sync);
	}
	
	/**
	 * @param s
	 * @return
	 */
	public void removeSync(TransactionSync s) {
		if (this.status == Status.NOTX){
			throw new TxRuntimeException("not in transaction");
		}
		if (syncs == null){
			return;
		}else{
			syncs.remove(s);
		}
	}
	
	/**
	 * 
	 */
	public void notifyTxBegin() {
		//�����������������飬syncӦ���Ѿ����ύ���߻ع���ʱ��������ˣ���
		if (syncs!=null && !syncs.isEmpty()){
			//�����˲�Ӧ�÷��������⣬����ͬ��û�������
			getLogger().error("�����˲�Ӧ�÷��������⣬����ͬ��û�������!!!!!!");
			syncs.clear();
		}
	}
	/**
	 * ���쳣���׳�
	 */
	public void notifyBeforeCompletion() {
		if (syncs == null){
			return;
		}
		
		for (TransactionSync s:syncs){
			s.beforeCompletion();
		}
	}
	
	//�ڳɹ��ύ��������׳��쳣������������׳��쳣
	public void notifyAfterCommit(Throwable th){
		if (syncs == null){
			return;
		}
		
		if (th==null){
			//ִ�У��׳��쳣
			Throwable theNewTh = null;
			for (TransactionSync s:syncs){
				try{
					s.afterCompletion(true, null);
				}catch(Throwable newTh){
					logError("Exception in transaction sync:1",newTh);
					if (theNewTh==null){
						theNewTh = newTh;
					}
				}
			}
			//����
			this.syncs.clear();
			//�׳��쳣
			if (theNewTh!=null){
				if (theNewTh instanceof RuntimeException){
					throw (RuntimeException)theNewTh;
				}else{
					throw new RuntimeException(theNewTh.getMessage(),theNewTh);
				}
			}
		}else{
			//ִ�У����׳��쳣
			for (TransactionSync s:syncs){
				try{
					s.afterCompletion(false, th);
				}catch(Throwable e){
					logError("Exception in transaction sync:1",e);
				}
			}
			//����
			this.syncs.clear();
		}
	}
	public void notifyAfterRollback(Throwable th){
		if (syncs == null){
			return;
		}
		//ִ�У����׳��쳣
		for (TransactionSync s:syncs){
			try{
				s.afterCompletion(false, th);
			}catch(Throwable e){
				logError("Exception in transaction sync:2",e);
			}
		}
		//����
		this.syncs.clear();
	}

	/**
	 * @param string
	 * @param e
	 */
	private void logError(String string, Throwable e) {
		getLogger().error(string, e);
	}
	
	
	private static Logger logger;
	/**
	 * @return 
	 * @return
	 */
	private static Logger getLogger() {
		if (logger==null){
			synchronized (TransactionObject.class) {
				if (logger==null){
					logger = ServiceFactory.getService(ILogService.class).getLogger(TransactionObject.class.getName());
				}
			}
		}
		return logger;
	}
}
