package crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * 爬虫控制器
 * @author skywalker
 *
 */
public class Controller {

	/**
	 * 数据存储路径
	 */
	private static final String PATH = "/data";
	/**
	 * 线程数目
	 */
	private static final int THREADS_COUNT = 4;
	
	public static void main(String[] args) {
		/*CrawlController controller = startCrawler("http://career.sdust.edu.cn/servlet/RecruitmentServlet?flag=main", RecruitmentInfoCrawler.class);
		controller.waitUntilFinish();*/
		CrawlController controller = startCrawler("http://career.sdust.edu.cn/servlet/CareerFairServlet?flag=main", RecruitmentFairCrawler.class);
		controller.waitUntilFinish();
		TokenHolder.flushIntoDatabase();
		RecruitmentHolder.flushIntoDatabase();
	}
	
	private static CrawlController startCrawler(String seed, Class<? extends WebCrawler> clazz) {
		try {
			CrawlConfig config = new CrawlConfig();
			config.setCrawlStorageFolder(PATH);
			PageFetcher pageFetcher = new PageFetcher(config);
			RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
			RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
			CrawlController controller;
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
			controller.addSeed(seed);
			//启动
			controller.start(clazz, THREADS_COUNT);
			return controller;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
