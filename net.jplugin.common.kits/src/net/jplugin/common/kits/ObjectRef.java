package net.jplugin.common.kits;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-9 ����08:48:01
 **/

public class ObjectRef<T> {
	T obj;
	public T get(){
		return obj;
	}
	public void set(T o){
		this.obj = o;
	}
}
