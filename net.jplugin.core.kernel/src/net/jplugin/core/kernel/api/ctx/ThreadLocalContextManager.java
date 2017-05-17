package net.jplugin.core.kernel.api.ctx;



/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-11 ����09:40:56
 **/

public class ThreadLocalContextManager {
	ThreadLocal<ThreadLocalContext> ctxLocal=new ThreadLocal<ThreadLocalContext>();
	
	public static ThreadLocalContextManager instance = new ThreadLocalContextManager();
	
	public static RequesterInfo getRequestInfo(){
		return ThreadLocalContextManager.instance.getContext().getRequesterInfo();
	}
	
	public static ThreadLocalContext currentContet(){
		return ThreadLocalContextManager.instance.getContext();
	}
	
	public ThreadLocalContext getContext(){
		ThreadLocalContext ctx = ctxLocal.get();
		return ctx;
	}
	/**
	 * �����http���ã�����http�������в���/����ctx
	 * ������첽���ã��ڵ��ô������в���/����ctx
	 * ȷ��������ֲ�ƥ�䣡����
	 * Contextֻ����һ���������ö�ջ���ƣ����ּ򵥡�
	 * @param rc
	 * @return 
	 */
	public ThreadLocalContext createContext(){
		if (ctxLocal.get()!=null){
			throw new RuntimeException("Ctx state not right!");
		}
		ThreadLocalContext rc = new ThreadLocalContext();
		ctxLocal.set(rc);
		return rc;
	}
	
	public void releaseContext(){
		ThreadLocalContext ctx = ctxLocal.get();
		if (ctx==null){
			throw new RuntimeException("Can't find ctx!");
		}
		ctxLocal.set(null);
		ctx.release();
	}
}
