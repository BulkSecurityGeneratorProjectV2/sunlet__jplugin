package net.jplugin.ext.filesvr.db;

import net.jplugin.core.das.hib.api.IPersistObjDefinition;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-16 ����05:11:03
 **/

public class DBDefine implements IPersistObjDefinition {

	/* (non-Javadoc)
	 * @see net.luis.plugin.das.api.IPersistObjDefinition#getClasses()
	 */
	public Class[] getClasses() {
		return new Class[]{DBCloudFile.class};
	}

}
