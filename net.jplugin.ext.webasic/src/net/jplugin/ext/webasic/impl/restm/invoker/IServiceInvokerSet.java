package net.jplugin.ext.webasic.impl.restm.invoker;

import java.util.Set;
/**
  * 
 * <P>�ýӿ��û����ı���ʽ����һ���Ѿ�������Rest���񣬲��õ����ؽ����<br>
 * ���԰������·�ʽ��ȡ�ýӿڵ���ʵ���� ServiceInvokerSet.instance;
 * ���õķ���������call������ ServiceInvokerSet.instance.call( callParam)
 * <br>
 * ����CallParam���������·������� : CallParam.create(path,operation,map);
 * ����,���������£�
 * 		<li>path:��������·������  /svc/cust</li>
 * 		<li>operation:��������ķ����������� addCustomer</li>
 * 		<li>map:����Ĳ��� ���ֱ��Ӧ��Java������ÿ���������������Ͳ���JSON��ʽ</li>
 * <br>
 * <br>
 * ����ֵ��Ŀǰrestful���񷵻ؽṹ��ͬ�����Բ��վ��ĵ���
 *  
 * @author Luis LiuHang
* 
 *
 */
public interface IServiceInvokerSet {
	public void init();
	public void call(CallParam cp)  throws Throwable;
	public Set<String> getAcceptPaths();
	//Ϊ��֧��ESF���
	public IServiceInvoker getServiceInvoker(String path);
}
