package net.jplugin.ext.webasic.impl.rmethod;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-13 ����09:07:46
 **/

public class RemoteExceptionInfo {
	String exceptionMessage;

	/**
	 * @param message
	 */
	public RemoteExceptionInfo(String message) {
		this.exceptionMessage = message;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
}
