package net.jplugin.core.log.impl;

import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;

/**
 * <pre>
 * ����ӿ�����ʵ�� ��commonLoggerImplû�г�ʼ���õ�����£�����StatLogger�ӿڽ�����־��¼��
 * ��Log4j��ʼ���Ժ󣬰����е����Loggerת��commonLoggerImpl��Logger,
 * ����commonLoggerImpl��Ŀǰ��Logger4j��û�г�ʼ����ʱ�򣬵�����־ʧ�ܡ�
 * </pre>
 * 
 * @author Administrator
 *
 */
public class BridgedLoggerService implements ILogService {
	private static final int MAX_LOGGERS_BEF_COMMON_LOG_START = 100000;
	LogServiceImpl commonLoggerImpl;
	List<BridgedLoggerImpl> bridgeLoggers = new ArrayList<BridgedLoggerImpl>();

	/**
	 * ��ʼ����ͨ��logService,��־������¼��log4j
	 */
	public void initCommonLoggerService(){
		synchronized (bridgeLoggers) {
			commonLoggerImpl = new LogServiceImpl();
			/**
			 * ���Ѿ���ȡ��logger����һ��
			 */
			for (BridgedLoggerImpl log:bridgeLoggers){
				log.changeToCommonLogger(commonLoggerImpl);
			}
			/**
			 * ����list
			 */
			bridgeLoggers.clear();
			bridgeLoggers = null;
		}
	}
	
	public Logger getLogger(String name) {
		if (commonLoggerImpl != null) {
			return commonLoggerImpl.getLogger(name);
		} else {
			synchronized (bridgeLoggers) {
				BridgedLoggerImpl l = new BridgedLoggerImpl(name);
				bridgeLoggers.add(l);
				if (bridgeLoggers.size()>=MAX_LOGGERS_BEF_COMMON_LOG_START){
					throw new RuntimeException("Too many loggers created before Common Logger Start");
				}
				return l;
			}
		}
	}

	public Logger getSpecicalLogger(String filename) {
		if (commonLoggerImpl != null)
			return commonLoggerImpl.getSpecicalLogger(filename);
		else
			throw new RuntimeException("Logger service not inited yet!");
	}

	public static class BridgedLoggerImpl implements Logger {
		StartLoggerImpl startLogger;
		Logger commonLogger;
		
		void changeToCommonLogger(LogServiceImpl s){
			commonLogger = s.getLogger(this.startLogger.name);
			//�ͷ��ڴ�
			startLogger = null;
		}

		
		BridgedLoggerImpl(String name) {
			this.startLogger = new StartLoggerImpl(name);
		}

		private Logger get() {
			return commonLogger == null ? startLogger : commonLogger;
		}

		public void debug(Object message, Throwable t) {
			get().debug(message, t);
		}

		public void debug(Object message) {
			get().debug(message);
		}

		public void error(Object message, Throwable t) {
			get().error(message, t);
		}

		public void error(Object message) {
			get().error(message);
		}

		public void fatal(Object message, Throwable t) {
			get().fatal(message, t);
		}

		public void fatal(Object message) {
			get().fatal(message);
		}

		public Level getLevel() {
			return get().getLevel();
		}

		public void info(Object message, Throwable t) {
			get().info(message, t);
		}

		public void info(Object message) {
			get().info(message);
		}

		public boolean isDebugEnabled() {
			return get().isDebugEnabled();
		}

		public boolean isEnabledFor(Priority level) {
			return get().isEnabledFor(level);
		}

		public boolean isInfoEnabled() {
			return get().isInfoEnabled();
		}

		public boolean isTraceEnabled() {
			return get().isTraceEnabled();
		}

		public void warn(Object message, Throwable t) {
			get().warn(message, t);
		}

		public void warn(Object message) {
			get().warn(message);
		}
	}

	public static class StartLoggerImpl implements Logger {
		String name;

		public StartLoggerImpl(String n) {
			this.name = n;
		}

		public void debug(Object message, Throwable t) {
			PluginEnvirement.INSTANCE.getStartLogger().log(message, t);
		}

		public void debug(Object message) {
			PluginEnvirement.INSTANCE.getStartLogger().log(message);
		}

		public void error(Object message, Throwable t) {
			PluginEnvirement.INSTANCE.getStartLogger().log(message, t);
		}

		public void error(Object message) {
			PluginEnvirement.INSTANCE.getStartLogger().log(message);
		}

		public void fatal(Object message, Throwable t) {
			PluginEnvirement.INSTANCE.getStartLogger().log(message, t);
		}

		public void fatal(Object message) {
			PluginEnvirement.INSTANCE.getStartLogger().log(message);
		}

		public Level getLevel() {
			throw new RuntimeException("Start Logger impl not supported!");
		}

		public void info(Object message, Throwable t) {
			PluginEnvirement.INSTANCE.getStartLogger().log(message, t);
		}

		public void info(Object message) {
			PluginEnvirement.INSTANCE.getStartLogger().log(message);
		}

		public boolean isDebugEnabled() {
			throw new RuntimeException("Start Logger impl not supported!");
		}

		public boolean isEnabledFor(Priority level) {
			throw new RuntimeException("Start Logger impl not supported!");
		}

		public boolean isInfoEnabled() {
			throw new RuntimeException("Start Logger impl not supported!");
		}

		public boolean isTraceEnabled() {
			throw new RuntimeException("Start Logger impl not supported!");
		}

		public void warn(Object message, Throwable t) {
			PluginEnvirement.INSTANCE.getStartLogger().log(message, t);
		}

		public void warn(Object message) {
			PluginEnvirement.INSTANCE.getStartLogger().log(message);
		}
	}

}
