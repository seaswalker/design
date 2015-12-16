package crawler;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Recruitment;
import model.RecruitmentType;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;

public class BaseCrawler extends WebCrawler {
	
	/**
	 * 缩略信息页面(表格)，需要在此提取招聘会地点、时间
	 * 招聘信息栏目不需要提取此信息
	 */
	protected static final Pattern DIGEST = Pattern.compile("^http://career\\.sdust\\.edu\\.cn/servlet/CareerFairServlet\\?((flag=content&CFID=[0-9]+)|(CFID=[0-9]+&flag=content))$");
	/**
	 * 招聘会详细信息链接使用了特殊的域名
	 */
	protected static final Pattern DETAIL_FIX = Pattern.compile("^http://career\\.sdkd\\.net\\.cn/servlet/RecruitmentServlet\\?((ID=([0-9]+)&flag=content)|(flag=content&ID=([0-9]+)))$");
	/**
	 * 招聘会、招聘信息详细页面
	 * 需要在此提取公司名称、浏览量、单位地址、招聘专业以及薪资
	 */
	protected static final Pattern DETAIL = Pattern.compile("^http://career\\.sdust\\.edu\\.cn/servlet/RecruitmentServlet\\?((ID=([0-9]+)&flag=content)|(flag=content&ID=([0-9]+)))$");
	/**
	 * 企业名称
	 */
	private static final Pattern COMPANY_NAME = Pattern.compile("招聘企业：[ ]?(\\S+)");
	/**
	 * 阅读量
	 */
	private static final Pattern CLICKCOUNT = Pattern.compile("点击阅读量：([0-9]+)");
	/**
	 * 省市
	 */
	private static final Pattern ADDRESS_PROVINCE = Pattern.compile("单位所在地：(.+)省-(.+)市");
	/**
	 * 直辖市
	 */
	private static final Pattern ADDRESS_CITY = Pattern.compile("单位所在地：(.+)市");
	/**
	 * 自治区
	 */
	private static final Pattern ADDRESS_REGION = Pattern.compile("单位所在地：(.+自治区)-(.+)市");
	/**
	 * 提取发布日期
	 */
	private static final Pattern pattern = Pattern.compile("更新时间:(\\d{4})-(\\d{1,2})-(\\d{1,2})");
	private static final Logger logger = LoggerFactory.getLogger(BaseCrawler.class);
	/**
	 * 学校官网上含有上个学期(也就是上一级学生春招)的信息，所以在此只统计2015年8月1日以后的招聘信息
	 */
	protected static final Date DATE_THRESHOLD;
	
	static {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2015, 7, 1);
		DATE_THRESHOLD = calendar.getTime();
	}
	
	/**
	 * 提取详细页面id，示例:
	 * http://career.sdust.edu.cn/servlet/RecruitmentServlet?ID=4685&flag=content 即为4685
	 * @param url 链接
	 * @return
	 */
	protected static int extractDetailID(String url) {
		Matcher matcher = DETAIL.matcher(url);
		if (matcher.matches()) {
			return extractDetailID(matcher);
		} 
		matcher = DETAIL_FIX.matcher(url);
		if (matcher.matches()) {
			return extractDetailID(matcher);
		}
		return -1;
	}
	
	/**
	 * 提取详细页面id
	 * @param matcher 注意，此matcher必须是调用了matcher.matches()方法
	 * @return
	 */
	protected static int extractDetailID(Matcher matcher) {
		String str = matcher.group(3);
		if (str == null) {
			str = matcher.group(5);
		}
		return Integer.parseInt(str);
	}
	
	/**
	 *	在详细信息页面里面提取专业、单位地址、浏览量
	 * @param page
	 * @return
	 */
	protected static Recruitment extractDetail(Page page) {
		ParseData parseData = page.getParseData();
		if (parseData instanceof HtmlParseData) {
			HtmlParseData data = (HtmlParseData) parseData;
			Document doc = Jsoup.parse(data.getHtml());
			Elements divs = doc.select("div[class=content_bottom] div");
			String name = extractName(divs.get(2).text());
			if (RecruitmentHolder.exists(name)) {
				logger.debug("重复信息: " + page.getWebURL().getURL());
				return null;
			}
			String second = divs.get(2).text();
			if (extractPublishDate(second).before(DATE_THRESHOLD)) {
				logger.debug("过期信息: " + page.getWebURL().getURL());
			}
			Recruitment recruitment = new Recruitment();
			recruitment.setUrl(page.getWebURL().getURL());
			recruitment.setClickCount(extractClickCount(divs.get(1).text()));
			recruitment.setName(name);
			recruitment.setRecruitmentType(RecruitmentType.RECRUITMENTINFORMATION);
			extractAddress(second, recruitment);
			parseTokens(divs.get(3).text(), recruitment);
			return recruitment;
		}
		return null;
	}
	
	/**
	 * 提取公司名称
	 * @param str 示例: 招聘企业：青岛奥技科光学有限公司      面向专业：理-工      单位所在地：北京市
	 */
	private static String extractName(String str) {
		Matcher matcher = COMPANY_NAME.matcher(str);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}
	
	/**
	 * 从字符串中提取点击量
	 * @param str 示例: (发布者：校管理员       更新时间:2015-12-14 11:11:14        点击阅读量：217)
	 * @return
	 */
	private static int extractClickCount(String str) {
		Matcher matcher = CLICKCOUNT.matcher(str);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		}
		return 0;
	}
	
	/**
	 * 抽取发布日期
	 * @param str 示例: (发布者：校管理员       更新时间:2015-12-14 11:11:14        点击阅读量：217)
	 * @return
	 */
	private static Date extractPublishDate(String str) {
		Matcher matcher = pattern.matcher(str);
		Calendar calendar = Calendar.getInstance();
		if (matcher.find()) {
			calendar.set(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) - 1, Integer.parseInt(matcher.group(3)));
		}
		return calendar.getTime();
	}
	
	/**
	 * 提取单位所在地
	 * @param str 示例: 招聘企业：青岛奥技科光学有限公司      面向专业：理-工      单位所在地：山东省-青岛市
	 * @return
	 */
	private static void extractAddress(String str, Recruitment recruitment) {
		Matcher matcher;
		if (str.indexOf("省") > -1) {
			matcher = ADDRESS_PROVINCE.matcher(str);
			if (matcher.find()) {
				recruitment.setProvinceId(ProvinceHolder.getID(matcher.group(1)));
				recruitment.setCityId(CityHolder.getID(matcher.group(2)));
			}
		} else if (str.indexOf("自治区") > -1) {
			//自治区
			matcher = ADDRESS_REGION.matcher(str);
			if (matcher.find()) {
				recruitment.setProvinceId(ProvinceHolder.getID(matcher.group(1)));
				recruitment.setCityId(CityHolder.getID(matcher.group(2)));
			}
		} else {
			//直辖市
			matcher = ADDRESS_CITY.matcher(str);
			if (matcher.find()) {
				recruitment.setProvinceId(ProvinceHolder.getID(matcher.group(1)));
			}
		}
	}
	
	/**
	 * 统计关键词
	 * @param source
	 */
	private static void parseTokens(String source, Recruitment recruitment) {
		for (String token : TokenHolder.TOKENS) {
			if (source.indexOf(token) > -1) {
				recruitment.addToken(TokenHolder.addTokenCount(token));
			}
		}
	}
	
}
