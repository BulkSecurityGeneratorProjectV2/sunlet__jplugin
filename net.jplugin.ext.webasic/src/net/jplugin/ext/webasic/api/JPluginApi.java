package net.jplugin.ext.webasic.api;

public @interface JPluginApi {
	/**
	 * ���������ͣ�CallerType
	 */
	public enum CT{APP,USER}
	/**
	 * ���Ƽ���RestrictLevel
	 */
	public enum RL{NONE,TK,AUTH}
	/**
	 * API����
	 * @return
	 */
	String name();
	/**
	 * API���Ƽ���
	 * @return
	 */
	RL restrictLevel() default RL.NONE;
	CT callerType();
}
