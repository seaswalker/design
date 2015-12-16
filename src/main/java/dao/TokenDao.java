package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.ConnectionManager;
import model.Token;

/**
 * 关键词数据库操作
 * @author skywalker
 *
 */
public class TokenDao {

	private static final TokenDao INSTANCE = new TokenDao();
	private static final Logger logger = LoggerFactory.getLogger(TokenDao.class);
	
	public static TokenDao getInstance() {
		return INSTANCE;
	}
	
	private TokenDao() {}
	
	/**
	 * 获取关键词map key: 关键词 value: Token对象
	 * @return 
	 */
	public Map<String, Token> getTokenMap() {
		Connection con = ConnectionManager.getConnection();
		Map<String, Token> map = new HashMap<String, Token>();
		String sql = "select * from token";
		try {
			new QueryRunner().query(con, sql, new ResultSetHandler<Void>() {
				@Override
				public Void handle(ResultSet rs) throws SQLException {
					while (rs.next()) {
						Token token = new Token();
						token.setId(rs.getInt("id"));
						token.setName(rs.getString("name"));
						token.setCount(rs.getInt("count"));
						map.put(token.getName(), token);
					}
					return null;
				}
			});
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return map;
	}
	
	/**
	 * 批量更新
	 * @param sqls sql语句, 比如update token set count = ? where id = ?
	 * @param params 二维数组，第一维是执行的次数，第二维是需要设置的参数
	 * @throws SQLException 
	 */
	public void batchUpdate(String sql, Object[][] params) throws SQLException {
		Connection con = ConnectionManager.getConnection();
		new QueryRunner().batch(con, sql, params);
	}
	
}
