package net.jplugin.core.das.api;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-8 ����11:16:58
 **/

public interface IResultDisposer {
	void readRow(ResultSet rs) throws SQLException;
}
