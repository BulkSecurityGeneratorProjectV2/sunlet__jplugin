package net.jplugin.ext.filesvr.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.jplugin.common.kits.UUIDKit;
import net.jplugin.ext.filesvr.api.IStorePathGenerator;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-18 ����05:59:43
 **/

public class StorePathGenerator implements IStorePathGenerator{

	/* (non-Javadoc)
	 * @see net.luis.plugin.filesvr.api.IStorePathGenerator#generateStorePath(java.lang.String)
	 */
	public String generateStorePath(String clientFileName) {
		return "store0/"+getYMD()+"/"+UUIDKit.getUUID();
	}

	/**
	 * @return
	 */
	private String getYMD() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/dd");
		return sdf.format(d);
	}

}
