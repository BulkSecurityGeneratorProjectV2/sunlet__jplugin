package net.jplugin.core.kernel.api;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.StringKit;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-22 ����11:43:28
 **/

public class ExtensionPoint {
	String name;
	Class<?> extensionClass;
	boolean extensionNameReqiredAndUnique;
	List<Extension> extensions;
	Object[] extensionObjects;
	Map<String,Object> extensionMap;
	
	public static ExtensionPoint create(String aName,Class<?> clazz){
		return create(aName, clazz,false);
	}
	
	public static ExtensionPoint create(String aName,Class<?> clazz,boolean aNameRaU){
		return new ExtensionPoint(aName,clazz,aNameRaU);
	}


	
	private ExtensionPoint(String aName,Class<?> clazz,boolean aNameRaU){
		this.name = aName;
		this.extensionClass = clazz;
		this.extensions = new ArrayList<Extension>();
		this.extensionNameReqiredAndUnique = aNameRaU;
	}
	
	
	/**
	 * ����׶�
	 * @return
	 */
	public String getName(){
		return name;
	}
	/**
	 * ����׶�
	 * @return
	 */
	public Class<?> getExtensionClass(){
		return extensionClass;
	}
	
	/**
	 * ����׶�
	 * @return
	 */
	public boolean extensionNameReqiredAndUnique(){
		return this.extensionNameReqiredAndUnique;
	}

	/**
	 * ���н׶�
	 * @return
	 */
	public List<Extension> getExtensions(){
		return this.extensions;
	}
	
	/**
	 * @param e
	 */
	public void addExtension(Extension e) {
		this.extensions.add(e);
	}
	public void findExtensionObjectByName(String nm){
		Object find = null;
		for (Extension e:this.extensions){
			if (nm.equals(e.getName())){
				if (find == null){
					find = e.getObject();
				}else{
					throw new RuntimeException("find duplicate object with name:"+nm);
				}
			}
		}
	}
	/**
	 * ���н׶�
	 * @param <T>
	 * @param t
	 * @return
	 */
	public synchronized <T> T[] getExtensionObjects(Class <T> t){
		if (this.extensionObjects==null){
			this.extensionObjects = (Object[]) Array.newInstance(extensionClass, this.extensions.size());
			for (int i=0;i<this.extensionObjects.length;i++){
				this.extensionObjects[i] = this.extensions.get(i).getObject();
			}
		}
		return (T[]) this.extensionObjects;
	}
	
	public synchronized  Map<String,Object> getExtensionMap(){
		if (!this.extensionNameReqiredAndUnique){
			throw new RuntimeException("can't call getExtensionMap when extensionNameReqiredAndUnique is false");
		}
		
		if (this.extensionMap==null){
			this.extensionMap = new HashMap<String, Object>();
		
			for (Extension e:this.extensions){
				this.extensionMap.put(e.getName(), e.getObject());
			}
		}
		return this.extensionMap;
	}

	/**
	 * @param name2
	 * @return
	 */
	public boolean validExtensionName(String nm) {
		if (!this.extensionNameReqiredAndUnique){
			return true;
		}
		if (StringKit.isNull(nm)){
			return false;
		}
		
		for (Extension e:this.extensions){
			if (nm.equals(e.getName())){
				return false;
			}
		}
		
		return true;
		
	}

	
}
