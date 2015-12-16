package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import db.ConnectionManager;

/**
 * 地级市据库操作
 * @author skywalker
 *
 */
public class CityDao {

	private static final CityDao INSTANCE = new CityDao();
	
	public static CityDao getInstance() {
		return INSTANCE;
	}
	
	private CityDao() {}
	
	/**
	 * 查询所有的地级市
	 * @return key: 地级市名称, value: 地级市id
	 */
	public Map<String, Integer> getCityMap() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		String sql = "select * from city";
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
