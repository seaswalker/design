package crawler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * 招聘会爬虫
 * @author skywalker
 *
 */
public class RecruitmentFairCrawler extends BaseCrawler {
	
	private static final String DETAIL_PREFIX = "http://career.sdust.edu.cn/servlet/RecruitmentServlet";
	private static final String DETAIL_PREFIX_FIX = "http://career.sdkd.net.cn/servlet/RecruitmentServlet";
	private static final String DIGEST_PREFIX = "http://career.sdust.edu.cn/servlet/CareerFairServlet";
	private static final Logger logger = LoggerFactory.getLogger(RecruitmentFairCrawler.class);
	
	@Override
	public boolean shouldVisit(Page page, WebURL url) {
		String href = url.getURL();
		return href.startsWith(DIGEST_PREFIX) || (href.startsWith(DETAIL_PREFIX_FIX) && href.indexOf("main") == -1)
				|| (href.startsWith(DETAIL_PREFIX) && href.indexOf("main") == -1);
	}
	
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		//翻页
		if (url.indexOf("index") > -1) return;
		Matcher matcher = DIGEST.matcher(url);
		if (matcher.matches()) {
			Recruitment recruitment = new Recruitment();
			int detailId = extractDigest(page, recruitment);
			if (detailId > 0) {
				RecruitmentHolder.updateFair(detailId, recruitment);
			}
			return;
		} 
		matcher = DETAIL.matcher(url);
		if (matcher.matches()) {
			int detailId = extractDetailID(matcher);
			if (detailId > 0) {
				RecruitmentHolder.updateFair(detailId, extractDetail(page));
			}
			return;
		}
		matcher = DETAIL_FIX.matcher(url);
		if (matcher.matches()) {
			int detailId = extractDetailID(matcher);
			if (detailId > 0) {
				RecruitmentHolder.updateFair(detailId, extractDetail(page));
			}
			return;
		}
	}
	
	/**
	 * 从摘要页面中抽取招聘会时间和地点
	 * @param page 页面
	 * @param recruitment 将解析到的数据存入
	 * @return 返回招聘会id
	 * @throws ParseException 
	 */
	private static int extractDigest(Page page, Recruitment recruitment) {
		recruitment.setRecruitmentType(RecruitmentType.RECRUITMENTFAIR);
		ParseData parseData = page.getParseData();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (parseData instanceof HtmlParseData) {
			HtmlParseData data = (HtmlParseData) parseData;
			Document doc = Jsoup.parse(data.getHtml());
			//id为body_1的div下面共有两个table，第一个是当前位置指示
			Elements tds = doc.select("#body_1 table").get(1).select("td");
			String name = tds.get(7).text();
			if (RecruitmentHolder.exists(name)) {
				logger.debug("重复信息: " + page.getWebURL().getURL());
				return -1;
			}
			String timeStr = tds.get(3).text();
			//解析时间
			try {
				recruitment.setTime(dateFormat.parse(timeStr));
				if (recruitment.getTime().before(DATE_THRESHOLD)) {
					logger.debug("过期信息: " + page.getWebURL().getURL());
					return -1;
				}
			} catch (ParseException e) {
				logger.warn("时间字符串: " + timeStr + "解析失败");
			}
			//解析招聘会地点
			recruitment.setBuilding(extractBuilding(tds.get(5).text()));
			//解析公司名称
			recruitment.setName(name);
			String url = tds.get(13).text();
			return extractDetailID(url);
		}
		logger.debug("转至学院官网的信息: " + page.getWebURL().getURL());
		return -1;
	}
	
	/**
	 * 提取招聘会地点
	 * @param str 示例: J1-211室
	 */
	private static int extractBuilding(String str) {
		Pattern pattern = Pattern.compile("[JS]\\d{1,2}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			return BuildingHolder.getID(matcher.group());
		}
		return -1;
	}
	
}
