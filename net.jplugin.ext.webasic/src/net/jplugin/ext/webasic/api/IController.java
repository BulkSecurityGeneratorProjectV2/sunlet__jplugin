package net.jplugin.ext.webasic.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-3 ����05:51:09
 **/

public interface IController {
	
	public void dohttp(HttpServletRequest req,HttpServletResponse res,String innerPath) throws Throwable;
	
}
