package net.jplugin.ext.dict.api;

import java.util.List;

public interface IDictProvidor {
	/**
	 * �Ƿ�ʹ�û���
	 * @return
	 */
	public boolean dynamic();
	/**
	 * ע�⣺dynamic==true���ſ��Դ���param
	 * @param param
	 * @return
	 */
	public List<Dictionary> get(String param);

}
