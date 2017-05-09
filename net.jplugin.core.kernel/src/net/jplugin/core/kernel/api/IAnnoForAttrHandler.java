package net.jplugin.core.kernel.api;

public interface IAnnoForAttrHandler <T> {
	/**
	 * ��Ǹ�Handler��Ӧ��AnnoClass
	 * @return
	 */
	public Class<T> getAnnoClass();
	
	/**
	 * ������У�飬��Ӧ��annotation��Ӧ��Щ��������
	 * @return
	 */
	public Class getAttrClass();

	/**
	 * ��ȡֵ
	 * @param fieldType Attr��ʵ������
	 * @param anno  anno����
	 * @return 
	 */
	public Object  getValue(Object theObject,Class fieldType,T anno);
}
