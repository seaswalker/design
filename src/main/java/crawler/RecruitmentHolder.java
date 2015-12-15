package crawler;

import java.util.HashMap;

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
	private static final Logger logger = LoggerFactory.getLogger(RecruitmentHolder.class);
	
	/**
	 * 更新招聘会
	 * @param id 详细页面的id，示例: url为http://career.sdust.edu.cn/servlet/RecruitmentServlet?flag=content&ID=4685
	 * 那么id即为4685
	 * @param recruitment 招聘会
	 */
	public static void updateFair(int id, Recruitment recruitment) {
		//是否需要存入数据库
		boolean flush = false;
		synchronized (FAIR_HOLDER) {
			Recruitment old = FAIR_HOLDER.get(id);
			if (old == null) {
				FAIR_HOLDER.put(id, recruitment);
				recruitment.addModify();
			} else {
				mergeRecruitment(old, recruitment);
				//招聘
				if (old.addModify() == 2) {
					flush = true;
				}
			}
		}
		if (flush) {
			flushIntoDatabase(id);
		}
	}
	
	/**
	 * 保存招聘信息
	 * @param recruitment
	 */
	public static void saveRecruitmentInfo(Recruitment recruitment) {
		flushIntoDatabase(recruitment);
	}
	
	/**
	 * 把from中的数据merger进入to
	 * @param to
	 * @param from
	 */
	private static void mergeRecruitment(Recruitment to, Recruitment from) {
		if (from.getAddress() != null) {
			to.setAddress(from.getAddress());
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
		if (!StringUtil.isBlank(from.getRoom())) {
			to.setRoom(from.getRoom());
		}
		if (from.getSalary() > 0) {
			to.setSalary(from.getSalary());
		}
	}
	
	/**
	 * 把指定的Recruitment存入数据库
	 * @param id Recruitment
	 */
	private static void flushIntoDatabase(int id) {
		Recruitment recruitment = FAIR_HOLDER.get(id);
		if (recruitment != null) {
			FAIR_HOLDER.remove(id);
			//TODO 存入数据库
			logger.debug(recruitment + "存入数据库");
		}
	}
	
	/**
	 * 直接把招聘信息存入数据库
	 * @param recruitment
	 */
	private static void flushIntoDatabase(Recruitment recruitment) {
		//TODO
		logger.debug(recruitment + "存入数据库成功");
	}
	
}
