package net.jplugin.ext.filesvr.svc;

import java.io.File;

import javax.security.auth.login.Configuration;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.FileKit;
import net.jplugin.core.event.api.Event;
import net.jplugin.core.event.api.EventConsumer;
import net.jplugin.ext.filesvr.api.FileCreatedEvent;
import net.jplugin.ext.filesvr.api.FileTypes;
import net.jplugin.ext.filesvr.web.Configures;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-18 ����08:33:56
 **/

public class ComressImageConsumer extends EventConsumer {

	/* (non-Javadoc)
	 * @see net.luis.plugin.event.api.EventConsumer#consume(net.luis.plugin.event.api.Event)
	 */
	@Override
	public void consume(Event e) {
//		FileCreatedEvent fce = (FileCreatedEvent) e;
//		AssertKit.assertEqual(fce.getFile().getFileType(),FileTypes.FT_IMAGE,"file type wrong");
//		String storePath = fce.getFile().getStorePath();
//		//ƴ�Ӿ���·��
//		String absStorePath = Configures.uploadPath + "/"+storePath;
//		
//		String filename = new File(absStorePath).getName();
//		String dirname = absStorePath.substring(0,absStorePath.length() - filename.length())+"/";
//		
//		PicCompressHelper.compressPic(dirname, dirname, filename, filename+"_min", 100, 100, true);
//		
//		//���Ƶ�webĿ¼���У���ͼСͼ���������Ժ��ͼ��˵
//		String destFilename = Configures.smallFilePath +"/" + storePath +"_min";
//		FileKit.makeDirectory(new File(destFilename).getParentFile().getAbsolutePath());
//		FileKit.copyFile(absStorePath+"_min",destFilename);
//		
//		
//		destFilename = Configures.smallFilePath +"/" + storePath;
//		FileKit.makeDirectory(new File(destFilename).getParentFile().getAbsolutePath());
//		FileKit.copyFile(absStorePath,destFilename);
	}

}
