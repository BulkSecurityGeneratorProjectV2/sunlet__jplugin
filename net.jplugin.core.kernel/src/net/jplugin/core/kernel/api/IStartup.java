package net.jplugin.core.kernel.api;

import java.util.List;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-15 ����01:10:25
 **/

public interface IStartup {
	public void startFailed(Throwable th,List<PluginError> errors);
	public void startSuccess();
}
