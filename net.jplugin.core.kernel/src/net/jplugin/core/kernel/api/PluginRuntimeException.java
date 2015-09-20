package net.jplugin.core.kernel.api;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-7 ����10:21:24
 **/

public class PluginRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public PluginRuntimeException() {
	}

	/**
	 * @param message
	 */
	public PluginRuntimeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public PluginRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PluginRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
