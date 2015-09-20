package net.jplugin.core.kernel.api;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-4 ����01:39:45
 **/

public abstract class AbstractPluginForTest extends AbstractPlugin{
	public final void init() {
		try{
			test();
			System.out.println("Plugin:"+this.getClass().getName()+" ���Գɹ�!");
		}catch(Throwable e){
			System.out.println("Plugin:"+this.getClass().getName()+" ����ʧ��!");
			e.printStackTrace();
		}
	}
	

	/**
	 * 
	 */
	public abstract void test() throws Throwable;

}
