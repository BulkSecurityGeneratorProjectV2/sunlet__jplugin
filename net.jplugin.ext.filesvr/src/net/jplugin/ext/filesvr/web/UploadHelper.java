package net.jplugin.ext.filesvr.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.ConfigHelper;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.filesvr.Plugin;
import net.jplugin.ext.filesvr.api.FileTypes;
import net.jplugin.ext.filesvr.api.IStorePathGenerator;
import net.jplugin.org.apache.log.Logger;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-16 ����10:37:10
 **/

public class UploadHelper {

	/**
	 * @author Luis
	 *
	 */
	public static class SaveFileResult {
		private String filename;
		private long size;
		private String storePath;
		private String fileType;
		public String getFilename() {
			return filename;
		}
		public long getSize() {
			return size;
		}
		public String getStorePath() {
			return storePath;
		}
		public String getFileType() {
			return fileType;
		}

		
	}
	


	/**
	 * �����������ʧ�ܶ����������أ�ֻ�ǲ���������ļ�
	 * @param request
	 * @param reqFileType 
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public static List<SaveFileResult> saveFiles(HttpServletRequest request,boolean onlyOne, String reqFileType) {
		//�������ֱ�Ӵ���filetype
		
		List<SaveFileResult> retList = new ArrayList<SaveFileResult>(2);
		try {
			DiskFileItemFactory fu = new DiskFileItemFactory();

			// ���û�������С��������4kb
			fu.setSizeThreshold(Configures.uploadBufferSize);
			// ������ʱĿ¼��
			fu.setRepository(new File(Configures.tempPath));

			ServletFileUpload upload = new ServletFileUpload(fu);



			// �õ����е��ļ���
			List fileItems = upload.parseRequest(request);
			Iterator i = fileItems.iterator();
			// ���δ���ÿһ���ļ���
			
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				// ����ļ���������ļ�������·����
				String fileName = fi.getName();
				
				System.out.println("field name="+fi.getName());

				if (StringKit.isNull(fileName)){
					//�����ļ���
					continue;
				}
				
				long fileSize = fi.getSize();
				if (fileSize==0){
					continue;
				}
				
				String fileType = reqFileType!=null? reqFileType:FileTypes.getFileType(FileKit.getFileExt(fileName));
				if (fileType.equals(FileTypes.FT_IMAGE)){
					// ��������ļ��ߴ磬������4MB
					upload.setSizeMax(Configures.maxPicSize);
					if (fileSize>Configures.maxPicSize){
						continue;
					}
				}else{
					upload.setSizeMax(Configures.maxFileSize);
					if (fileSize>Configures.maxFileSize){
						continue;
					}
				}

				String storePath = IStorePathGenerator.instance.generateStorePath(fileName);
				File file = new File(Configures.uploadPath + "/"+storePath);
				FileKit.makeDirectory(file.getParentFile().getAbsolutePath());
				fi.write(file);
				
				SaveFileResult res = new SaveFileResult();
				res.filename = fileName;
				res.size = fileSize;
				res.fileType = fileType;
				res.storePath = storePath;
				retList.add(res);
				
				if (onlyOne){
					break;
				}
			}
			return retList;
		} catch (Exception e) {
			ILogService svc = ServiceFactory.getService(ILogService.class);
			svc.getLogger(UploadHelper.class.getName()).error("file upload error",e);
			return retList;
		}
	}


}
