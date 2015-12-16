package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import db.ConnectionManager;

/**
 * 省份数据库操作
 * @author skywalker
 *
 */
public class ProvinceDao {

	private static final ProvinceDao INSTANCE = new ProvinceDao();
	
	public static ProvinceDao getInstance() {
		return INSTANCE;
	}
	
	private ProvinceDao() {}
	
	/**
	 * 查询所有的省份
	 * @return key: 省份名称, value: 省份id
	 */
	public Map<String, Integer> getProvinceMap() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		String sql = "select * from province";
		try {
			new QueryRunner().query(ConnectionManager.getConnection(), sql, new ResultSetHandler<Void>() {
				@Override
				public Void handle(ResultSet rs) throws SQLException {
					while (rs.next()) {
						String name = rs.getString("name");
						int id = rs.getInt("id");
						map.put(name, id);
					}
					return null;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
