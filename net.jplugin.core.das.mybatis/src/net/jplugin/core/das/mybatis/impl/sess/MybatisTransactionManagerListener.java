package net.jplugin.core.das.mybatis.impl.sess;

import net.jplugin.core.ctx.api.ITransactionManagerListener;

public class MybatisTransactionManagerListener implements ITransactionManagerListener {

	@Override
	public void beforeBegin() {
		//����ʹ��clearSessions()�ᱨ���²���Ϊbegin�����лᵼ�����ӱ��ͷţ����session�����Ļ�
		//�ᵼ�º����sessionִ�б����ݿ����ӹرմ�
		//����ԭ����mybatisĿǰ���ǳ���Connection�����õģ���������оͺ���,δ�����ǸĽ�һ�¡�
		MybatisSessionManager.releaseSessions();
	}

	@Override
	public void afterBegin() {
	}

	@Override
	public void beforeCommit() {
		MybatisSessionManager.releaseSessions();
	}

	@Override
	public void afterCommit(boolean success) {
	}

	@Override
	public void beforeRollback() {
		MybatisSessionManager.releaseSessions();
	}

}
