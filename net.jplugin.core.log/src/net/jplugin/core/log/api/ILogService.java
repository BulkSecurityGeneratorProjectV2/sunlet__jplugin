package net.jplugin.core.log.api;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-8 ����12:22:40
 **/

public interface ILogService {

	/**
	 * ��ȡϵͳ��־Logger
	 * @param name
	 * @return
	 */
	public abstract Logger getLogger(String name);

	/**
	 * ��ȡ�����ר��Logger������������ģ�����additive=false��level=debug
	 * @param filename
	 * @return
	 */
	public abstract Logger getSpecicalLogger(String filename);

}