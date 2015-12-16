package crawler;

import java.util.Map;

import dao.CityDao;

public abstract class CityHolder {

	private static final CityDao CITY_DAO = CityDao.getInstance();
	private static final Map<String, Integer> CITIES = CITY_DAO.getCityMap();
	
	/**
	 * 获取对应的城市id
	 * @param str 示例: 青岛市
	 * @return
	 */
	public static int getID(String str) {
		return CITIES.get(str);
	}
	
}
