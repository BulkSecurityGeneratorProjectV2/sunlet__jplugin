package net.jplugin.ext.webasic.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
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
	String name() default "";
	/**
	 * API���Ƽ���
	 * @return
	 */
	RL restrictLevel() default RL.NONE;
	CT callerType();
}
