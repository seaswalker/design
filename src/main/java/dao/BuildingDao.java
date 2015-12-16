package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import db.ConnectionManager;

/**
 * 教学楼数据库操作
 * @author skywalker
 *
 */
public class BuildingDao {

	private static final BuildingDao INSTANCE = new BuildingDao();
	
	public static BuildingDao getInstance() {
		return INSTANCE;
	}
	
	private BuildingDao() {}
	
	/**
	 * 查询所有的教学楼
	 * @return key: 教学楼名称, value: 教学楼id
	 */
	public Map<String, Integer> getBuildingMap() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		String sql = "select * from building";
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
