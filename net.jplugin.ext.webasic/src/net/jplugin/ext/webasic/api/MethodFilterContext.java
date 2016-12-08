package net.jplugin.ext.webasic.api;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class MethodFilterContext {
	String servicePath;
	/**
	 * method �� dynamicMethodNameֻ��һ����ֵ��
	 * ��method��ֵʱ��args����ֵ��
	 */
	Method method;
	String dynamicMethodName;
	Object object;
	Object[] args;
	Object result;
	Throwable th;
	Map<String,Object> attributes;
	RequesterInfo requestInfo;
	
	private MethodFilterContext(){}
	public  MethodFilterContext(String p,Object o,Method m,Object[] a){
		this.servicePath = p;
		this.method = m;
		this.object = o;
		this.args = a;
		
		//������Ԥ������һ�£������ؿ��ܵ����û��������߳������ȡ����
		this.requestInfo = ThreadLocalContextManager.getRequestInfo();
	}
	public  MethodFilterContext(String p,Object o,String aDynamicMethodName){
		this.servicePath = p;
//		this.method = m;
		this.dynamicMethodName = aDynamicMethodName;
		this.object = o;
//		this.args = a;
		
		//������Ԥ������һ�£������ؿ��ܵ����û��������߳������ȡ����
		this.requestInfo = ThreadLocalContextManager.getRequestInfo();
	}
	
	public String getDynamicMethodName() {
		return dynamicMethodName;
	}
	public RequesterInfo getRequestInfo() {
		return requestInfo;
	}
	public String getServicePath() {
		return servicePath;
	}
	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public Throwable getTh() {
		return th;
	}
	public void setTh(Throwable th) {
		this.th = th;
	}
	
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public void setAttribute(String k,Object v){
		if (attributes==null) attributes = new HashMap<String, Object>();
		attributes.put(k, v);
	}
	@Deprecated
	public void sddAttribute(String k,Object v){
		setAttribute(k, v);
	}
	public Object getAttribute(String k){
		if (attributes==null) return null;
		return attributes.get(k);
	}
}
