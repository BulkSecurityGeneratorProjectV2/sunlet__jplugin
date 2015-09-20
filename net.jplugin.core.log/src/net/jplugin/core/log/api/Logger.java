package net.jplugin.core.log.api;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-8 ����12:24:05
 **/

public interface Logger {

	public abstract void debug(Object message, Throwable t);

	public abstract void debug(Object message);

	public abstract void error(Object message, Throwable t);

	public abstract void error(Object message);

	public abstract void fatal(Object message, Throwable t);

	public abstract void fatal(Object message);

	public abstract Level getLevel();

	public abstract void info(Object message, Throwable t);

	public abstract void info(Object message);

	public abstract boolean isDebugEnabled();

	public abstract boolean isEnabledFor(Priority level);

	public abstract boolean isInfoEnabled();

	public abstract boolean isTraceEnabled();

	public abstract void warn(Object message, Throwable t);

	public abstract void warn(Object message);

}