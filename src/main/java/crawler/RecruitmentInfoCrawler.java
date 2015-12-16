package crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * 招聘信息爬虫
 * @author skywalker
 *
 */
public class RecruitmentInfoCrawler extends BaseCrawler {
	
	private static final String PREFIX = "http://career.sdust.edu.cn/servlet/RecruitmentServlet";
	
	@Override
	public boolean shouldVisit(Page page, WebURL url) {
		return url.getURL().startsWith(PREFIX);
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		if (url.indexOf("index") == -1) {
			RecruitmentHolder.saveRecruitmentInfo(extractDetail(page));
		}
	}
	
}
