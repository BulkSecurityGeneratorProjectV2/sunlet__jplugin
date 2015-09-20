package net.jplugin.ext.webasic;

import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.ClassDefine;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.ext.webasic.api.ObjectDefine;
import net.jplugin.ext.webasic.impl.InitRequestInfoFilter;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-4 ����10:29:19
 **/

public class ExtensionWebHelper {
	//add rest method .   json ��ʽ��Զ�̷���
	public static void addRestMethodExtension(AbstractPlugin plugin,String path,Class beanClz){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()}} ));
	}
	public static void addRestMethodExtension(AbstractPlugin plugin,String path,String svcName){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName}} ));
	}
	public static void addRestMethodExtension(AbstractPlugin plugin,String path,Class beanClz,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()},{"methodName",method}} ));
	}
	public static void addRestMethodExtension(AbstractPlugin plugin,String path,String svcName,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName},{"methodName",method}} ));
	}

	//add rest service��Hashmap����
	public static void addRestServiceExtension(AbstractPlugin plugin,String path,Class beanClz){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTSERVICE, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()}} ));
	}
	public static void addRestServiceExtension(AbstractPlugin plugin,String path,String svcName){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTSERVICE, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName}} ));
	}
	public static void addRestServiceExtension(AbstractPlugin plugin,String path,Class beanClz,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTSERVICE, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()},{"methodName",method}} ));
	}
	public static void addRestServiceExtension(AbstractPlugin plugin,String path,String svcName,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_RESTSERVICE, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName},{"methodName",method}} ));
	}
	
	
	//add webex controller  ��չ��webcontroller
	public static void addWebExControllerExtension(AbstractPlugin plugin,String path,Class beanClz){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBEXCONTROLLER, path, ClassDefine.class,new String[][]{{"clazz",beanClz.getName()}} ));
	}
	
	//add web controller  Web����
	public static void addWebControllerExtension(AbstractPlugin plugin,String path,Class beanClz){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()}} ));
	}
	public static void addWebControllerExtension(AbstractPlugin plugin,String path,String svcName){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName}} ));
	}
	public static void addWebControllerExtension(AbstractPlugin plugin,String path,Class beanClz,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()},{"methodName",method}} ));
	}
	public static void addWebControllerExtension(AbstractPlugin plugin,String path,String svcName,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName},{"methodName",method}} ));
	}

	//add remote call  Java���л���Զ�̷���
	public static void addRemoteCallExtension(AbstractPlugin plugin,String path,Class beanClz){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()}} ));
	}
	public static void addRemoteCallExtension(AbstractPlugin plugin,String path,String svcName){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName}} ));
	}
	public static void addRemoteCallExtension(AbstractPlugin plugin,String path,Class beanClz,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, path, ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",beanClz.getName()},{"methodName",method}} ));
	}
	public static void addRemoteCallExtension(AbstractPlugin plugin,String path,String svcName,String method){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_REMOTECALL, path, ObjectDefine.class,new String[][]{{"objType","bizLogic"},{"blName",svcName},{"methodName",method}} ));
	}
	
	//filter
	public static void addWebFilterExtension(AbstractPlugin plugin,Class filter){
		plugin.addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBFILTER,"",filter));
	}
}
