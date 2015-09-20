package net.jplugin.ext.filesvr.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.FileKit;
import net.jplugin.core.ctx.api.RuleResult;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.filesvr.Plugin;
import net.jplugin.ext.filesvr.api.FileDownloadFilter;
import net.jplugin.ext.filesvr.api.FileTypes;
import net.jplugin.ext.filesvr.api.FileUploadFilter;
import net.jplugin.ext.filesvr.api.Size;
import net.jplugin.ext.filesvr.db.DBCloudFile;
import net.jplugin.ext.filesvr.svc.IFileService;
import net.jplugin.ext.filesvr.svc.PicCompressHelper;
import net.jplugin.ext.filesvr.web.UploadHelper.SaveFileResult;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-15 ����03:13:31
 **/

public class FileTransferServlet {

	private static final String FILE_ID = "fileId";

	
	public void downloadFile(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		//����filter
		FileDownloadFilter[] filters = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_DOWNLOADFILTER, FileDownloadFilter.class);
		for (FileDownloadFilter f:filters){
			if (!f.filter(req)){
				return;
			}
		}
		
		String fileid = req.getParameter(FILE_ID);
		AssertKit.assertStringNotNull(fileid, "fileid");
		
		IFileService svc = RuleServiceFactory.getRuleService(IFileService.class);
		DBCloudFile cf = svc.getFile(Long.parseLong(fileid));
		
		if (cf==null) return;
		
		if (FileTypes.FT_IMAGE.equals(cf.getFileType())){
			String scale=req.getParameter("scale");
			if (scale==null) scale=ImgScale.BIG;
			/*Ԥ��ѹ���÷���webĿ¼�ķ�ʽ
			String path = "/upload/" + cf.getStorePath();
			path = ImgScale.maintainImgName(path,scale);
			req.getRequestDispatcher(path).forward(req, res);
			*/
			
			//�·�ʽ��ʹ��ʱѹ������������web����
			AssertKit.assertNotNull(cf, "the cloud file");
			String origFilePath = Configures.uploadPath+"/"+cf.getStorePath();
			String dirname = new File(origFilePath).getParentFile().getAbsolutePath()+"/";
			String origFileName = origFilePath.substring(dirname.length());
			
			String targetFileName = ImgScale.maintainImgName(origFileName,scale);
			String targetFilePath = dirname +targetFileName;
			
			Size targetSize = ImgScale.getTargetSize(scale);
			if (!FileKit.existsFile(targetFilePath)){
				//ѹ���ļ�
				PicCompressHelper.compressPic(dirname, dirname, origFileName, targetFileName, targetSize.width, targetSize.height, true);
			}
			DownloadHelper.downLoad(targetFilePath, cf.getFileName(), res, true);
		}else{
			AssertKit.assertNotNull(cf, "the cloud file");
			String filepath = Configures.uploadPath+"/"+cf.getStorePath();
			DownloadHelper.downLoad(filepath, cf.getFileName(), res, false);
		}
	}
	
	public void uploadFile(HttpServletRequest req,HttpServletResponse res) throws IOException{
		//����filter
		FileUploadFilter[] filters = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_UPLOADFILTER, FileUploadFilter.class);
		for (FileUploadFilter f:filters){
			if (!f.filter(req)){
				return;
			}
		}
		
		RequesterInfo ctx = ThreadLocalContextManager.instance.getContext().getRequesterInfo();
		IFileService svc = RuleServiceFactory.getRuleService(IFileService.class);
		
		String reqFileType = req.getParameter("filetype");

		List<SaveFileResult> files = UploadHelper.saveFiles(req,true,reqFileType);
		if (reqFileType!=null){
			if (!FileTypes.validType(reqFileType)){
				throw new RuntimeException("Error file type:"+reqFileType);
			}
		}
		
		if (files.size() > 1){
			throw new RuntimeException("cound't come here");
		}
		if (files.size() ==0 ){
			return;
		}
		SaveFileResult sfr = files.get(0);
		long fileid = svc.createFile(sfr.getFilename(), sfr.getFileType(), sfr.getSize(), sfr.getStorePath());
		
		//return
		RuleResult rr = RuleResult.create(RuleResult.OK);
		rr.setContent(FILE_ID, fileid);
		res.getWriter().write(rr.getJson());
	}
}
