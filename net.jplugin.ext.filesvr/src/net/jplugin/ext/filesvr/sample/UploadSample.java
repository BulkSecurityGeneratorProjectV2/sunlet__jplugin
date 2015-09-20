package net.jplugin.ext.filesvr.sample;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 
 * �����ϸ���
 **/

public class UploadSample {

	private String uploadPath = "C:\\upload\\"; // �ϴ��ļ���Ŀ¼
	private String tempPath = "C:\\upload\\tmp\\"; // ��ʱ�ļ�Ŀ¼

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			DiskFileItemFactory fu = new DiskFileItemFactory();

			// ���û�������С��������4kb
			fu.setSizeThreshold(4096);
			// ������ʱĿ¼��
			fu.setRepository(new File(tempPath));

			ServletFileUpload upload = new ServletFileUpload(fu);

			// ��������ļ��ߴ磬������4MB
			upload.setSizeMax(4194304);

			// �õ����е��ļ���
			List fileItems = upload.parseRequest(request);
			Iterator i = fileItems.iterator();
			// ���δ���ÿһ���ļ���
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				// ����ļ���������ļ�������·����
				String fileName = fi.getName();
				long fileSize = fi.getSize();
				// ��������Լ�¼�û����ļ���Ϣ
				// ...
				// д���ļ����ݶ��ļ���Ϊa.txt�����Դ�fileName����ȡ�ļ�����
				fi.write(new File(uploadPath + "a.txt"));
			}
		} catch (Exception e) {
			// ������ת����ҳ��
		}
	}

}
