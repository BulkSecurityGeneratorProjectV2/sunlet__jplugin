package net.jplugin.ext.filesvr.api;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-15 ����03:30:56
 **/

public interface FileDownloadFilter {
	/**
	 * @param req
	 * @return
	 */
	public boolean filter(HttpServletRequest req) ;

}
