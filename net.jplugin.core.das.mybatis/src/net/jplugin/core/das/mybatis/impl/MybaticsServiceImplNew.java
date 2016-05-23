package net.jplugin.core.das.mybatis.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.mybatis.api.IMapperHandlerForReturn;
import net.jplugin.core.das.mybatis.impl.sess.MybatisSessionManager;

public class MybaticsServiceImplNew implements IMybatisService {
	DataSource dataSource=null;
	SqlSessionFactory sqlSessionFactory;
	String theDataSourceName;
	
	public void init(String dataSourceName,List<String> mappers,List<Class> interceptors){
		theDataSourceName = dataSourceName;
		
		if (mappers==null || mappers.size()==0) {
			System.out.println("  No mappers configed.");
			return;
		}
		
//		managedDataSource = new TxManagedDataSource(DataSourceHolder.getInstance().getDataSource());
		
		dataSource = DataSourceFactory.getDataSource(dataSourceName);
//		ServiceFactory.getService(TransactionManager.class).addTransactionHandler(managedDataSource);

		//����txfactory�����ύ�����رգ�һ�н�������
		Properties prop = new Properties();
		prop.setProperty("closeConnection", "false");
		ManagedTransactionFactory tm = new ManagedTransactionFactory();
		tm.setProperties(prop);

		//create session factory
		Environment environment = new Environment("mybatis", tm, dataSource);
		Configuration configuration = new Configuration(environment);
		
		for (String c:mappers){
			try {
				if (c.endsWith(".xml")||c.endsWith(".XML")){
					 InputStream inputStream = Resources.getResourceAsStream(c);
			         XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, c, configuration.getSqlFragments());
			         mapperParser.parse();
				}else{
					//check xml file exists in classpath
					String tryFile = StringKit.replaceStr(c, ".", "/") + ".xml";
					boolean exists = false;
					InputStream stream = null;
					try {
						stream = this.getClass().getClassLoader().getResourceAsStream(tryFile);
						if (stream != null)
							exists = true;
					} finally {
						if (stream != null)try {stream.close();} catch (Exception e) {}
					}
					
					//load the mapper
					if (!exists)
						configuration.addMapper(Class.forName(c));
					else{
						 c = tryFile;
						 InputStream inputStream = Resources.getResourceAsStream(c);
				         XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, c, configuration.getSqlFragments());
				         mapperParser.parse();
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
//		MybatsiInterceptorManager.instance.installPlugins(configuration);
		if (interceptors!=null){
			for (Class clazz:interceptors){
				Interceptor incept;
				try {
					incept = (Interceptor) clazz.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("create the mybatis interceptor error: "+clazz.getName());
				}
				configuration.addInterceptor(incept);
			}
		}
		
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
	}
	
	/* (non-Javadoc)
	 * @see net.luis.plugin.das.mybatis.impl.IMybatisService#openSession()
	 */
	@Override
	public SqlSession openSession(){
//		return sqlSessionFactory.openSession();
		return MybatisSessionManager.getSession(this.theDataSourceName);
	}
	
	/* (non-Javadoc)
	 * @see net.luis.plugin.das.mybatis.impl.IMybatisService#runWithMapper(java.lang.Class, net.luis.plugin.das.mybatis.impl.IMapperHandler)
	 */
	@Override
	public <T> void runWithMapper(Class<T> type,IMapperHandler<T> handler){
		/**
		 * ��¼һ�£������sess.close()Ŀǰ�ǿղ�������ȫ����ɾȥ��
		 */
		SqlSession sess = openSession();
		try{
			T mapper = sqlSessionFactory.getConfiguration().getMapper(type, sess);
			handler.run(mapper);
		}finally{
			sess.close();
		}
	}

	@Override
	public <M,R> R returnWithMapper(Class<M> type,IMapperHandlerForReturn<M,R> handler){
		SqlSession sess = openSession();
		try{
			M mapper = sqlSessionFactory.getConfiguration().getMapper(type, sess);
			return handler.fetchResult(mapper);
		}finally{
			sess.close();
		}
	}

	
	/**
	 * ע�⣬�����ȡ��connection���ùرգ����Զ��رյ�
	 */
	@Override
	public Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T getMapper(Class<T> t) {
		return openSession().getMapper(t);
	}

	public SqlSession _openRealSession() {
		return sqlSessionFactory.openSession();
	}
}
