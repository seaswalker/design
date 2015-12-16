package crawler;

import java.util.Map;

import dao.BuildingDao;

public abstract class BuildingHolder {

	private static final BuildingDao BUILDING_DAO = BuildingDao.getInstance();
	private static final Map<String, Integer> BUILDINGS = BUILDING_DAO.getBuildingMap();
	
	/**
	 * 获取对应的教学楼id
	 * @param str 示例: J1
	 * @return
	 */
	public static int getID(String str) {
		return BUILDINGS.get(str);
	}
	
}
