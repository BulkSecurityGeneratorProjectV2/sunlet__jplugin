package net.jplugin.core.kernel.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.StringKit;

/**
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-22 ����11:43:12
 **/

public abstract class AbstractPlugin implements IPlugin {

	private List<ExtensionPoint> extensionPoints = new ArrayList<ExtensionPoint>();
	private List<Extension> extensions = new ArrayList<Extension>();
	private String pluginName = this.getClass().getName();
	private int status = IPlugin.STAT_INIT;
	private Hashtable<String,String> configreus = new Hashtable<String, String>();


	
	public void addExtensionPoint(ExtensionPoint ep) {
		this.extensionPoints.add(ep);
	}

	public void addExtension(Extension e) {
		this.extensions.add(e);
	}

	public void addConfigure(String name,String val){
		if (this.configreus.containsKey(name)){
			throw new RuntimeException("duplicate config name:"+name);
		}
		this.configreus.put(name,val);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.luis.common.kernal.IPlugin#getExtensionPoints()
	 */
	public List<ExtensionPoint> getExtensionPoints() {
		return this.extensionPoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.luis.common.kernal.IPlugin#getExtensions()
	 */
	public List<Extension> getExtensions() {
		return this.extensions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.luis.common.kernal.IPlugin#getName()
	 */
	public String getName() {
		return this.pluginName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.luis.common.kernal.IPlugin#getPrivority()
	 */
	public abstract int getPrivority();

	public int getStatus() {
		return this.status;
	}

	public List<PluginError> load() {
		List<PluginError> errList = null;
		// ������ǳ�ʼ״̬���쳣
		if (this.status != IPlugin.STAT_INIT)
			throw new RuntimeException(
					"Not init state,can't call load,plugin name:"
							+ this.getName());

		//�������
		for (int i = 0; i < this.extensions.size(); i++) {
			try {
				this.extensions.get(i).load();
			} catch (Exception e) {
				if (errList==null){
					errList = new ArrayList<PluginError>();
				}
				errList.add(new PluginError(this.getName(), "extension load error."+this.extensions.get(i).getClazz(),e));
			}
		}
		return errList;
	}

	/**
	 * ���plugin�Ȱ�ExtensionPoint�ŵ����У���ȥ����Extension�����Ա���Plugin֮�佻�����ã�
	 * plugin1�е���չ����plugin2�е���չ�㣬ͬʱ
	 * plugin2�е���չ����plugin1�е���չ��
	 * @param pluginRegistry
	 * @param errorList 
	 */
	public void wire(PluginRegistry pluginRegistry, List<PluginError> errorList) {
		for (int i=0;i<this.getExtensionPoints().size();i++){
			ExtensionPoint ep = this.getExtensionPoints().get(i);
			pluginRegistry.getExtensionPointMap().put(ep.getName(), ep);
		}
		
		for (int i=0;i<this.getExtensions().size();i++){
			Extension e = this.getExtensions().get(i);
			String pname = e.getExtensionPointName();
			ExtensionPoint point = pluginRegistry.getExtensionPointMap().get(pname);
			
			if (point ==null){
//				throw new RuntimeException("Shoudn't be null ,because have valided! "+pname);
				errorList.add(new PluginError(this.getName(),"Couldn't find extension point for extension ,perhaps the extensionpoint plugin is not load correctly.extension= "+e.getName() +" pointname="+pname));
			}else{
				if (point.validExtensionName(e.getName())){
					point.addExtension(e);
				}else{
					errorList.add(new PluginError(this.getName(),"The extension name must be unique and notnull. extension= "+e.getName() +" pointname="+pname));
				}
			}
		}
	}

	
	/**
	 * @param pluginRegistry
	 * @param errorList 
	 */
	public List<PluginError> valid(PluginRegistry pluginRegistry) {
		List<PluginError> errors = new ArrayList<PluginError>();
		//���point
		for (ExtensionPoint ep:this.extensionPoints){
			if (StringKit.isNull(ep.getName())){
				errors.add(new PluginError(this.getName(),"extension point name is null"));
			}
			ExtensionPoint finder = pluginRegistry.getExtensionPointMap().get(ep.getName());
			if (finder!=null){
				errors.add(new PluginError(this.getName(),"point name duplicated with old points:"+ep.getName()));
			}
			
			pluginRegistry.getExtensionPointMap().put(ep.getName(), ep);
		}
		//���extension�Ĺ�������extensionpoint���棬˳���ܵߵ�
		for (Extension e:this.extensions){
			String pname = e.getExtensionPointName();
			ExtensionPoint finder = pluginRegistry.getExtensionPointMap().get(pname);
			if (finder == null){
				errors.add(new PluginError(this.getName(),"can't find extension point for:"+e.getName() +" pointname="+pname));
				continue;
			}
			
			if(! ReflactKit.isTypeOf(e.getClazz(),finder.getExtensionClass())){
				errors.add(new PluginError(this.getName(),"The extension is not sub class of the point required. extClass="+e.getClazz()+" required="+finder.getExtensionClass()));
			}
			
			if (e.getClass().equals(String.class)){
				if (e.getProperties().size()!=1){
					errors.add(new PluginError(this.getName(),"String type extension must has one property with the val."+e.getName() +" pointname="+pname));
				}
			}
		}
		//����Ŀǰ�����������ļ������Բ��ü��extension��point�е���Ĵ������ˡ�
		return errors;
	}

	/**
	 * @param statError
	 */
	public void setStatus(int st) {
		this.status = st;
	}
	
	
	public Map<String,String> getConfigures(){
		return this.configreus;
	}
	
	public void onDestroy() {
	}
}
