package net.jplugin.ext.filesvr.api;

import net.jplugin.ext.filesvr.web.StorePathGenerator;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-18 ����05:56:32
 **/

public interface IStorePathGenerator {
	public static IStorePathGenerator instance = new StorePathGenerator();
	/**
	 * �����ļ������������洢Ŀ¼�����ƣ�Ҳ������ "/"
	 * ���磺abc.txt   a/b/c/dd.txt 
	 *  
	 * @param clientFileName
	 * @return
	 */
	public String generateStorePath(String clientFileName);

}
