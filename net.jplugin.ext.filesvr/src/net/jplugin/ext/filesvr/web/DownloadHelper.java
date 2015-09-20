package net.jplugin.ext.filesvr.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.service.api.ServiceFactory;

/**
 * 
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-18 ����07:14:05
 **/

public class DownloadHelper {

	// ֧�����ߴ�
	public static void downLoad(String filePath,String downloadName, HttpServletResponse response,
			boolean isOnLine){
		File f = new File(filePath);
		FileInputStream inputStream = null;
		BufferedInputStream br = null;
		OutputStream out=null;
		try {
			inputStream = new FileInputStream(f);
			br = new BufferedInputStream(inputStream);
			byte[] buf = new byte[1024];
			int len = 0;

			response.reset(); // �ǳ���Ҫ
			if (isOnLine) { // ���ߴ򿪷�ʽ
				URL u = new URL("file:///" + filePath);
				response.setContentType(u.openConnection().getContentType());
				response.setHeader("Content-Disposition", "inline; filename="
						+ downloadName);
				// �ļ���Ӧ�ñ����UTF-8
			} else { // �����ط�ʽ
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + java.net.URLEncoder.encode(downloadName, "UTF-8"));
			}
			out = response.getOutputStream();
			while ((len = br.read(buf)) > 0)
				out.write(buf, 0, len);
			out.flush();
		}catch(Exception e){
			throw new RuntimeException("download file error:"+filePath,e);
		}finally {
			closeQuirtely(br);
			closeQuirtely(inputStream);
			closeQuirtely(out);
		}
	}


	private static void closeQuirtely(OutputStream s) {
		if (s!=null){
			try{
				s.close();
			}catch(Exception e){
				ServiceFactory.getService(ILogService.class).getLogger(DownloadHelper.class.getName()).error("�ر����쳣",e);
			}
		}
	}


	/**
	 * @param br
	 */
	private static void closeQuirtely(InputStream s) {
		if (s!=null){
			try{
				s.close();
			}catch(Exception e){
				ServiceFactory.getService(ILogService.class).getLogger(DownloadHelper.class.getName()).error("�ر����쳣",e);
			}
		}
	}
}
