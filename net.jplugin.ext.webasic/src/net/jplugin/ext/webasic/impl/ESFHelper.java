package net.jplugin.ext.webasic.impl;

import net.jplugin.ext.webasic.api.IControllerSet;
import net.jplugin.ext.webasic.impl.WebDriver.ControllerMeta;
import net.jplugin.ext.webasic.impl.helper.ObjectCallHelper.ObjectAndMethod;
import net.jplugin.ext.webasic.impl.restm.RestMethodControllerSet4Invoker;
import net.jplugin.ext.webasic.impl.restm.invoker.IServiceInvoker;
import net.jplugin.ext.webasic.impl.restm.invoker.ServiceInvokerSet;
import net.jplugin.ext.webasic.impl.rmethod.RmethodController;
import net.jplugin.ext.webasic.impl.rmethod.RmethodControllerSet;

public class ESFHelper {

	/**
	 * ����uri������uri�Ľ�������������������λ·����������
	 * ����Ҳ�����Ӧ�ķ�����Ӧ��·�����򷵻�null
	 * @param uri
	 * @return
	 */
	public static ControllerMeta parseUri(String uri) {
		return WebDriver.INSTANCE.parseControllerMeta(uri);
	}
	
	/**
	 * �õ���Ӧ�ķ���ͷ�����
	 * @param cm
	 * @param arg
	 * @return
	 */
	public ObjectAndMethod getObjectAndMethod(ControllerMeta cm, Class[] arg) {
		IControllerSet cs = cm.getControllerSet();
		if (cs instanceof RestMethodControllerSet4Invoker) {
			IServiceInvoker si = ServiceInvokerSet.instance.getServiceInvoker(cm.getServicePath());
			return si.getObjectCallHelper().get(cm.getOperation(), arg);
		} else if (cs instanceof RmethodControllerSet) {
			RmethodControllerSet rcs = (RmethodControllerSet) cm.getControllerSet();
			RmethodController rc = rcs.getRMethodController(cm.getServicePath());
			return rc.getObjectCallHelper().get(cm.getOperation(), arg);
		} else
			return null;
	}
}
