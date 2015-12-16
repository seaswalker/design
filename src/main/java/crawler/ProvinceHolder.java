package crawler;

import java.util.Map;

import dao.ProvinceDao;

public abstract class ProvinceHolder {

	private static final ProvinceDao PROVINCE_DAO = ProvinceDao.getInstance();
	private static final Map<String, Integer> PROVINCES = PROVINCE_DAO.getProvinceMap();
	
	/**
	 * 获取对应的省份id
	 * @param str 示例: 山东省
	 * @return
	 */
	public static int getID(String str) {
		return PROVINCES.get(str);
	}
	
}
