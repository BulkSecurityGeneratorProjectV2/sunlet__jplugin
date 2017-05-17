package net.jplugin.core.kernel.kits;

import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class KernelKit {
	private static final String DEFAULT_ENV = "develop";

	public static String getConfigFilePath(String filename){
		//���Ҳ���env��ȱʡ�ļ�
		String path = getFileName("",filename);
		if (FileKit.existsAndIsFile(path)){
			return path;
		}
		//��env�ļ������û����������ȱʡenv
		String envtype = PluginEnvirement.INSTANCE.getEnvType();
		if (envtype==null){
			envtype=DEFAULT_ENV;
		}
		path = getFileName(envtype,filename);
		if (FileKit.existsAndIsFile(path)){
			return path;
		}else{
			//ͨ�����������Ҳ����ļ���Ҳ�Ҳ���develop��ʹ��Ĭ��ֵ��Ӧ���Ҳ����ļ��ᱨ��ġ�
			return getFileName("",filename);
		}
	}
	private static String getFileName(String env, String filename) {
		if (StringKit.isNull(env)) 
			return PluginEnvirement.getInstance().getConfigDir()+"/"+filename;
		else
			return PluginEnvirement.getInstance().getConfigDir()+"/"+env+"/"+filename;
	}
	
	private static String getEnvType(){
		return System.getProperty("plugin.env");
	}
}
