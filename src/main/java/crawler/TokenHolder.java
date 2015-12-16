package crawler;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Token;
import dao.TokenDao;

/**
 * 辅助统计关键词出现次数
 * @author skywalker
 *
 */
public abstract class TokenHolder {

	private static final TokenDao TOKEN_DAO = TokenDao.getInstance();
	private static final Map<String, Token> TOKEN_MAP = TOKEN_DAO.getTokenMap();
	/**
	 * 不可变的关键词列表
	 */
	public static final Set<String> TOKENS = Collections.unmodifiableSet(TOKEN_MAP.keySet());
	private static final Logger logger = LoggerFactory.getLogger(TokenHolder.class);
	
	/**
	 * 指定的关键词出现次数加一
	 * @param token 关键词
	 * @param 返回对应的Token的id
	 */
	public static int addTokenCount(String token) {
		synchronized (TOKEN_MAP) {
			Token t = TOKEN_MAP.get(token);
			t.addCount();
			return t.getId();
		}
	}
	
	/**
	 * 存入数据库
	 */
	public static void flushIntoDatabase() {
		String sql = "update token set count = ? where id = ?";
		Integer[][] params = new Integer[TOKENS.size()][];
		int index = 0;
		for (String key : TOKENS) {
			Token token = TOKEN_MAP.get(key);
			Integer[] param = new Integer[2];
			param[0] = token.getCount();
			param[1] = token.getId();
			params[index++] = param;
		}
		try {
			TOKEN_DAO.batchUpdate(sql, params);
			logger.debug("token存入数据库完成");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	
}
