package crawler;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Recruitment;

/**
 * 因为招聘会信息需要在摘要页面和详细信息页面两个页面中抓取，所以
 * 此类暂时保存Recruitment对象，以便两次在两次抓取过程中完善信息
 * @author skywalker
 *
 */
public abstract class RecruitmentHolder {

	/**
	 * 保存招聘会
	 */
	private static final HashMap<Integer, Recruitment> FAIR_HOLDER = new HashMap<Integer, Recruitment>();
	/**
	 * 招聘会/招聘信息容器
	 * 所有爬取到的数据都被存在这里，等到爬虫停止运行后统一存入数据库
	 * key为公司的名称，防止一个公司发布多个招聘信息从而导致统计信息不准确
	 * 如果一个公司既发布了招聘会信息又发布了招聘信息，那么以招聘会信息为准，先启动招聘会爬虫可以保证这一点
	 */
	private static final Map<String, Recruitment> RECRUITMENT_CONTAINER = new HashMap<String, Recruitment>();
	private static final Logger logger = LoggerFactory.getLogger(RecruitmentHolder.class);
	
	/**
	 * 更新招聘会
	 * @param id 详细页面的id，示例: url为http://career.sdust.edu.cn/servlet/RecruitmentServlet?flag=content&ID=4685
	 * 那么id即为4685
	 * @param recruitment 招聘会
	 */
	public static void updateFair(int id, Recruitment recruitment) {
		if (recruitment == null) return;
		//是否需要存入RECRUITMENT_CONTAINER
		boolean store = false;
		Recruitment old;
		synchronized (FAIR_HOLDER) {
			old = FAIR_HOLDER.get(id);
			if (old == null) {
				FAIR_HOLDER.put(id, old = recruitment);
				recruitment.addModify();
			} else {
				mergeRecruitment(old, recruitment);
				//招聘
				if (old.addModify() == 2) {
					store = true;
				}
			}
		}
		if (store) {
			saveRecruitmentInfo(old);
		}
	}
	
	/**
	 * 保存招聘信息
	 * @param recruitment
	 */
	public static void saveRecruitmentInfo(Recruitment recruitment) {
		synchronized (RECRUITMENT_CONTAINER) {
			RECRUITMENT_CONTAINER.put(recruitment.getName(), recruitment);
			logger.debug("保存: " + recruitment);
		}
	}
	
	/**
	 * 公司是否已被收录
	 * @param name 公司名称
	 * @return
	 */
	public static boolean exists(String name) {
		synchronized (RECRUITMENT_CONTAINER) {
			return RECRUITMENT_CONTAINER.containsKey(name);
		}
	}
	
	/**
	 * 把from中的数据merger进入to
	 * @param to
	 * @param from
	 */
	private static void mergeRecruitment(Recruitment to, Recruitment from) {
		if (from.getCityId() > 0) {
			to.setCityId(from.getCityId());
		}
		if (from.getProvinceId() > 0) {
			to.setProvinceId(from.getProvinceId());
		}
		if (from.getClickCount() > 0) {
			to.setClickCount(from.getClickCount());
		}
		if (!StringUtil.isBlank(from.getName())) {
			to.setName(from.getName());
		}
		if (from.getRecruitmentType() != null) {
			to.setRecruitmentType(from.getRecruitmentType());
		}
		if (from.getTime() != null) {
			to.setTime(from.getTime());
		}
		if (from.getBuilding() > 0) {
			to.setBuilding(from.getBuilding());
		}
		if (!StringUtil.isBlank(from.getUrl())) {
			to.setUrl(from.getUrl());
		}
		if (from.getTokens().size() > 0) {
			to.setTokens(from.getTokens());
		}
	}
	
	/**
	 * 把所有的Recruitment存入数据库
	 */
	public static void flushIntoDatabase() {
		//TODO 特殊处理: 和token的第三章表关联关系
	}
	
}
