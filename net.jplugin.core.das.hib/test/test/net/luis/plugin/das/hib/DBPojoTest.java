package test.net.luis.plugin.das.hib;

import net.jplugin.core.das.hib.api.Entity;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-5 ����06:14:53
 **/

@Entity(idgen="uuid.hex")
public class DBPojoTest {
	String id;
	String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
