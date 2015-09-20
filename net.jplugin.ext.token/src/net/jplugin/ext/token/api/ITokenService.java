package net.jplugin.ext.token.api;

import java.util.Map;
import java.util.Set;

import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.ctx.api.Rule.TxType;


public interface ITokenService {

	/**
	 * һ��identifier��һ��realm��ֻ����һ��token���ڣ����洴����token�����Զ�������ǰ�����ġ�
	 * @param tkInfo
	 * @param identifier
	 * @param realm
	 * @return
	 */
	@Rule(methodType=TxType.REQUIRED)
	public String createToken(Map<String,String> tkInfo,String identifier,String realm);
	/**
	 * ���key�����ڣ��׳��쳣
	 * @param tk
	 * @param info
	 */
	@Rule(methodType=TxType.REQUIRED)
	public void putTokenInfo(String tk,Map<String,String> info); 
	/**
	 * ���key�����ڣ��׳��쳣
	 * @param tk
	 * @param keys
	 */
	@Rule(methodType=TxType.REQUIRED)
	public void removeTokenInfo(String tk,Set<String> keys);
	
	/**
	 * ���key�����ڣ�����null�����key���ڵ�������Ϣ�����ؿ�map
	 * @param tk
	 * @return
	 */
	@Rule
	public Map<String,String> validAndGetTokenInfo(String tk);
	
	@Rule
	public Map<String,String> getTokenInfo(String tk);
	
	@Rule(methodType=TxType.REQUIRED)
	public void removeToken(String tk);
	
}
