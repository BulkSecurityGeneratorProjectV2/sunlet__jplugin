package net.jplugin.ext.filesvr.api;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-15 ����03:29:52
 **/

public interface FileUploadFilter {

	/**
	 * @param req
	 * @return
	 */
	boolean filter(HttpServletRequest req);

}
