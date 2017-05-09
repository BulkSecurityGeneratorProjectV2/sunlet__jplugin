package net.jplugin.ext.webasic.impl;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.webasic.Plugin;
import net.jplugin.ext.webasic.api.IControllerSet;
import net.jplugin.ext.webasic.api.WebContext;
import net.jplugin.ext.webasic.api.WebFilter;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-2 ����05:57:36
 **/

public class WebDriver {
	public static WebDriver INSTANCE = new WebDriver();
	public static final String SERVICE_CALL = "/service";
//	public static final String OPERATION_KEY = "_o";
	private static Logger logger;
	IControllerSet[] controllerSets;
	ConcurrentHashMap<String, IControllerSet> pathMap=new ConcurrentHashMap<String, IControllerSet>();
	
	private WebFilter[] filters;
	/**
	 * @param extensionMap
	 * @param webfilters 
	 * @param controllerMap2 
	 */
	public void init() {
		controllerSets = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_CONTROLLERSET,IControllerSet.class);
		filters = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_WEBFILTER,WebFilter.class);
		
		for ( int i=0;i<controllerSets.length;i++){
			controllerSets[i].init();
		}
		
		//��ʼ��pathmap
		for (IControllerSet is:controllerSets){
			Set<String> paths = is.getAcceptPaths();
			for (String p:paths){
				if (pathMap.containsKey(p)){
					throw new RuntimeException("Multi web handlers can work with :"+p);
				}
				pathMap.put(p, is);
			}
		}
		
	}
	
	public IControllerSet[] getControllerSet(){
		return this.controllerSets;
	}

	public void dohttp(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		ThreadLocalContext tlc =null;
		try{
			tlc = ThreadLocalContextManager.instance.createContext();
			
			WebContext.initFromRequest(req);
			if (doWebFilter(req,res)){
				Throwable th = null;
				try{
					//��ȡControllerMeta����ִ��
					String path = req.getServletPath();
					ControllerMeta controllerMeta = this.parseControllerMeta(path);
					if (controllerMeta!=null)
						controllerMeta.controllerSet.dohttp(controllerMeta.servicePath, req, res, controllerMeta.operation);
					else
						throw new RuntimeException("Can't find controller for :"+path);
					
					doAfterWebFilter(req,res,null);
				}catch(Throwable t){
					th = t;
					doAfterWebFilter(req,res,th);
				}
				//��������쳣�ٴ��׳�
				if (th!=null) throw th;
			}
		}catch(Throwable e){
			e.printStackTrace();
			getLogger().error("Error when service "+req.getRequestURI(),e);
			//throw exception
			if (e instanceof ServletException) throw (ServletException)e;
			else if (e instanceof IOException) throw (IOException)e;
			else throw (new ServletException(e));
		}finally{
			ThreadLocalContextManager.instance.releaseContext();
		}
	}
	


	/**
	 * ����з���false�����ж�
	 * @param req
	 * @param res
	 * @return
	 */
	private boolean doWebFilter(HttpServletRequest req, HttpServletResponse res) {
		for (WebFilter wf:filters){
			if (!wf.doFilter(req, res)){
				return false;
			}
		}
		return true;
	}

	private void doAfterWebFilter(HttpServletRequest req, HttpServletResponse res,Throwable th) {
		for (int i=filters.length-1;i>=0;i--){
			WebFilter filter = filters[i];
			try{
				filter.doAfter(req,res,th);
			}catch(Exception e){
				e.printStackTrace();
				ServiceFactory.getService(ILogService.class).getLogger(this.getClass().getName()).error(req.getRequestURI(),e);
			}
		}
	}
	/**
	 * @return 
	 * 
	 */
	private Logger getLogger() {
		if (logger == null){
			synchronized (this.getClass()) {
				ILogService log = ServiceFactory.getService(ILogService.class);
				logger = log.getLogger(this.getClass().getName());
			}
		}
		return logger;
	}
	
//	/**
//	 * ·�����ҹ�������ȫƥ�䣬��ƥ�����һ��/��ǰ��ǰ��Ϊ�˼�����ǰ��ʵ�֣��������ƥ�䲻�ϣ���ǰ��ʼ����ƥ�䣬һ��һ���ҡ�
//	 * ����ʵ��Ϊ���ڶ�̬����ʵ��ʱ�򣬿��Դ��ݶ༶/X/X/X����dynamicMethodName
//	 * @param path
//	 * @return
//	 */
//	public ControllerMeta parseControllerMeta(String path){
//		//��ȥ��
//		int dotPos = path.lastIndexOf('.');
//		if ( dotPos >= 0){
//			path = path.substring(0,dotPos);
//		}
//		
//		IControllerSet ctroller = pathMap.get(path);
//
//		if (ctroller!=null)
//			return new ControllerMeta(ctroller,path,null);
//
//		int splitPos = path.lastIndexOf('/');
//
//		//����0��ʱ���ʺϣ�ֻ��һ��path
//		if (splitPos>0){
//			String prePath = path.substring(0, splitPos);
//			String postPath = path.substring(splitPos+1);
//			ctroller = pathMap.get(prePath);
//			
//			if (ctroller!=null)
//				return new ControllerMeta(ctroller,prePath,postPath);
//			else 
//				return searchFromStartToEnd3(path);
//		}
//		return null;
//	}
	
	/**
	 * ��ǰ������
	 * @param path
	 * @return
	 */
	public ControllerMeta parseControllerMeta(String path) {
		//��ȥ��
		int dotPos = path.lastIndexOf('.');
		if ( dotPos >= 0){
			path = path.substring(0,dotPos);
		}
				
		//��ȫ�� /�����⴦��
		if (pathMap.get("/")!=null){
			return new ControllerMeta(pathMap.get("/"),"/",path.substring(1));
		}
		
		//��1��ʼ����
		int pos =1;
		while(true){
			pos = path.indexOf('/',pos);
			if (pos<0) {
				//��ĩβ������ƥ��
				IControllerSet ctroller = pathMap.get(path);
				if (ctroller!=null)
					return new ControllerMeta(ctroller,path,null);
				else
					return null;
			}
			String prePath = path.substring(0, pos);
			IControllerSet ctroller = pathMap.get(prePath);
			if (ctroller!=null){
				String postPath = path.substring(pos+1);
				return new ControllerMeta(ctroller,prePath,postPath);
			}
			pos++;
		}
	}

//	
//	/**
//	 * ��ǰ�����ҵ������ڶ���Ϊֹ
//	 * @param path
//	 * @return
//	 */
//	private ControllerMeta searchFromStartToEnd3(String path) {
//		//�������һ�� / ��λ��
//		int lastPos = path.lastIndexOf('/');
//		if (lastPos<=0) //�������0˵��ֻ��һ��/��Ҳ��ͬ������ 
//			return null;
//		
//		int pos =1;
//		while(true){
//			pos = path.indexOf('/',pos);
//			if (pos>=lastPos) 
//				return null;
//			if (pos<0) //���ˣ�ֻ��һ��/ʱ��ᵽ�����Ϊ�Ǵ�1��ʼ�ҵ�
//				return null;
//			
//			String prePath = path.substring(0, pos);
//			IControllerSet ctroller = pathMap.get(prePath);
//			if (ctroller!=null){
//				String postPath = path.substring(pos+1);
//				return new ControllerMeta(ctroller,prePath,postPath);
//			}
//			
//			pos++;
//		}
//	}

	public static class ControllerMeta {
		IControllerSet controllerSet;
		String servicePath;
		String operation;

		ControllerMeta(IControllerSet cs,String s,String o){
			this.controllerSet = cs;
			this.servicePath = s;
			this.operation = o;
		}
		
		public IControllerSet getControllerSet() {
			return controllerSet;
		}


		public String getServicePath() {
			return servicePath;
		}


		public String getOperation() {
			return operation;
		}

	}
	
//	private void dohttpThrowEx(HttpServletRequest req,HttpServletResponse res) throws Throwable{
//		String path = req.getServletPath();
//		
//		//��ȥ��
//		int dotPos = path.lastIndexOf('.');
//		if ( dotPos >= 0){
//			path = path.substring(0,dotPos);
//		}
//		
//		IControllerSet ctroller = pathMap.get(path);
//
//		if (ctroller!=null){
//			ctroller.dohttp(path,req, res,null);
//			return;
//		}
//
//		int splitPos = path.lastIndexOf('/');
//		//����0��ʱ���ʺϣ�ֻ��һ��path
//		if (splitPos>0){
//			String prePath = path.substring(0, splitPos);
//			String postPath = path.substring(splitPos+1);
//			ctroller = pathMap.get(prePath);
//			if (ctroller!=null){
//				ctroller.dohttp(prePath,req,res,postPath);
//				return;
//			}
//		}
//			
//		throw new RuntimeException("Can't find controller for :"+path);
//		
//	}
}
