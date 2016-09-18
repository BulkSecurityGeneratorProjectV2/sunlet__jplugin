package test.net.jplugin.core.das.route;

import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;

public class Plugin extends AbstractPluginForTest {

	public Plugin(){
		//�Ѿ�������������Դ "database","db_2";
		ExtensionDasHelper.addDataSourceExtension(this,"router-db" , "router-db");
		ExtensionDasHelper.addDataSourceExtension(this,"router-ds-1" , "router-ds-1");
		ExtensionDasHelper.addDataSourceExtension(this,"router-ds-2" , "router-ds-2");
	}
	
	@Override
	public void test() throws Throwable {
		DbCreate.create();
		new InsertSelectTest().test();
		DbCreate.create();
		new UpdateTest().test();
		DbCreate.create();
		new DeleteTest().test();
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS_TS+1;
	}

}
